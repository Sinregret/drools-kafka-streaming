package com.mas.bgdt.drool.util

import java.util.Properties

object PropertiesUtil {
  private var properties:Properties= new Properties()

  /**
    * 解析properties文件
    */
  def getKeyValue(prop:String,key:String):String={
    properties.load(this.getClass.getClassLoader.getResourceAsStream(prop))
    return properties.getProperty(key)
  }
  def getProperty(prop:String):Properties={
    properties.load(this.getClass.getClassLoader.getResourceAsStream(prop))
    return properties
  }
}
