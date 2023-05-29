package com.ctg.pipeline.execute.util;

import javax.annotation.Nonnull;

import com.ctg.pipeline.execute.model.Stage;

@FunctionalInterface
public interface StageDefinitionBuilderFactory {
  @Nonnull
  StageDefinitionBuilder builderFor(@Nonnull Stage stage);
}
