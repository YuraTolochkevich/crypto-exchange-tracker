package oi.crypto

import java.util.concurrent.{Executors, ScheduledExecutorService}

import io.circe._
import io.circe.Json
import fs2.{Scheduler, Task, _}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.receiver.Receiver
import org.apache.spark.util.Utils
import org.http4s.Uri
import org.http4s.client.Client
import org.http4s.{EntityEncoder, Uri}
import org.http4s.circe._
import org.http4s.client.blaze.PooledHttp1Client

import scala.collection.mutable
import scala.concurrent.duration._
import scala.concurrent.duration.FiniteDuration

class FromHttpInputDStream(
                                 @transient ssc_ : StreamingContext,
                                 uri: String,
                                 interval: FiniteDuration,
                                 storageLevel: StorageLevel) extends ReceiverInputDStream[CurrenciesList](ssc_)  {

  def getReceiver(): Receiver[CurrenciesList] = {
    new HttpReceiver(uri, interval, storageLevel)
  }
}

case class HttpReceiver(
                         url: String,
                         interval: FiniteDuration,
                         override val storageLevel: StorageLevel) extends Receiver[CurrenciesList](storageLevel) {

  def getTicker(uri: Uri, client: Client) = client.expect[Json](uri)

  def onStop() {
  }

  def onStart() {
    val client = PooledHttp1Client()
    implicit val scheduler = Scheduler.fromFixedDaemonPool(4)
    implicit val strategy = fs2.Strategy.fromCachedDaemonPool()
    val uri = Uri.fromString(url).right.get
    Stream.repeatEval(
      getTicker(uri, client).schedule(interval))
      .map{Utils.extractData}
      .map(store)
      .run
      .unsafeRun()
  }
}