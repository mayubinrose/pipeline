package com.ctg.pipeline.execute.queue.listeners.echo;

import com.ctg.pipeline.echo.api.IEchoService;
import com.ctg.pipeline.execute.model.Execution;
import com.ctg.pipeline.execute.model.ExecutionStatus;
import com.ctg.pipeline.execute.queue.listeners.ExecutionListener;
import com.ctg.pipeline.execute.queue.listeners.Persister;
import com.ctg.pipeline.execute.util.ContextParameterProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static com.ctg.pipeline.execute.model.Execution.ExecutionType.PIPELINE;

/**
 * 阶段监听器
 *
 * @author zhiHuang
 * @date 2022/12/10 22:24
 **/
@Slf4j
public class EchoNotifyingExecutionListener implements ExecutionListener {
    private final IEchoService echoService;
    private final ObjectMapper objectMapper;
    private final ContextParameterProcessor contextParameterProcessor;

    public EchoNotifyingExecutionListener(
            IEchoService echoService,
            ObjectMapper objectMapper,
            ContextParameterProcessor contextParameterProcessor) {
        this.echoService = echoService;
        this.objectMapper = objectMapper;
        this.contextParameterProcessor = contextParameterProcessor;
    }

    @Override
    public void beforeExecution(Persister persister, Execution execution) {
        try {
            if (execution.getStatus() != ExecutionStatus.SUSPENDED) {
                processSpelInNotifications(execution);

                if (execution.getType() == PIPELINE) {
                    addApplicationNotifications(execution);
                }

                log.info("event == execution=[{}]", execution);
//                AuthenticatedRequest.allowAnonymous({
//                        echoService.recordEvent(
//                                details: [
//                source     : "orca",
//                        type       : "orca:${execution.type}:starting".toString(),
//                        application: execution.application,
//            ],
//                content: buildContent(execution)
//          )
//        });
            }
        } catch (Exception e) {
            log.error("Failed to send pipeline start event: ${execution?.id}", e);
        }
    }

    @Override
    public void afterExecution(Persister persister,
                               Execution execution,
                               ExecutionStatus executionStatus,
                               boolean wasSuccessful) {
        try {
            if (execution.getStatus() != ExecutionStatus.SUSPENDED) {
                processSpelInNotifications(execution);

                if (execution.getType() == PIPELINE) {
                    addApplicationNotifications(execution);
                }
//                AuthenticatedRequest.allowAnonymous({
//                        echoService.recordEvent(
//                                details: [
//                source     : "orca",
//                        type       : "orca:${execution.type}:${wasSuccessful ? "complete" : "failed"}".toString(),
//                        application: execution.application,
//            ],
//                content: buildContent(execution)
//          )
//        })
            }
        } catch (Exception e) {
            log.error("Failed to send pipeline end event: ${execution?.id}", e);
        }
    }

    private void processSpelInNotifications(Execution execution) {
//        List<Map<String, Object>> spelProcessedNotifications = execution.getNotifications().stream().collect(it ->{
//                contextParameterProcessor.process(it, contextParameterProcessor.buildExecutionContext(execution), true)
//        });
//
//        execution.getNotifications() = spelProcessedNotifications;
    }

    /**
     * Adds any application-level notifications to the pipeline's notifications
     * If a notification exists on both with the same address and type, the pipeline's notification will be treated as an
     * override, and any "when" values in the application-level notification that are also in the pipeline's notification
     * will be removed from the application-level notification
     *
     * @param pipeline
     */
    private void addApplicationNotifications(Execution pipeline) {
    }

    private Map<String, Object> buildContent(Execution execution) {
        return new HashMap<>();
    }

}