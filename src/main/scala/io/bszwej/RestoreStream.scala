package io.bszwej

import akka.stream.Materializer
import akka.stream.alpakka.s3.scaladsl.S3Client
import akka.stream.scaladsl.{Compression, JsonFraming, Sink, Source}
import akka.util.ByteString
import com.mongodb.client.model.InsertOneModel
import com.mongodb.reactivestreams.client.MongoCollection
import org.bson.Document

import scala.collection.JavaConverters._
import scala.collection.immutable.Seq

class RestoreStream(collection: MongoCollection[Document], s3Client: S3Client, bucket: String, fileName: String) {

  private val maximumObjectLength = 16000000
  private val bulkSize = 100

  def run(implicit m: Materializer) = {
    val (s3Source, _) = s3Client.download(bucket, fileName)

    s3Source
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
}
