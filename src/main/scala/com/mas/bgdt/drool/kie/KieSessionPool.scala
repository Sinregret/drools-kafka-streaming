/**
  * 管理KieSession的创建和更新
  */

package com.mas.bgdt.drool.kie

import java.util.Date
import com.mas.bgdt.drool.config.{Constants, RuleInfo}
import com.mas.bgdt.drool.handler.SQLiteHandler
import org.kie.api.runtime.KieSession

object KieSessionPool {
  private var sessionPool :Map[String,KieSession] = Map[String,KieSession]()
  private var ruleVersion :Map[String,String] = Map[String,String]()
  private var ruleTimer :Map[String,Long] = Map[String,Long]()
  val DBPath: String = Constants().jdbcUrl
  //count用于调试
  var count :Long = 0

  /**
    * 根据 timer、version 获取或新建 KieSession
    */
  def getSession(ruleGroup:String): KieSession ={
    val timestamp = new Date().getTime
    val lastTime: Long = ruleTimer.getOrElse(ruleGroup,0)
    if(lastTime <= 0){
      /* 如果是新的rule_group，新建Kiession */
      val ruleInfo = queryRule(ruleGroup)
      addSession(ruleInfo,timestamp)
    }else if ((timestamp - lastTime) >= 5000){
      /* 如果距离上次检查规则10s，则重新检查 */
      ruleTimer = ruleTimer ++ Map(ruleGroup -> timestamp)
      val ruleInfo = queryRule(ruleGroup)
    /* 如果version不匹配，重建KieSession */
    if (!ruleInfo.version.equals(ruleVersion.getOrElse(ruleGroup,""))){
      addSession(ruleInfo,timestamp)
    }
  }
    sessionPool.getOrElse(ruleGroup,null)
  }

  /**
    * 新建 KieSession
    */
  def addSession(ruleInfo:RuleInfo,timestamp:Long): Unit ={

    this.synchronized{
      if(ruleTimer.size>=100){
        //无效ruleGroup
        val invalid = ruleTimer.toSeq.sortBy(_._2).take(20).map(_._1)
        //释放内存
        sessionPool.filterKeys(invalid.contains(_)).foreach(_._2.dispose())
        //刪除无效ruleGroup
        ruleTimer = ruleTimer.filterKeys(!invalid.contains(_))
        ruleVersion = ruleVersion.filterKeys(!invalid.contains(_))
        sessionPool = sessionPool.filterKeys(!invalid.contains(_))
      }
      println(s"======= Add Session ${ruleInfo.ruleGroup} =============")
      sessionPool = sessionPool ++ Map(ruleInfo.ruleGroup -> RuleFileKie.getSession(ruleInfo.path))
      ruleVersion = ruleVersion ++ Map(ruleInfo.ruleGroup -> ruleInfo.version)
      ruleTimer = ruleTimer ++ Map(ruleInfo.ruleGroup -> timestamp)
    }
  }

  /**
    * 连接SQLite，查询规则文件基础信息
    */
  def queryRule(ruleGroup:String): RuleInfo ={
    println(s"=============query==================")
    val conn = SQLiteHandler.getConnection(DBPath)
//    val rs = SQLiteHandler.query(connection,s"select file_path,ifnull(update_date,create_date) as update_date from tt_groups where rule_group='${ruleGroup}'")
    val rs = SQLiteHandler.query(conn,s"select path,last_update from test where rule_group='${ruleGroup}'")
    val path = rs.getString("path")
    val version = rs.getString("last_update")
    conn.close()
    if(path.nonEmpty && version.nonEmpty){
      new RuleInfo(ruleGroup,path,version)
    }else{
      throw new NullPointerException("文件地址或更新日期为空")
    }
  }
}
