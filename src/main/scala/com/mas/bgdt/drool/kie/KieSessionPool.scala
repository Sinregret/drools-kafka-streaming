/**
  * 管理KieSession的创建和更新
  */

package com.mas.bgdt.drool.kie

import java.sql.Connection
import com.mas.bgdt.drool.config.RuleInfo
import com.mas.bgdt.drool.handler.SQLiteHandler
import org.kie.api.runtime.KieSession

object KieSessionPool {
  private var sessionPool :Map[String,KieSession] = Map[String,KieSession]()
  private var ruleVersion :Map[String,String] = Map[String,String]()
  private var ruleTimer :Map[String,Long] = Map[String,Long]()
  var count :Long = 0

//  def nowTime(): Long = new Date().getTime

  /**
    * 根据 timer、version 获取或新建 KieSession
    * @param connection
    * @param ruleGroup
    * @return
    */
  def getSession(connection: Connection,ruleGroup:String,timestamp:Long): KieSession ={
    val lastTime: Long = ruleTimer.getOrElse(ruleGroup,0)
    if(lastTime <= 0){
      /* 如果是新的rule_group，新建Kiession */
      val ruleInfo = queryRule(connection,ruleGroup)
      addSession(ruleInfo,timestamp)
    }else if ((timestamp - lastTime) >= 10000){
      /* 如果距离上次检查规则10s，则重新检查 */
      ruleTimer = ruleTimer ++ Map(ruleGroup -> timestamp)
      val ruleInfo = queryRule(connection,ruleGroup)
      /* 如果version不匹配，重建KieSession */
      if (!ruleInfo.version.equals(ruleVersion.getOrElse(ruleGroup,""))){
        addSession(ruleInfo,timestamp)
      }
    }
    sessionPool.getOrElse(ruleGroup,null)
  }

  /**
    * 新建 KieSession
    * @param ruleInfo
    */
  def addSession(ruleInfo:RuleInfo,timestamp:Long): Unit ={

    this.synchronized{
      if(ruleTimer.size>=100){
        //无效ruleGroup
        val invalid = ruleTimer.toSeq.sortBy(_._2).take(20).map(_._1)
        //释放内存
        sessionPool.filterKeys(invalid.contains(_)).foreach(_._2.dispose())
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
    * @param connection
    * @param ruleGroup
    * @return
    */
  def queryRule(connection:Connection,ruleGroup:String): RuleInfo ={
//    println("check rule -- "+ ruleGroup)
    val rs = SQLiteHandler.query(connection,s"select file_path,ifnull(update_date,create_date) as update_date from tt_groups where rule_group='${ruleGroup}'")
    val path = rs.getString("file_path")
//    val path = s"/home/mas_drools/tomcat/apache-tomcat-7.0.90/webapps/prj_sgm_iapps_platform/WEB-INF/${ruleGroup}.drl"
    val version = rs.getString("update_date")
    if(path.nonEmpty && version.nonEmpty){
      new RuleInfo(ruleGroup,path,version)
    }else{
      throw new NullPointerException("文件地址或更新日期为空")
    }

  }
}
