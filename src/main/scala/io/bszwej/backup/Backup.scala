package io.bszwej.backup

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.s3.auth.AWSCredentials
import akka.stream.alpakka.s3.scaladsl.{MultipartUploadResult, S3Client}
import com.mongodb.reactivestreams.client.{MongoClients, MongoCollection}
import io.bszwej.Configuration
import org.bson.Document

import scala.concurrent.Future
import scala.util.{Failure, Success}

object Backup extends App with Configuration {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  import system.dispatcher

  val collection: MongoCollection[Document] =
    MongoClients.create
      .getDatabase(mongoDatabase)
      .getCollection(mongoCollection)

  val s3Client: S3Client =
    S3Client(AWSCredentials(awsAccessKeyId, awsAccessSecretKey), awsRegion)

  val result: Future[MultipartUploadResult] =
    new BackupStream(collection, s3Client, bucket, fileName).runWithEncryption

  result.onComplete {
    case Success(_) ⇒
      println(s"Backup ended successfully!")

    case Failure(e) ⇒
      println(s"Backup failed. Message: '${e.getMessage}")
  }

  // If you're worried about AbruptTerminationException
  // see: https://github.com/akka/alpakka/issues/203
  result.onComplete(_ ⇒ system.terminate())

}
