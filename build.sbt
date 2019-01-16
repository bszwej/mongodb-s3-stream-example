name := "mongodb-s3-stream-example"

version := "1.0"

scalaVersion := "2.12.8"

val akkaVersion = "2.5.19"
val alpakkaVersion = "0.20"
val mongoDriverVersion = "1.10.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.lightbend.akka" %% "akka-stream-alpakka-s3" % alpakkaVersion,
  "org.mongodb" % "mongodb-driver-reactivestreams" % mongoDriverVersion
)

dependencyOverrides ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion
)
