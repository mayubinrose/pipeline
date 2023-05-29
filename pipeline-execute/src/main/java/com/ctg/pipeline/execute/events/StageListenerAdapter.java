package com.ctg.pipeline.execute.events;

import java.util.List;

import com.ctg.pipeline.execute.model.Execution;
import com.ctg.pipeline.execute.model.ExecutionStatus;
import com.ctg.pipeline.execute.model.Stage;
import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository;
import com.ctg.pipeline.execute.queue.listeners.DefaultPersister;
import com.ctg.pipeline.execute.queue.listeners.Persister;
import com.ctg.pipeline.execute.queue.listeners.StageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;


/**
 * This listener translates events coming from the nu-orca queueing system to the old Spring Batch
 * style listeners. Once we're running full-time on the queue we should simplify things.
 */
public final class StageListenerAdapter implements ApplicationListener<ExecutionEvent> {

  private final StageListener delegate;
  private final ExecutionRepository repository;
  private final Persister persister;

  @Autowired
  public StageListenerAdapter(StageListener delegate, ExecutionRepository repository) {
    this.delegate = delegate;
    this.repository = repository;
    this.persister = new DefaultPersister(this.repository);
  }

  @Override
  public void onApplicationEvent(ExecutionEvent event) {
    if (event instanceof StageStarted) {
      onStageStarted((StageStarted) event);
    } else if (event instanceof StageComplete) {
      onStageComplete((StageComplete) event);
    } else if (event instanceof TaskStarted) {
      onTaskStarted((TaskStarted) event);
    } else if (event instanceof TaskComplete) {
      onTaskComplete((TaskComplete) event);
    }
  }

  private void onStageStarted(StageStarted event) {
    Execution execution = retrieve(event);
    List<Stage> stages = execution.getStages();
    stages.stream()
        .filter(it -> it.getId().equals(event.getStageId()))
        .findFirst()
        .ifPresent(stage -> delegate.beforeStage(persister, stage));
  }

  private void onStageComplete(StageComplete event) {
    Execution execution = retrieve(event);
    List<Stage> stages = execution.getStages();
    stages.stream()
        .filter(it -> it.getId().equals(event.getStageId()))
        .findFirst()
        .ifPresent(stage -> delegate.afterStage(persister, stage));
  }

  private void onTaskStarted(TaskStarted event) {
    Execution execution = retrieve(event);
    List<Stage> stages = execution.getStages();
    stages.stream()
        .filter(it -> it.getId().equals(event.getStageId()))
        .findFirst()
        .ifPresent(
            stage ->
                delegate.beforeTask(
                    persister,
                    stage,
                    stage.getTasks().stream()
                        .filter(it -> it.getId().equals(event.getTaskId()))
                        .findFirst()
                        .get()));
  }

  private void onTaskComplete(TaskComplete event) {
    Execution execution = retrieve(event);
    List<Stage> stages = execution.getStages();
    ExecutionStatus status = event.getStatus();
    stages.stream()
        .filter(it -> it.getId().equals(event.getStageId()))
        .findFirst()
        .ifPresent(
            stage ->
                delegate.afterTask(
                    persister,
                    stage,
                    stage.getTasks().stream()
                        .filter(it -> it.getId().equals(event.getTaskId()))
                        .findFirst()
                        .get(),
                    status,
                    // TODO: not sure if status.isSuccessful covers all the weird cases here
                    status.isSuccessful()));
  }

  private Execution retrieve(ExecutionEvent event) {
    return repository.retrieve(event.getExecutionType(), event.getExecutionId());
  }
}
