package com.mas.bgdt.drool.fact

class Model() extends Serializable {
  private var vin :String = _
  private var birthday :String = _
  private var brand :String = _
  private var series :String = _
  private var infoType :String = _
  private var ruleGroup :String = _

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

  def this(vin: String, birthday: String, brand: String, series: String, infoType: String, ruleGroup:String) {
    this()
    this.vin = vin
    this.birthday = birthday
    this.brand = brand
    this.series = series
    this.infoType = infoType
    this.ruleGroup=ruleGroup
  }

  override def toString: String = "vin:"+this.vin+"|birth:"+this.birthday+"|brand:"+this.brand+"|series:"+this.series+"|info:"+this.infoType+"|rule:"+this.ruleGroup
}