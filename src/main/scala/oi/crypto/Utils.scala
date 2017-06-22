package oi.crypto

import java.time.Instant

import io.circe.Json



object Utils {
  def extractData(json: Json): CurrenciesList = {
    val filtered = json.hcursor.fields.map{_.filter{_.startsWith("BTC")}}.getOrElse(Vector.empty[String])
    val currenciesList = filtered map {curr =>
      for {
        priceToBTC <- json.hcursor.downField(curr).downField("last").as[Double].right
        volume <-  json.hcursor.downField(curr).downField("baseVolume").as[Double].right
      } yield Currency(curr, priceToBTC, volume)

    } map {_.fold(_=> None, v=>Some(v))} flatten

    CurrenciesList(currenciesList, Instant.now.getEpochSecond)
  }

}
case class Currency(name: String, priceToBTC: Double, volumeSize24: Double)
case class CurrenciesList(currencies: Seq[Currency], timeStamp: Long)