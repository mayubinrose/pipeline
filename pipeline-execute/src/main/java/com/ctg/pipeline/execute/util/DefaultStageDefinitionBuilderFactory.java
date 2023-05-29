package com.ctg.pipeline.execute.util;

import com.ctg.pipeline.execute.model.Stage;

import javax.annotation.Nonnull;


public class DefaultStageDefinitionBuilderFactory implements StageDefinitionBuilderFactory {
  private final StageResolver stageResolver;

  public DefaultStageDefinitionBuilderFactory(StageResolver stageResolver) {
    this.stageResolver = stageResolver;
  }

  @Override
  public @Nonnull StageDefinitionBuilder builderFor(@Nonnull Stage stage) {
    return stageResolver.getStageDefinitionBuilder(
        stage.getType(), (String) stage.getContext().get("alias"));
  }
}
