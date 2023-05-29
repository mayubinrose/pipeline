package com.ctg.pipeline.execute.cicd.tasks;

import com.ctg.pipeline.execute.basic.task.RetryableTask;
import com.ctg.pipeline.execute.basic.task.TaskResult;
import com.ctg.pipeline.execute.model.ExecutionStatus;
import com.ctg.pipeline.execute.model.Stage;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class BuildArtifactTask implements RetryableTask {
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
      Thread.sleep(50000L);
    }catch (Exception e){}
    // 其他阶段 其他task传参
    Map<String, Object> outputs = new HashMap<>();
    outputs.put("packageNameWithoutStage","111111");
    // 本阶段内 其他task传参
    Map<String, Object> context = new HashMap<>();
    context.put("packageNameWithinStage","00000");
    if(context.containsKey("artifactTag1")){throw new RuntimeException("测试异常情况");}
    // task 日志输出 示例
    List<String> logs=new ArrayList<String>();
    logs.add("输出日志3");
    return TaskResult.builder(ExecutionStatus.SUCCEEDED).context(context).outputs(outputs).logs(logs).build();
  }
}
