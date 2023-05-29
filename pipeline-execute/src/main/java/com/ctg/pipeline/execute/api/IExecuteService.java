package com.ctg.pipeline.execute.api;

import com.ctg.pipeline.common.model.pipeline.config.PipelineConfig;
import com.ctg.pipeline.execute.model.Execution;

import java.util.Map;

/**
 * @author zhiHuang
 * @date 2022/11/12 16:56
 **/
public interface IExecuteService {


    Map getTask(String id);


    String startPipeline(PipelineConfig pipelineConfig);

    Execution getPipeline(String executionId);

    void cancelPipeline(String executionId, String user, String reason);

    void pausePipeline(String executionId, String user);

    void resumePipeline(String executionId, String user);

    Execution retryPipelineStage(String executionId, String stageId);

}