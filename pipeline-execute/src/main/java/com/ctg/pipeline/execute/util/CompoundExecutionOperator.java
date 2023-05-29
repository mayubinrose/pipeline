package com.ctg.pipeline.execute.util;

import java.time.Duration;

import javax.annotation.Nullable;

import com.ctg.pipeline.execute.model.AuthenticatedRequest;
import com.ctg.pipeline.execute.model.Execution.ExecutionType;
import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository;
import com.netflix.spinnaker.kork.core.RetrySupport;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompoundExecutionOperator {
  private ExecutionRepository repository;
  private ExecutionRunner runner;
  private RetrySupport retrySupport;

  public CompoundExecutionOperator(
      ExecutionRepository repository, ExecutionRunner runner, RetrySupport retrySupport) {
    this.repository = repository;
    this.runner = runner;
    this.retrySupport = retrySupport;
  }

  public void cancel(ExecutionType executionType, String executionId) {
    cancel(
        executionType,
        executionId,
        AuthenticatedRequest.getSpinnakerUser().orElse("anonymous"),
        null);
  }

  public void cancel(ExecutionType executionType, String executionId, String user, String reason) {
    doInternal(
        () -> runner.cancel(repository.retrieve(executionType, executionId), user, reason),
        () -> repository.cancel(executionType, executionId, user, reason),
        "cancel",
        executionType,
        executionId);
  }

  public void delete(ExecutionType executionType, String executionId) {
    repository.delete(executionType, executionId);
  }

  public void pause(
      @NonNull ExecutionType executionType,
      @NonNull String executionId,
      @Nullable String pausedBy) {
    doInternal(
        () -> runner.reschedule(repository.retrieve(executionType, executionId)),
        () -> repository.pause(executionType, executionId, pausedBy),
        "pause",
        executionType,
        executionId);
  }

  public void resume(
      @NonNull ExecutionType executionType,
      @NonNull String executionId,
      @Nullable String user,
      @NonNull Boolean ignoreCurrentStatus) {
    doInternal(
        () -> runner.unpause(repository.retrieve(executionType, executionId)),
        () -> repository.resume(executionType, executionId, user, ignoreCurrentStatus),
        "resume",
        executionType,
        executionId);
  }

  private void doInternal(
      Runnable runnerAction,
      Runnable repositoryAction,
      String action,
      ExecutionType executionType,
      String executionId) {
    try {
      runWithRetries(runnerAction);
      runWithRetries(repositoryAction);
    } catch (Exception e) {
      log.error(
          "Failed to {} execution with executionType={} and executionId={}",
          action,
          executionType,
          executionId,
          e);
    }
  }

  private void runWithRetries(Runnable action) {
    retrySupport.retry(
        () -> {
          action.run();
          return true;
        },
        5,
        Duration.ofMillis(100),
        false);
  }
}
