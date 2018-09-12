package com.mas.bgdt.drool.handler

import java.sql.{Connection, DriverManager,ResultSet}

object SQLiteHandler {

  def getConnection(url:String): Connection = {
    Class.forName("org.sqlite.JDBC")
    DriverManager.getConnection(url)
  }

  def query(connection: Connection,sql:String): ResultSet ={

    val statement = connection.createStatement
    statement.setQueryTimeout(30)
    // 执行查询语句
    statement.executeQuery(sql)
  }
}
