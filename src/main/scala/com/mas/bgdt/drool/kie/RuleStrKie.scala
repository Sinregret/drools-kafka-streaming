package com.mas.bgdt.drool.kie

import org.kie.api.io.ResourceType
import org.kie.api.runtime.KieSession
import org.kie.internal. KnowledgeBaseFactory
import org.kie.internal.builder. KnowledgeBuilderFactory
import org.kie.internal.builder.conf.RuleEngineOption
import org.kie.internal.io.ResourceFactory

object RuleStrKie {
  def getSession(rule: String): KieSession = {
    val builder = KnowledgeBuilderFactory.newKnowledgeBuilder
    builder.add(ResourceFactory.newByteArrayResource(rule.getBytes), ResourceType.DRL)

    if (builder.hasErrors) throw new RuntimeException(builder.getErrors.toString)

    val kbase = KnowledgeBaseFactory.newKnowledgeBase
    kbase.addKnowledgePackages(builder.getKnowledgePackages)
    kbase.newStatefulKnowledgeSession
  }

//  def getSession2(rule: String): KieSession = {
//    val builder = KnowledgeBuilderFactory.newKnowledgeBuilder
//    builder.add(ResourceFactory.newByteArrayResource(rule.getBytes), ResourceType.DRL)
//    if (builder.hasErrors) throw new RuntimeException(builder.getErrors.toString)
//    val kconf = KnowledgeBaseFactory.newKnowledgeBaseConfiguration
//    kconf.setOption(RuleEngineOption.PHREAK)
//    val knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase(kconf)
//    knowledgeBase.addKnowledgePackages(builder.getKnowledgePackages)
//    knowledgeBase.newStatefulKnowledgeSession
//  }
}
