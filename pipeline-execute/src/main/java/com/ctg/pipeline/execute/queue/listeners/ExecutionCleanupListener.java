package com.ctg.pipeline.execute.queue.listeners;

import com.ctg.pipeline.execute.model.Execution;
import com.ctg.pipeline.execute.model.ExecutionStatus;
import com.ctg.pipeline.execute.model.Stage;
import java.util.List;

public class ExecutionCleanupListener implements ExecutionListener {
  @Override
  public void beforeExecution(Persister persister, Execution execution) {
    // do nothing
  }

  @Override
  public void afterExecution(
      Persister persister,
      Execution execution,
      ExecutionStatus executionStatus,
      boolean wasSuccessful) {
    if (!execution.getStatus().isSuccessful()) {
      // only want to cleanup executions that successfully completed
      return;
    }

    List<Stage> stages = execution.getStages();
    stages.forEach(
        it -> {
          if (it.getContext().containsKey("targetReferences")) {
            // remove `targetReferences` as it's large and unnecessary after a pipeline has
            // completed
            it.getContext().remove("targetReferences");
            persister.save(it);
          }
        });
  }
}
