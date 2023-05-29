package com.ctg.pipeline.execute.events;

import javax.annotation.Nonnull;

import com.ctg.pipeline.execute.model.Execution;
import com.ctg.pipeline.execute.model.ExecutionStatus;
import com.ctg.pipeline.execute.model.Stage;
import com.ctg.pipeline.execute.model.Task;

public class TaskComplete extends ExecutionEvent {
  private final String stageId;
  private final String stageType;
  private final String stageName;
  private final String taskId;
  private final String taskType;
  private final String taskName;
  private final ExecutionStatus status;

  public TaskComplete(
      @Nonnull Object source,
      @Nonnull Execution.ExecutionType executionType,
      @Nonnull String executionId,
      @Nonnull String stageId,
      @Nonnull String stageType,
      @Nonnull String stageName,
      @Nonnull String taskId,
      @Nonnull String taskType,
      @Nonnull String taskName,
      @Nonnull ExecutionStatus status) {
    super(source, executionType, executionId);
    this.stageId = stageId;
    this.stageType = stageType;
    this.stageName = stageName;
    this.taskId = taskId;
    this.taskType = taskType;
    this.taskName = taskName;
    this.status = status;
  }

  public TaskComplete(@Nonnull Object source, @Nonnull Stage stage, @Nonnull Task task) {
    this(
        source,
        stage.getExecution().getType(),
        stage.getExecution().getId(),
        stage.getId(),
        stage.getType(),
        stage.getName(),
        task.getId(),
        task.getImplementingClass(),
        task.getName(),
        task.getStatus());
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

  public @Nonnull String getTaskId() {
    return taskId;
  }

  public @Nonnull String getTaskType() {
    return taskType;
  }

  public @Nonnull String getTaskName() {
    return taskName;
  }

  public @Nonnull ExecutionStatus getStatus() {
    return status;
  }
}
