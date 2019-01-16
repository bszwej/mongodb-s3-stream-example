package io.bszwej

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.s3.S3Settings
import akka.stream.alpakka.s3.scaladsl.S3Client
import akka.stream.scaladsl.{Sink, Source}
import com.mongodb.reactivestreams.client.{MongoClients, MongoCollection}
import org.bson.Document

import scala.util.{Failure, Success}

object Main extends App with Configuration {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  import system.dispatcher

  val s3Client: S3Client = new S3Client(S3Settings(config))

  val collection: MongoCollection[Document] =
    MongoClients.create
      .getDatabase(mongoDatabase)
      .getCollection(mongoCollection)

  val backupStream = new BackupStream(collection, s3Client, bucket, fileName)
  val restoreStream = new RestoreStream(collection, s3Client, bucket, fileName)

  val result = for {
    _ ← backupStream.run
    _ ← Source.fromPublisher(collection.drop()).runWith(Sink.ignore)
    _ ← restoreStream.run
  } yield ()

  result.onComplete {
    case Success(_) ⇒
      println(s"Backup, collection drop and restore ended successfully!")

    case Failure(e) ⇒
      println(s"Backup, collection drop or restore failed. Message: '${e.getMessage}")
  }

  result.onComplete(_ ⇒ system.terminate())

}
