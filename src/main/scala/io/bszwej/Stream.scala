package io.bszwej

import akka.stream.Materializer
import akka.stream.alpakka.s3.impl.{S3Headers, ServerSideEncryption}
import akka.stream.alpakka.s3.scaladsl.{MultipartUploadResult, S3Client}
import akka.stream.scaladsl.{Compression, Sink, Source}
import akka.util.ByteString
import com.mongodb.reactivestreams.client.MongoCollection
import org.bson.Document

import scala.concurrent.Future

class Stream(collection: MongoCollection[Document], s3Client: S3Client, bucket: String, key: String) {

  def run(implicit m: Materializer) = runStream(s3Sink)

  def runWithEncryption(implicit m: Materializer) = runStream(s3SinkWithEncryption)

  private def runStream(sink: Sink[ByteString, Future[MultipartUploadResult]])(implicit m: Materializer) =
    Source.fromPublisher(collection.find())
      .map(_.toJson())
      .map(ByteString(_))
      .via(Compression.gzip)
      .runWith(sink)

  private val s3Sink: Sink[ByteString, Future[MultipartUploadResult]] =
    s3Client.multipartUpload(bucket, key)

  private val s3SinkWithEncryption: Sink[ByteString, Future[MultipartUploadResult]] =
    s3Client.multipartUploadWithHeaders(
      bucket,
      key,
      s3Headers = Some(S3Headers(ServerSideEncryption.AES256))
    )
}
