//package com.mas.bgdt.drool.core
//
//import com.mas.bgdt.drool.config.Constants
//import com.mas.bgdt.drool.fact.Model
//import com.mas.bgdt.drool.kie.RuleFileKie
//import org.apache.spark.sql.SparkSession
//
//object RuleFileApp {
//  def main(args: Array[String]): Unit = {
//    val spark = SparkSession
//      .builder()
//      .appName("prj_sgm_iapps_platform")
//      .master("local[2]")
//      .getOrCreate()
//    spark.sparkContext.setLogLevel("WARN")
//    val constants = new Constants(
//      "",
//      "192.168.117.117:9092",
//      "test",
//      "192.168.117.117:9092",
//      "test1")
//
//    val inputstream = spark.readStream
//      .format("kafka")
//      .option("kafka.bootstrap.servers", constants.sourceBroker)
//      .option("subscribe", constants.sourceTopic)
//      .load()
//    import spark.implicits._
//    val query = inputstream.select($"value")
//      .as[String]
//      .map(x=>{
//        var info = "no change"
//        try{
//          val mstr = x.split(";")
//          val model = new Model()
//          model.setBrand(x)
//          val ks = RuleFileKie.getSession(args(0))
//          ks.insert(model)
//          ks.fireAllRules()
//          ks.dispose()
//          info = model.getInfoType
//        }catch {
//          case e:Exception => info = "error";e.printStackTrace()
//        }
//        info
//      })
//      .as[String]
//      .writeStream
//      .outputMode("append")
//      .format("console")
//      .start()
//
//    query.awaitTermination()
//  }
//}
