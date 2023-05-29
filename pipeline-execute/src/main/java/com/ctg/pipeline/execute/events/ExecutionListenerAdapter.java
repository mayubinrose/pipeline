package com.ctg.pipeline.execute.events;

import com.ctg.pipeline.execute.model.Execution;
import com.ctg.pipeline.execute.model.ExecutionStatus;
import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository;
import com.ctg.pipeline.execute.queue.listeners.DefaultPersister;
import com.ctg.pipeline.execute.queue.listeners.ExecutionListener;
import com.ctg.pipeline.execute.queue.listeners.Persister;
import org.slf4j.MDC;
import org.springframework.context.ApplicationListener;

import com.netflix.spinnaker.kork.common.Header;
public final class ExecutionListenerAdapter implements ApplicationListener<ExecutionEvent> {

  private final ExecutionListener delegate;
  private final ExecutionRepository repository;
  private final Persister persister;

  public ExecutionListenerAdapter(ExecutionListener delegate, ExecutionRepository repository) {
    this.delegate = delegate;
    this.repository = repository;
    persister = new DefaultPersister(repository);
  }

  @Override
  public void onApplicationEvent(ExecutionEvent event) {
    try {
      MDC.put(Header.EXECUTION_ID.getHeader(), event.getExecutionId());
      if (event instanceof ExecutionStarted) {
        onExecutionStarted((ExecutionStarted) event);
      } else if (event instanceof ExecutionComplete) {
        onExecutionComplete((ExecutionComplete) event);
      }
    } finally {
      MDC.remove(Header.EXECUTION_ID.getHeader());
    }
  }

  private void onExecutionStarted(ExecutionStarted event) {
    delegate.beforeExecution(persister, executionFor(event));
  }

  private void onExecutionComplete(ExecutionComplete event) {
    ExecutionStatus status = event.getStatus();
    delegate.afterExecution(persister, executionFor(event), status, status.isSuccessful());
  }

  private Execution executionFor(ExecutionEvent event) {
    return repository.retrieve(event.getExecutionType(), event.getExecutionId());
  }
}
