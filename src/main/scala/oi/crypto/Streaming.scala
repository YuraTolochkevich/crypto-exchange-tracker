package oi.crypto


import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer
import scala.math._
import scala.concurrent.duration._


class Streaming {

  implicit val strategy = fs2.Strategy.fromCachedDaemonPool()
  val storageLevel: StorageLevel = StorageLevel.MEMORY_AND_DISK_SER_2
  val interval = 10 seconds
  val url = "https://poloniex.com/public?command=returnTicker"

  val sparkConf = new SparkConf().
    setMaster("local[4]").
    setAppName("CryptoMarketStreaming")

  val ssc = new StreamingContext(sparkConf, Seconds(10))
  val receiver = new HttpReceiver(url, interval, storageLevel)
  val cryptoReceiverStream: ReceiverInputDStream[CurrenciesList] = ssc.receiverStream(receiver)

  //  cryptoReceiverStream.reduceByWindow(case (x: Seq[Currency], y: Seq[Currency])=>  )

//  val tenMinutesWindow: DStream[CurrenciesList] = cryptoReceiverStream.window(Seconds(60 * 10), Seconds(10))

  val firstInStream: (CurrenciesList, CurrenciesList) => CurrenciesList = (a, b) => {
    if (a.timeStamp < b.timeStamp) a else b
  }

  val lastInStream: (CurrenciesList, CurrenciesList) => CurrenciesList = (a, b) => {
    if (a.timeStamp > b.timeStamp) a else b
  }



  val arr = new ArrayBuffer[CurrenciesList]();
  cryptoReceiverStream.window(Seconds(60 * 2), Seconds(10)).foreachRDD(arr++_.collect())
  val wstream =cryptoReceiverStream.window(Seconds(60 * 2), Seconds(10)).print()
  val sorted = arr.sortWith((a,b)=>a.timeStamp<b.timeStamp)
  print(sorted)
//  val first = sorted.take(1)

//  cryptoReceiverStream.window(Seconds(60 * 10), Seconds(10)).foreachRDD()
  val tenMinutesWindow = {
    val firstSeqInWindow = getReducedByWindow(cryptoReceiverStream, firstInStream, Seconds(60 * 10), Seconds(10))
    val lastSeqInWindow = getReducedByWindow(cryptoReceiverStream, lastInStream, Seconds(60 * 10), Seconds(10))
//    getWindowChangeforCurrencies(firstSeqInWindow, lastSeqInWindow)
  }
//  tenMinutesWindow.print()
  ssc.start()
  ssc.awaitTermination()

  def getReducedByWindow(stream: => ReceiverInputDStream[CurrenciesList],
                            op: (CurrenciesList, CurrenciesList) => CurrenciesList, windowInterval: Duration,
                            slidingInterval: Duration) = {
//    def reduceFunc(s1: CurrenciesList, s2: CurrenciesList): CurrenciesList =
//      ( s1.currencies zip s2.currencies).map { case (a, b) => op(a, b) }

    stream.reduceByWindow(op, windowInterval, slidingInterval)
  }
//
//  def getWindowChangeforCurrencies(firstSeq: DStream[CurrenciesList],
//                                   lastSeq: DStream[CurrenciesList]): DStream[Seq[GradientCurrency]] = {
//    for {
//      first <- firstSeq
//      last <- lastSeq
//    } yield {
//      (first.currencies zip last.currencies) map { case (f: Currency, l: Currency) =>
//        GradientCurrency(f.name, (f.priceToBTC, l.priceToBTC), (f.volumeSize24, l.volumeSize24))
//      }
//    }
//  }
}

case class GradientCurrency(name: String, priceRange: (Double, Double), volumeRange: (Double, Double)) {
  val percentPriceChange = ((priceRange._2 - priceRange._1)/ priceRange._1) * 100
  val percentMarketChange = ((volumeRange._2 - volumeRange._1)/ volumeRange._1) * 100
}

object  SparkApp extends App {
  new Streaming()
}
