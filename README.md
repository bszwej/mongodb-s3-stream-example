# Streaming collection from MongoDB to S3 using Akka Streams

This is an example of using Akka Streams and Alpakka to stream the whole MongoDB collection to Amazon S3.

This example contains a full runnable code presented in [this blog post](https://medium.com/@bszwej/crafting-production-ready-backup-as-a-service-solution-using-akka-streams-130725df20cb).

It uses the following dependencies:
- akka-stream
- akka-stream-alpakka-s3
- mongodb-driver-reactivestreams

## Running

In order to run the stream:

1. Go to `application.conf` and fill the missing properties.

2. Run your MongoDB locally and prepare the database and collection with some documents. You can quickly [run MongoDB as well as MongoShell using Docker](https://hub.docker.com/_/mongo/).

3. `sbt run`
