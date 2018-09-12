package com.mas.bgdt.drool.core

import java.sql.Connection
import java.util.Date

import com.mas.bgdt.drool.config.Constants
import com.mas.bgdt.drool.handler.SQLiteHandler
import com.mas.bgdt.drool.kie.{KieSessionPool, RuleFileKie}
import com.mas.bgdt.drool.util.JSONUtil
import org.apache.spark.sql.SparkSession
import org.kie.api.runtime.KieSession

object Application {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("drools_streaming")
      .master("local[*]")
      .getOrCreate()
    spark.sparkContext.setLogLevel("WARN")
    val constants = new Constants(
      "jdbc:sqlite:/root/test.db",
      "192.168.117.117:9092",
      "test",
      "192.168.117.117:9092",
      "test_result")

    val inputstream = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", constants.sourceBroker)
      .option("subscribe", constants.sourceTopic)
      .load()

    import spark.implicits._

    val query = inputstream
      .select($"value")
      .as[String]
      .mapPartitions(partition=>{
        //获取连接
        val conn: Connection = SQLiteHandler.getConnection(constants.jdbcUrl)
        partition.map(x=>{
        try{
          val model = JSONUtil.JSON2Model(x)
          if(model.getSeries.toInt % 100000 ==0) println(model.getSeries +": "+ new Date().getTime)
          val ks = KieSessionPool.getSession(conn,model.getRuleGroup)
          ks.insert(model)
          ks.fireAllRules()
          model.getInfoType
        }catch {
          case e:Exception => e.printStackTrace();""
        }
        })})
      .as[String]
      .writeStream
      .outputMode("append")
      .format("kafka")
      .option("checkpointLocation", "/opt/checkpoint")
      .option("kafka.bootstrap.servers", constants.targetBroker)
      .option("topic", constants.targetTopic)
      .start()

    query.awaitTermination()
  }
}
