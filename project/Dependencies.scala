import sbt._

object Dependencies {
  // VERSIONS
  val scalaTestVersion = "3.0.0"
  val scalaCheckVersion = "1.13.4"
  val scalaMockVersion = "3.5.0"
  val kafkaVersion = "0.10.2.0"
  val circeVersion = "0.7.1"
  val logbackVersion = "1.2.3"
  val scalaLoggingVersion = "3.5.0"
  val http4sVersion = "0.17.0-M2"
  val typesafeConfigVersion = "1.3.1"
  val fs2Version = "0.9.5"
  val scalazVersion = "7.1.13"
  val scalazStreamVersion = "0.8.6"
  val sparkCoreVersion = "2.1.1"
  val sparkStreamingVersion = "2.1.1"


  val spark = {
    val sparkCore =  "org.apache.spark" %% "spark-core" % sparkCoreVersion
    val sparkStreaming = "org.apache.spark" %% "spark-streaming" % sparkStreamingVersion
    Seq(sparkCore, sparkStreaming)
  }

  val circe = {
    val circeParser = "io.circe" %% "circe-parser" % circeVersion
    val circeGeneric = "io.circe" %% "circe-generic" % circeVersion
    Seq(circeParser, circeGeneric)
  }
  val logging = {
    val logback = "ch.qos.logback" % "logback-classic" % logbackVersion
    val scalaLogging = "com.typesafe.scala-logging" % "scala-logging_2.11" % scalaLoggingVersion
    Seq(logback, scalaLogging)
  }
  val http4s = {
    val http4sClient = "org.http4s" %% "http4s-blaze-client" % http4sVersion
    val http4sCirce = "org.http4s" %% "http4s-circe" % http4sVersion
    Seq(http4sClient, http4sCirce)
  }


  val typesafeConfig = "com.typesafe" % "config" % typesafeConfigVersion


  val scalaz = {
    val scalazCore = "org.scalaz" % "scalaz-core_2.11" % scalazVersion
    val scalazConcurency = "org.scalaz" % "scalaz-concurrent_2.11" % scalazVersion
    Seq(scalazCore, scalazConcurency)
  }

  val fs2  = "co.fs2" % "fs2-core_2.11" % fs2Version
  // TEST
  val testing = {
    val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion % Test
    val scalaCheck = "org.scalacheck" %% "scalacheck" % scalaCheckVersion % Test
    Seq(scalaTest, scalaCheck)
  }
}