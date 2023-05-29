package com.ctg.pipeline.execute.queue.listeners;

import com.ctg.pipeline.execute.model.Execution;
import javax.annotation.Nonnull;

import com.ctg.pipeline.execute.model.ExecutionStatus;
import org.springframework.core.Ordered;

public interface ExecutionListener extends Ordered, Comparable<ExecutionListener> {
  default void beforeInitialPersist(@Nonnull Execution execution) {
    // do nothing
  }

  default void beforeExecution(@Nonnull Persister persister, @Nonnull Execution execution) {
    // do nothing
  }

  default void afterExecution(
      @Nonnull Persister persister,
      @Nonnull Execution execution,
      @Nonnull ExecutionStatus executionStatus,
      boolean wasSuccessful) {
    // do nothing
  }

  default int getOrder() {
    return 0;
  }

  @Override
  default int compareTo(@Nonnull ExecutionListener o) {
    return o.getOrder() - getOrder();
  }
}
