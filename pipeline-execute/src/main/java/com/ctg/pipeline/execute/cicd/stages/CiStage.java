package com.ctg.pipeline.execute.cicd.stages;

import com.ctg.pipeline.execute.cicd.tasks.CiBrakerImageTask;
import com.ctg.pipeline.execute.cicd.tasks.CiUploadImageTask;
import com.ctg.pipeline.execute.model.Stage;
import com.ctg.pipeline.execute.queue.model.Trigger;
import com.ctg.pipeline.execute.util.StageDefinitionBuilder;
import com.ctg.pipeline.execute.util.TaskNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@Slf4j
public class CiStage implements StageDefinitionBuilder {

  @Override
  public void taskGraph(Stage stage, TaskNode.Builder builder) {

      // 上一阶段赋值内容获取
      Map<String,Object> context=stage.getExecution().getContext();
      log.info("Execution.context=[{}]",context.toString());

      builder
        .withTask("Braker image", CiBrakerImageTask.class)
        .withTask("Upload image", CiUploadImageTask.class);
  }
}
