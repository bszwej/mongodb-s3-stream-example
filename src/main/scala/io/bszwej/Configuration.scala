package io.bszwej

import com.typesafe.config.ConfigFactory

trait Configuration {

  private val config = ConfigFactory.load()

  val mongoDatabase = config.getString("mongo.db")
  val mongoCollection = config.getString("mongo.collection")

  val awsAccessKeyId = config.getString("aws.access-key-id")
  val awsAccessSecretKey = config.getString("aws.access-secret-key")
  val awsRegion = config.getString("aws.region")

  val bucket = config.getString("aws.bucket")
  val fileName = config.getString("aws.file-name")

}
