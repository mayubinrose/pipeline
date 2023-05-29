package com.ctg.pipeline.execute.cicd.stages;

import com.ctg.pipeline.execute.cicd.tasks.BuildArtifactTask;
import com.ctg.pipeline.execute.cicd.tasks.BuildCloneCodeTask;
import com.ctg.pipeline.execute.cicd.tasks.BuildCompileTask;
import com.ctg.pipeline.execute.model.Stage;
import com.ctg.pipeline.execute.queue.model.Trigger;
import com.ctg.pipeline.execute.util.StageDefinitionBuilder;
import com.ctg.pipeline.execute.util.TaskNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@Slf4j
public class BuildStage implements StageDefinitionBuilder {
@Autowired
ObjectMapper objectMapper;
  @Override
  public void taskGraph(Stage stage, TaskNode.Builder builder) {
      // stage配置参数获取 示例：
      Map<String,Object> context=stage.getContext();
      try {
          log.info("Stage.context=[{}]",objectMapper.writeValueAsString(context));
      }catch (Exception e){}
      // 流水线运行参数获取 示例：
      Trigger trigger=stage.getExecution().getTrigger();
      log.info("Execution.trigger.parameters=[{}]",trigger.getParameters().toString());
      builder
        .withTask("拉取代码", BuildCloneCodeTask.class)
        .withTask("编译代码", BuildCompileTask.class)
        .withTask("匹配制品", BuildArtifactTask.class);
  }
}
