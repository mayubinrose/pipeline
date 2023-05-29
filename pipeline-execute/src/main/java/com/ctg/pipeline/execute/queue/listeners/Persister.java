package com.ctg.pipeline.execute.queue.listeners;

import com.ctg.pipeline.execute.model.Execution.ExecutionType;
import com.ctg.pipeline.execute.model.ExecutionStatus;
import com.ctg.pipeline.execute.model.Stage;

public interface Persister {
  void save(Stage stage);

  boolean isCanceled(ExecutionType type, String executionId);

  void updateStatus(ExecutionType type, String executionId, ExecutionStatus executionStatus);
}
