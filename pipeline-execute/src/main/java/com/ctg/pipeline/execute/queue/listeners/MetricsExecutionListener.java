package com.ctg.pipeline.execute.queue.listeners;

import com.ctg.pipeline.execute.model.Execution;
import com.ctg.pipeline.execute.model.ExecutionStatus;
import com.netflix.spectator.api.Id;
import com.netflix.spectator.api.Registry;
import java.util.concurrent.TimeUnit;

import static com.ctg.pipeline.execute.model.Execution.ExecutionType.ORCHESTRATION;

public class MetricsExecutionListener implements ExecutionListener {
  private final Registry registry;

  public MetricsExecutionListener(Registry registry) {
    this.registry = registry;
  }

  @Override
  public void beforeExecution(Persister persister, Execution execution) {
    if (execution.getApplication() == null) {
      return;
    }

    Id id =
        registry
            .createId("executions.started")
            .withTag("executionType", execution.getClass().getSimpleName().toLowerCase())
            .withTag("application", execution.getApplication().toLowerCase());

    registry.counter(id).increment();
  }

  @Override
  public void afterExecution(
      Persister persister,
      Execution execution,
      ExecutionStatus executionStatus,
      boolean wasSuccessful) {
    if (execution.getType() != ORCHESTRATION) {
      // not concerned with pipelines right now (pipelines can have wait stages / manual judgments
      // which skew execution time)
      return;
    }

    if (execution.getApplication() == null
        || execution.getStartTime() == null
        || execution.getEndTime() == null) {
      // should normally have all attributes but a guard just in case
      return;
    }

    Id id =
        registry
            .createId("executions.totalTime")
            .withTag("executionType", "orchestration")
            .withTag("successful", Boolean.valueOf(wasSuccessful).toString())
            .withTag("application", execution.getApplication().toLowerCase());

    registry
        .timer(id)
        .record(execution.getEndTime() - execution.getStartTime(), TimeUnit.MILLISECONDS);
  }
}
