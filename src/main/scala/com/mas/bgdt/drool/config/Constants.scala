package com.mas.bgdt.drool.config

import com.mas.bgdt.drool.util.PropertiesUtil

class Constants(
                 val jdbcUrl:String,
                 val sourceBroker:String,
                 val sourceTopic:String,
                 val targetBroker:String,
                 val targetTopic:String
                ) extends Serializable

object Constants{
  def apply(
             jdbcUrl:String,
             sourceBroker:String,
             sourceTopic:String,
             targetBroker:String,
             targetTopic:String
           ) = new Constants(jdbcUrl,sourceBroker,sourceTopic,targetBroker,targetTopic)

  def apply() = {
    val config = "conf.properties"
    new Constants(
      PropertiesUtil.getKeyValue(config,"db_path"),
      PropertiesUtil.getKeyValue(config,"source_brokers"),
      PropertiesUtil.getKeyValue(config,"source_topic"),
      PropertiesUtil.getKeyValue(config,"target_brokers"),
      PropertiesUtil.getKeyValue(config,"target_topic"))
  }
}

