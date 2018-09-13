package com.mas.bgdt.drool.fact

class Model() extends Serializable {
  private var vin :String = ""
  private var birthday :String = ""
  private var brand :String = ""
  private var series :String = ""
  private var infoType :String = ""
  private var ruleGroup :String = ""
  private var timestamp :Long = 0
  private var ruleFlow :String = ""

  def getVin: String = this.vin

  def setVin(vin: String): Unit = {
    this.vin = vin
  }

  def getBirthday: String = this.birthday

  def setBirthday(birthday: String): Unit = {
    this.birthday = birthday
  }

  def getBrand: String = this.brand

  def setBrand(brand: String): Unit = {
    this.brand = brand
  }

  def getSeries: String = this.series

  def setSeries(series: String): Unit = {
    this.series = series
  }

  def getInfoType: String = this.infoType

  def setInfoType(infoType: String): Unit = {
    this.infoType = infoType
  }

  def getRuleGroup: String = this.ruleGroup

  def setRuleGroup(ruleGroup: String): Unit = {
    this.ruleGroup = ruleGroup
  }
  def getTimestamp: Long = this.timestamp

  def setTimestamp(timestamp: Long): Unit = {
    this.timestamp = timestamp
  }
  def getRuleFlow: String = this.ruleFlow

  def setRuleFlow(ruleFlow: String): Unit = {
    this.ruleFlow = ruleFlow
  }
  def this(vin: String, birthday: String, brand: String, series: String, infoType: String, ruleGroup:String, timestamp:Long, ruleFlow:String) {
    this()
    this.vin = vin
    this.birthday = birthday
    this.brand = brand
    this.series = series
    this.infoType = infoType
    this.ruleGroup = ruleGroup
    this.timestamp = timestamp
    this.ruleFlow = ruleFlow
  }

  override def toString: String = s"""{"vin":"${this.vin}","birthday":"${this.birthday}","brand":"${this.brand}","series":"${this.series}","infoType":"${this.infoType}","ruleGroup":"${this.ruleGroup}","timestamp":${this.timestamp},"ruleFlow":"${this.ruleFlow}"}"""
}