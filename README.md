# MongoDB backup and restore

This is an example of using Akka Streams to:

1. Backup MongoDB collection to AWS S3.
1. Restore it from AWS S3 back to MongoDB.

This example contains a full runnable code presented in a two-part article:
 - [Crafting production-ready Backup as a Service solution using Akka Streams](https://medium.com/@bszwej/crafting-production-ready-backup-as-a-service-solution-using-akka-streams-130725df20cb)
- [Crafting production-ready Backup as a Service solution using Akka Streams: part 2](https://medium.com/@bszwej/crafting-production-ready-backup-as-a-service-solution-using-akka-streams-part-2-5ac84ca45b47)

## Dependencies

- Akka Streams
- Alpakka S3 connector
- MongoDB Reactive Streams driver

## Running

The scenario located in the `Main` is the following:

1. Perform a backup to S3.
1. Drop the collection.
1. Perform a restore.

This project uses [Minio](https://www.minio.io/), a fully compatible S3 object storage, to replace Amazon S3. It's being ran locally in a docker-compose along with MongoDB. That's why you'll be able to run this example in less than a minute!

Steps to run the example:

1. Create dirs 
    ```sh
    mkdir -p /tmp/data/mybucket /tmp/config
    ```

1. Run Minio and MongoDB 
    ```sh 
    docker-compose up -d
    ```

1. Run Mongo shell 
    ```sh
    docker run -it --net host --rm mongo sh -c 'exec mongo "localhost:27017"'
    ```

1. Insert a document
    ```sh
    > use CookieDB
    switched to db CookieDB

    > db.cookies.insert({"name" : "cookie1", "delicious" : true})
    WriteResult({ "nInserted" : 1 })
    ```

1. `sbt run`
    In the default scenario, the collection is being backed up to Minio, removed from Mongo and restored. You can go to http://127.0.0.1:9000/minio/mybucket/ (login: `minio_access_key`, password: `minio_secret_key`) and see the backup file (`backup.json`).

1. Clean up afterwards
    ```bash
    docker-compose down
    ```

## MongoDB basics

The following snippet presents a basic set of MongoDB commands useful when playing with backup/restore streams.

```sh
> show dbs
admin  0.000GB
local  0.000GB

> use CookieDB
switched to db CookieDB

> db.cookies.insert({"name" : "cookie1", "delicious" : true})
WriteResult({ "nInserted" : 1 })

> show collections
cookies

> db.cookies.find()
{ "_id" : ObjectId("599b0d9a266a67c9516e0245"), "name" : "cookie1", "delicious" : true }

> db.dropDatabase()
{ "dropped" : "CookieDB", "ok" : 1 }
```
