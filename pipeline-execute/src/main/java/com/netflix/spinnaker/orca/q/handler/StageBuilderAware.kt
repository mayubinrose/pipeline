package com.netflix.spinnaker.orca.q.handler

import com.ctg.pipeline.execute.model.Stage
import com.ctg.pipeline.execute.util.StageDefinitionBuilder
import com.ctg.pipeline.execute.util.StageDefinitionBuilderFactory

interface StageBuilderAware {

  val stageDefinitionBuilderFactory: StageDefinitionBuilderFactory

  fun Stage.builder(): StageDefinitionBuilder =
    stageDefinitionBuilderFactory.builderFor(this)
}
