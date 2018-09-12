package com.mas.bgdt.drool.config

class RuleInfo (
                 val ruleGroup:String,
                 val path:String,
                 val version:String
               ) extends Serializable
object RuleInfo{
  def apply(
             ruleGroup:String,
             path:String,
             version:String
           ) = new RuleInfo(ruleGroup,path,version)
}