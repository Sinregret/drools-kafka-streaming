package com.mas.bgdt.drool.kie

import java.io.{FileInputStream, FileNotFoundException}

import org.kie.api.io.ResourceType
import org.kie.api.runtime.KieSession
import org.kie.internal.KnowledgeBaseFactory
import org.kie.internal.builder.KnowledgeBuilderFactory
import org.kie.internal.io.ResourceFactory

object RuleFileKie {

  def getSession (path: String): KieSession = {
    val knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder
    try{
      knowledgeBuilder.add(ResourceFactory.newInputStreamResource(new FileInputStream(path)), ResourceType.DRL)
    } catch {
      case e: FileNotFoundException => e.printStackTrace()
    }
    val knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase
    knowledgeBase.addKnowledgePackages(knowledgeBuilder.getKnowledgePackages)
    knowledgeBase.newStatefulKnowledgeSession
  }
}
