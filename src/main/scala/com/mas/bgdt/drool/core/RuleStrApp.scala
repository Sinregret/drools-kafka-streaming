package com.mas.bgdt.drool.core

import com.mas.bgdt.drool.config.Constants
import com.mas.bgdt.drool.fact.Model
import com.mas.bgdt.drool.kie.RuleStrKie
import org.apache.spark.sql.SparkSession

object RuleStrApp {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("Spark structured streaming Kafka example")
      .master("local[2]")
      .getOrCreate()
    spark.sparkContext.setLogLevel("WARN")
    val rule = ""
    val constants = new Constants("","192.168.117.117:9092","test","","")

    val inputstream = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", constants.sourceBroker)
      .option("subscribe", constants.sourceTopic)
      .load()
    import spark.implicits._
    val query = inputstream.select($"value")
      .as[String]
      .map(x=>{
        var info = "no change"
        try{
          val pstr = x.split(" ")
          val model = new Model()
          val ks = RuleStrKie.getSession(rule)
          ks.insert(model)
          ks.fireAllRules()
          ks.dispose()
          info = model.toString
        }catch {
          case e:Exception => info = "error";e.printStackTrace()
        }
        info
      })
      .as[String]
      .writeStream
      .outputMode("append")
      .format("console")
      .start()

    query.awaitTermination()
  }
}
