package com.ctg.pipeline.execute.util;

import com.ctg.pipeline.execute.model.Stage;

import javax.annotation.Nonnull;


public class SimpleStageDefinitionBuilder implements StageDefinitionBuilder {
  private SimpleStage simpleStage;

  public SimpleStageDefinitionBuilder(SimpleStage simpleStage) {
    this.simpleStage = simpleStage;
  }

  public void taskGraph(@Nonnull Stage stage, @Nonnull TaskNode.Builder builder) {
    SimpleTask task = new SimpleTask(simpleStage);
    builder.withTask(simpleStage.getName(), task.getClass());
  }
}
