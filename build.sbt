name := "mongodb-s3-stream-example"

version := "1.0"

scalaVersion := "2.12.5"

val akkaVersion = "2.5.11"
val alpakkaVersion = "0.18"
val mongoDriverVersion = "1.7.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.lightbend.akka" %% "akka-stream-alpakka-s3" % alpakkaVersion,
  "org.mongodb" % "mongodb-driver-reactivestreams" % mongoDriverVersion
)

dependencyOverrides ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion
)
