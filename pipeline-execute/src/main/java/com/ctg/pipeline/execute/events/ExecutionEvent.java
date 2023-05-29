package com.ctg.pipeline.execute.events;

import java.time.Instant;

import javax.annotation.Nonnull;

import org.springframework.context.ApplicationEvent;

import com.ctg.pipeline.execute.model.Execution.ExecutionType;

/** Events emitted at various stages in the lifecycle of an execution. */
public abstract class ExecutionEvent extends ApplicationEvent {

  private final ExecutionType executionType;
  private final String executionId;

  protected ExecutionEvent(
      @Nonnull Object source, @Nonnull ExecutionType executionType, @Nonnull String executionId) {
    super(source);
    this.executionType = executionType;
    this.executionId = executionId;
  }

  public final @Nonnull Instant timestamp() {
    return Instant.ofEpochMilli(super.getTimestamp());
  }

  public final @Nonnull ExecutionType getExecutionType() {
    return executionType;
  }

  public final @Nonnull String getExecutionId() {
    return executionId;
  }
}
