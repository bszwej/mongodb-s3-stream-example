# Streaming collection from MongoDB to S3 using Akka Streams

This example is a full runnable code presented in [this blog post](http://medium.com).

It uses the following dependencies:
- akka-stream
- akka-stream-alpakka-s3
- mongodb-driver-reactivestreams

in order to stream the whole MongoDB collection to a file on Amazon S3.

## Running

1. Go to `application.conf` and fill the missing properties.

2. Run your MongoDB locally and prepare the database and collection with some documents. You can quickly [run MongoDB as well as MongoShell using Docker](https://hub.docker.com/_/mongo/).

3. `sbt run`
