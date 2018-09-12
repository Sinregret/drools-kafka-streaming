package com.mas.bgdt.drool.kie

import java.sql.Connection
import java.util.Date
import com.mas.bgdt.drool.config.RuleInfo
import com.mas.bgdt.drool.handler.SQLiteHandler
import org.kie.api.runtime.KieSession

object KieSessionPool {
  private var sessionPool :Map[String,KieSession] = Map[String,KieSession]()
  private var ruleVersion :Map[String,String] = Map[String,String]()
  private var ruleTimer :Map[String,Long] = Map[String,Long]()

  def nowTime(): Long = new Date().getTime

  /**
    * 根据 timer、version 获取或新建 KieSession
    * @param connection
    * @param ruleGroup
    * @return
    */
  def getSession(connection: Connection,ruleGroup:String): KieSession ={
    val lastTime: Long = ruleTimer.getOrElse(ruleGroup,0)
    val now = nowTime()
    if(lastTime <= 0){
      /* 如果是新的rule_group，新建Kiession */
      val ruleInfo = queryRule(connection,ruleGroup)
      addSession(ruleInfo)
    }else if ((now - lastTime) >= 10000){
      ruleTimer = ruleTimer ++ Map(ruleGroup -> now)
      /* 如果距离上次检查规则10s，则重新检查 */
      val ruleInfo = queryRule(connection,ruleGroup)
      /* 如果version不匹配，重建KieSession */
      if (!ruleInfo.version.equals(ruleVersion.getOrElse(ruleGroup,""))){
        addSession(ruleInfo)
      }
    }
    sessionPool.getOrElse(ruleGroup,null)
  }

  /**
    * 新建 KieSession
    * @param ruleInfo
    */
  def addSession(ruleInfo:RuleInfo): Unit ={

    if(sessionPool.size>=20){
      sessionPool = sessionPool.drop(5)
      ruleVersion = ruleVersion.filterKeys(sessionPool.keySet.contains(_))
      ruleTimer = ruleTimer.filterKeys(sessionPool.keySet.contains(_))
    }
    println("======= Add Session =============")
    sessionPool = sessionPool ++ Map(ruleInfo.ruleGroup -> RuleFileKie.getSession(ruleInfo.path))
    ruleVersion = ruleVersion ++ Map(ruleInfo.ruleGroup -> ruleInfo.version)
    ruleTimer = ruleTimer ++ Map(ruleInfo.ruleGroup -> nowTime())
  }

  /**
    * 连接SQLite，查询规则文件基础信息
    * @param connection
    * @param ruleGroup
    * @return
    */
  def queryRule(connection:Connection,ruleGroup:String): RuleInfo ={
    println("check rule -- "+ nowTime())
    val rs = SQLiteHandler.query(connection,s"select path,version from test where rule_group='${ruleGroup}'")
    val path = rs.getString("path")
    val version = rs.getString("version")
    new RuleInfo(ruleGroup,path,version)
  }
}
