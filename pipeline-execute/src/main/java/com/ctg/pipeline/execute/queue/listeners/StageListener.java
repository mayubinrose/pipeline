package com.ctg.pipeline.execute.queue.listeners;

import com.ctg.pipeline.execute.model.ExecutionStatus;
import com.ctg.pipeline.execute.model.Stage;
import com.ctg.pipeline.execute.model.Task;

public interface StageListener {
  default void beforeTask(Persister persister, Stage stage, Task task) {
    // do nothing
  }

  default void beforeStage(Persister persister, Stage stage) {
    // do nothing
  }

  default void afterTask(
      Persister persister,
      Stage stage,
      Task task,
      ExecutionStatus executionStatus,
      boolean wasSuccessful) {
    // do nothing
  }

  default void afterStage(Persister persister, Stage stage) {
    // do nothing
  }
}
