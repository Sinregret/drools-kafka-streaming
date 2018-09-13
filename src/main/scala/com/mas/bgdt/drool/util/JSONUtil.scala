package com.mas.bgdt.drool.util

import com.google.gson.{Gson, GsonBuilder}
import com.mas.bgdt.drool.fact.Model

object JSONUtil {

  val gson = new Gson()

  val gson2 = new GsonBuilder().serializeNulls.create()

  def JSON2Model(json:String): Model = gson.fromJson(json,classOf[Model])

  /**
    * 不保留空值字段
    * @param model
    * @return
    */
  def Model2JSON(model:Model):String = gson.toJson(model, classOf[Model])

  /**
    * 保留空值字段
    * @param model
    * @return
    */
  def Model2JSON2(model:Model):String = gson2.toJson(model, classOf[Model])
}
