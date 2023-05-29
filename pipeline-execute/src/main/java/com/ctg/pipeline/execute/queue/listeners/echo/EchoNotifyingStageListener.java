package com.ctg.pipeline.execute.queue.listeners.echo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ctg.pipeline.echo.api.IEchoService;
import com.ctg.pipeline.execute.model.Execution;
import com.ctg.pipeline.execute.model.ExecutionStatus;
import com.ctg.pipeline.execute.model.Stage;
import com.ctg.pipeline.execute.model.Task;
import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository;
import com.ctg.pipeline.execute.queue.listeners.Persister;
import com.ctg.pipeline.execute.queue.listeners.StageListener;
import com.ctg.pipeline.execute.util.ContextParameterProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Optional;

/**
 * Converts execution events to Echo events.
 */
@Slf4j
public class EchoNotifyingStageListener implements StageListener {

  private final IEchoService echoService;
  private final ExecutionRepository repository;
  private final ContextParameterProcessor contextParameterProcessor;

  @Autowired
  public EchoNotifyingStageListener(IEchoService echoService, ExecutionRepository repository, ContextParameterProcessor contextParameterProcessor) {
    this.echoService = echoService;
    this.repository = repository;
    this.contextParameterProcessor = contextParameterProcessor;
  }

  @Override
  public void beforeTask(Persister persister, Stage stage, Task task) {
    recordEvent("task", "starting", stage, task);
  }

  @Override
  public void beforeStage(Persister persister, Stage stage) {
    recordEvent("stage", "starting", stage);
  }

  @Override
  public void afterTask(Persister persister,
                        Stage stage,
                        Task task,
                        ExecutionStatus executionStatus,
                        boolean wasSuccessful) {
    if (executionStatus == ExecutionStatus.RUNNING) {
      return;
    }

    recordEvent("task", (wasSuccessful ? "complete" : "failed"), stage, task);
  }

  @Override
  public void afterStage(Persister persister, Stage stage) {
    // STOPPED stages are "successful" because they allow the pipeline to
    // proceed but they are still failures in terms of the stage and should
    // send failure notifications
    if (stage.getStatus() == ExecutionStatus.SKIPPED) {
      log.debug("***** $stage.execution.id Echo stage $stage.name skipped v2");
      recordEvent("stage", "skipped", stage);
    } else if (stage.getStatus() == ExecutionStatus.SUCCEEDED) {
      log.debug("***** $stage.execution.id Echo stage $stage.name complete v2");
      recordEvent("stage", "complete", stage);
    } else {
      log.debug("***** $stage.execution.id Echo stage $stage.name failed v2");
      recordEvent("stage", "failed", stage);
    }
  }

  private void recordEvent(String type, String phase, Stage stage, Task task) {
    log.info("event == stage=[{}],task=[{}]", stage);
    recordEvent(type, phase, stage, Optional.of(task));
  }

  private void recordEvent(String type, String phase, Stage stage) {
    recordEvent(type, phase, stage, Optional.empty());
  }

  private void recordEvent(String type, String phase, Stage stage, Optional<Task> maybeTask) {
    try {
      JSONObject event = new JSONObject();

      JSONObject details = new JSONObject();
      details.put("source", "orca");
      details.put("type", String.format("orca:${type}:${phase}", type, phase));
      details.put("application", String.format("orca:%s:%s", type, phase));
      event.put("details", details);

      JSONObject content = new JSONObject();
      content.put("source", "orca");
      content.put("type", String.format("orca:${type}:${phase}", type, phase));
      content.put("application", String.format("orca:%s:%s", type, phase));
      content.put("taskName", "");
      event.put("content", content);

//      def event = [
//              details: [
//                      source     : "orca",
//                      type       : "orca:${type}:${phase}".toString(),
//                      application: stage.execution.application
//              ],
//              content: [
//                      standalone : stage.execution.type == com.netflix.spinnaker.orca.pipeline.model.Execution.ExecutionType.ORCHESTRATION,
//                      canceled   : stage.execution.canceled,
//                      context    : buildContext(stage.execution, stage.context),
//                      startTime  : stage.startTime,
//                      endTime    : stage.endTime,
//                      execution  : stage.execution,
//                      executionId: stage.execution.id,
//                      isSynthetic: stage.syntheticStageOwner != null,
//                      name       : stage.name
//              ]
//      ]
//      maybeTask.ifPresent { Task task ->
//        event.content.taskName = "${stage.type}.${task.name}".toString()
//      }

      try {
//        MDC.put(Header.EXECUTION_ID.header, stage.execution.id)
//        MDC.put(Header.USER.header, stage.execution?.authentication?.user ?: "anonymous")
//        AuthenticatedRequest.allowAnonymous({
//          echoService.recordEvent(event)
//        })

        log.info("recordEvent==eventMap=[{}]", event.toJSONString());
      } finally {
//        MDC.remove(Header.EXECUTION_ID.header)
//        MDC.remove(Header.USER.header)
      }
    } catch (Exception e) {
      log.error("Failed to send ${type} event ${phase} ${stage.execution.id} ${maybeTask.map { Task task -> task.name }}", e);
    }
  }

  private Map<String, Object> buildContext(Execution execution, Map context) {
    return contextParameterProcessor.process(context,
            JSON.parseObject(JSON.toJSONString(execution), new TypeReference<Map<String, Object>>() {
            }), true
    );
  }

}
