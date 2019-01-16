package io.bszwej

import com.typesafe.config.ConfigFactory

trait Configuration {

  val config = ConfigFactory.load()

  val mongoDatabase = config.getString("mongo.db")
  val mongoCollection = config.getString("mongo.collection")

  val bucket = config.getString("backup.bucket")
  val fileName = config.getString("backup.file-name")

}
