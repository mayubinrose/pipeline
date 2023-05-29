package com.ctg.pipeline.execute.events;

import javax.annotation.Nonnull;

import com.ctg.pipeline.execute.model.Stage;
import com.ctg.pipeline.execute.model.Execution.ExecutionType;

public final class StageStarted extends ExecutionEvent {
  private final String stageId;
  private final String stageType;
  private final String stageName;

  public StageStarted(
      @Nonnull Object source,
      @Nonnull ExecutionType executionType,
      @Nonnull String executionId,
      @Nonnull String stageId,
      @Nonnull String stageType,
      @Nonnull String stageName) {
    super(source, executionType, executionId);
    this.stageId = stageId;
    this.stageType = stageType;
    this.stageName = stageName;
  }

  public StageStarted(@Nonnull Object source, @Nonnull Stage stage) {
    this(
        source,
        stage.getExecution().getType(),
        stage.getExecution().getId(),
        stage.getId(),
        stage.getType(),
        stage.getName());
  }

  public @Nonnull String getStageId() {
    return stageId;
  }

  public @Nonnull String getStageType() {
    return stageType;
  }

  public @Nonnull String getStageName() {
    return stageName;
  }
}
