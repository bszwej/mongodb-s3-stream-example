package io.bszwej.restore

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.s3.auth.AWSCredentials
import akka.stream.alpakka.s3.scaladsl.S3Client
import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.mongodb.reactivestreams.client.{MongoClients, MongoCollection}
import io.bszwej.Configuration
import org.bson.Document

import scala.util.{Failure, Success}

object Restore extends App with Configuration {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  import system.dispatcher

  val s3Client: S3Client =
    S3Client(AWSCredentials(awsAccessKeyId, awsAccessSecretKey), awsRegion)

  val s3Source: Source[ByteString, NotUsed] = s3Client.download(bucket, fileName)

  val collection: MongoCollection[Document] =
    MongoClients.create
      .getDatabase(mongoDatabase)
      .getCollection(mongoCollection)

  val maximumObjectLength = 16000000
  val bulkSize = 100
  val result = new RestoreStream(collection, s3Client, bucket, fileName).run

  result.onComplete {
    case Success(_) ⇒
      println(s"Restore ended successfully!")

    case Failure(e) ⇒
      println(s"Restore failed. Message: '${e.getMessage}")
  }

  result.onComplete(_ ⇒ system.terminate())

}
