package com.mas.bgdt.drool.config

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
}

