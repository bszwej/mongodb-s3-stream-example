name := "mongo-s3-backup"

version := "1.0"

scalaVersion := "2.12.3"

val akkaVersion = "2.5.4"
val alpakkaVersion = "0.11"
val mongoDriverVersion = "1.5.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.lightbend.akka" %% "akka-stream-alpakka-s3" % alpakkaVersion,
  "org.mongodb" % "mongodb-driver-reactivestreams" % mongoDriverVersion
)

dependencyOverrides += "com.typesafe.akka" %% "akka-stream" % akkaVersion
dependencyOverrides += "com.typesafe.akka" %% "akka-actor" % akkaVersion
