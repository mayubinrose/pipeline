package com.ctg.pipeline.execute.events;

import javax.annotation.Nonnull;

import com.ctg.pipeline.execute.model.ExecutionStatus;
import com.ctg.pipeline.execute.model.Stage;
import com.ctg.pipeline.execute.model.Execution.ExecutionType;

public final class StageComplete extends ExecutionEvent {
  private final String stageId;
  private final String stageType;
  private final String stageName;
  private final ExecutionStatus status;

  public StageComplete(
      @Nonnull Object source,
      @Nonnull ExecutionType executionType,
      @Nonnull String executionId,
      @Nonnull String stageId,
      @Nonnull String stageType,
      @Nonnull String stageName,
      @Nonnull ExecutionStatus status) {
    super(source, executionType, executionId);
    this.stageId = stageId;
    this.stageType = stageType;
    this.stageName = stageName;
    this.status = status;
  }

  public StageComplete(@Nonnull Object source, @Nonnull Stage stage) {
    this(
        source,
        stage.getExecution().getType(),
        stage.getExecution().getId(),
        stage.getId(),
        stage.getType(),
        stage.getName(),
        stage.getStatus());
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

  public @Nonnull ExecutionStatus getStatus() {
    return status;
  }
}
