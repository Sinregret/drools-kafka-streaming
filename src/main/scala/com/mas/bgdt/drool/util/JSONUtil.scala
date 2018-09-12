package com.mas.bgdt.drool.util

import com.google.gson.Gson
import com.mas.bgdt.drool.fact.Model

object JSONUtil {

  val gson = new Gson()

  def JSON2Model(json:String): Model = gson.fromJson(json,classOf[Model])

  def Model2JSON(model:Model):String = gson.toJson(model, classOf[Model])
}
