package com.ctg.pipeline.execute.cicd.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.ctg.pipeline.execute.basic.task.RetryableTask;
import com.ctg.pipeline.execute.basic.task.TaskResult;
import com.ctg.pipeline.execute.model.ExecutionStatus;
import com.ctg.pipeline.execute.model.Stage;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;


@Component
public class CiBrakerImageTask implements RetryableTask {
  @Override
  public long getBackoffPeriod() {
    return TimeUnit.SECONDS.toMillis(3);
  }

  @Override
  public long getTimeout() {
    return TimeUnit.SECONDS.toMillis(60);
  }

  @NotNull
  @Override
  public TaskResult execute(@NotNull Stage stage) {
    try {
      Thread.sleep(20000L);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    Map<String, Object> outputs = new HashMap<>();
    outputs.put("Braker image==year","Command:docker build xxxxx complete!==year");
    return TaskResult.builder(ExecutionStatus.SUCCEEDED).outputs(outputs).build();
  }
}
