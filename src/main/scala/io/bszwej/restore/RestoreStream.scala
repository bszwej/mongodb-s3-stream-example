package io.bszwej.restore

import akka.stream.Materializer
import akka.stream.alpakka.s3.scaladsl.S3Client
import akka.stream.scaladsl.{Compression, JsonFraming, Sink, Source}
import akka.util.ByteString
import com.mongodb.client.model.InsertOneModel
import com.mongodb.reactivestreams.client.MongoCollection
import io.bszwej.restore.Restore.{bulkSize, fileName, maximumObjectLength}
import org.bson.Document

import scala.collection.JavaConverters._
import scala.collection.immutable.Seq

class RestoreStream(collection: MongoCollection[Document], s3Client: S3Client, bucket: String, key: String) {

  def run(implicit m: Materializer) =
    s3Client
      .download(bucket, fileName)
      .via(Compression.gunzip())
      .via(JsonFraming.objectScanner(maximumObjectLength))
      .map((json: ByteString) ⇒ Document.parse(json.utf8String))
      .map((d: Document) ⇒ new InsertOneModel[Document](d))
      .grouped(bulkSize)
      .flatMapConcat { (docs: Seq[InsertOneModel[Document]]) ⇒
        Source.fromPublisher(collection.bulkWrite(docs.toList.asJava))
      }
      .runWith(Sink.ignore)

}
