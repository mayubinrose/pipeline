package com.ctg.pipeline.execute.cicd.tasks;

import com.ctg.pipeline.execute.basic.task.RetryableTask;
import com.ctg.pipeline.execute.basic.task.TaskResult;
import com.ctg.pipeline.execute.model.ExecutionStatus;
import com.ctg.pipeline.execute.model.Stage;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
@Component
public class BuildCompileTask implements RetryableTask {
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
      Thread.sleep(40000L);
    }catch (Exception e){}
    List<String> logs=new ArrayList<String>();
    logs.add("输出日志2");
    return TaskResult.builder(ExecutionStatus.SUCCEEDED).logs(logs).build();
  }
}
