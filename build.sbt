import Dependencies._


name := "crypto-exchange-tracker"

scalaVersion in ThisBuild := "2.11.8"
resolvers += "JBoss" at "https://repository.jboss.org/"

lazy val commonSettings = Seq(
  organization := "io.crypto",
  version := "0.1",
  scalaVersion := "2.11.8",
  parallelExecution in Test := false,
  scalacOptions := Seq(
    "-deprecation",
    "-explaintypes",
    "-feature",
    "-language:postfixOps",
    "-language:implicitConversions",
    "-target:jvm-1.8",
    "-unchecked",
    "-Xcheckinit",
    "-Xfuture",
    "-Xlint",
    "-Xverify",
    "-Ywarn-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-inaccessible",
    "-Ywarn-infer-any",
    "-Ywarn-nullary-override",
    "-Ywarn-nullary-unit",
    "-Ywarn-unused",
    "-Ywarn-unused-import"
  )
)

lazy val `crypto-exchange-tracker` = (project in file("."))
  .settings(
    name := "crypto-exchange-tracker",
    mainClass in assembly := Some("io.crypto"),
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case x => MergeStrategy.first
    },
    assemblyJarName in assembly := name.value + ".jar",
    resolvers += "JBoss" at "https://repository.jboss.org/",
    libraryDependencies ++= Seq(typesafeConfig, fs2) ++ spark ++ scalaz ++ http4s ++ circe ++ logging ++ Dependencies.testing
  )
