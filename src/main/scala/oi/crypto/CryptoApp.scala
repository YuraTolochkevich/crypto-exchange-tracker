package oi.crypto

import org.apache.spark.storage.StorageLevel
import org.http4s.{ParseResult, Uri}
import org.http4s.client.blaze.PooledHttp1Client

import scala.concurrent.duration._
import scalaz.\/-

object CryptoApp  extends App{
  val uri ="https://poloniex.com/public?command=returnTicker"
  val storageLevel: StorageLevel = StorageLevel.MEMORY_AND_DISK_SER_2
  val interval = 10 seconds
  implicit val strategy = fs2.Strategy.fromCachedDaemonPool()
  val receiver = new  HttpReceiver(uri,interval, storageLevel)
//  val json = receiver.getTicker(uri, ).unsafeRun()
//  print(json.hcursor.fields.map{f=>f.filter(_.startsWith("BTC"))})
//  val filtered = json.hcursor.fields.map{_.filter{_.startsWith("BTC")}}.getOrElse(Vector.empty[String])
//
//  val currenciesList = filtered map {curr =>
//    for {
//      priceToBTC <- json.hcursor.downField(curr).downField("last").as[Double].right
//      volume <-  json.hcursor.downField(curr).downField("baseVolume").as[Double].right
//    } yield Currency(curr, priceToBTC, volume)
//
//  } map {_.fold(_=>(), v=>v)}
//  currenciesList foreach {println}

//  print(receiver.getTicker(uri).unsafeRun().unsafeRun())

}
