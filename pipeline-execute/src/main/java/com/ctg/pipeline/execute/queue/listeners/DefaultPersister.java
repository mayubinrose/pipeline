package com.ctg.pipeline.execute.queue.listeners;


import com.ctg.pipeline.execute.model.Execution.ExecutionType;
import com.ctg.pipeline.execute.model.ExecutionStatus;
import com.ctg.pipeline.execute.model.Stage;
import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository;

public class DefaultPersister implements Persister {
  private final ExecutionRepository executionRepository;

  public DefaultPersister(ExecutionRepository executionRepository) {
    this.executionRepository = executionRepository;
  }

  @Override
  public void save(Stage stage) {
    executionRepository.storeStage(stage);
  }

  @Override
  public boolean isCanceled(ExecutionType type, String executionId) {
    return executionRepository.isCanceled(type, executionId);
  }

  @Override
  public void updateStatus(
      ExecutionType type, String executionId, ExecutionStatus executionStatus) {
    executionRepository.updateStatus(type, executionId, executionStatus);
  }
}
