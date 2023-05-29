package com.ctg.pipeline.execute.util;

import com.ctg.pipeline.execute.basic.task.Task;
import com.ctg.pipeline.execute.basic.task.TaskResult;
import com.ctg.pipeline.execute.model.Stage;

import java.util.concurrent.TimeUnit;


public interface TaskExecutionInterceptor {

  default long maxTaskBackoff() {
    return TimeUnit.MINUTES.toMillis(2);
  }

  default Stage beforeTaskExecution(Task task, Stage stage) {
    return stage;
  }

  default TaskResult afterTaskExecution(Task task, Stage stage, TaskResult taskResult) {
    return taskResult;
  }
}
