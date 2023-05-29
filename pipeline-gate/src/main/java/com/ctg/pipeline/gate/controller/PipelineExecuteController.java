package com.ctg.pipeline.gate.controller;


import com.ctg.cloud.paascommon.vo.Response;
import com.ctg.pipeline.common.model.execute.ExecuteTrigger;
import com.ctg.pipeline.common.model.pipeline.config.PipelineConfig;
import com.ctg.pipeline.config.service.IConfigService;
import com.ctg.pipeline.execute.api.IExecuteService;
import com.ctg.pipeline.execute.model.Execution;
import com.netflix.spinnaker.kork.web.exceptions.NotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 流水线运行相关
 *
 * @author zhiHuang
 * @date 2022/11/13 17:48
 **/
@Api(value = "流水线运行相关")
@Slf4j
@RestController
@RequestMapping("/execute")
public class PipelineExecuteController {


    @Autowired
    IExecuteService executeService;

    @Autowired
    IConfigService configService;

    @ApiOperation(value = "运行流水线")
    @PostMapping("/{application}/{pipelineNameOrId:.+}")
    Response startPipeline(@PathVariable("application") String application,
                           @PathVariable("pipelineNameOrId") String pipelineNameOrId,
                           @RequestBody(required = false) ExecuteTrigger trigger) {
        PipelineConfig pipelineConfig = configService.getPipelineConfig(application, pipelineNameOrId);
        pipelineConfig.setTriggerAndCheckParameter(trigger);
        return Response.success(executeService.startPipeline(pipelineConfig));
    }

    @ApiOperation(value = "查询运行详情")
    @GetMapping("/{executionId}")
    Execution getPipeline(@PathVariable("executionId") String executionId) {
        try {
            return executeService.getPipeline(executionId);
        } catch (Exception e) {
            if ("not".equals(e.getMessage())) {
                throw new NotFoundException("Pipeline not found (id: ${id})");
            }
            throw e;
        }
    }

    @ApiOperation(value = "取消流水线")
    @PutMapping("{executionId}/cancel")
    void cancelPipeline(@PathVariable("executionId") String executionId,
                        @RequestParam(required = false) String user,
                        @RequestParam(required = false) String reason) {
        executeService.cancelPipeline(executionId, user, reason);
    }

    @ApiOperation(value = "暂停流水线")
    @PutMapping("{executionId}/pause")
    void pausePipeline(@PathVariable("executionId") String executionId,
                       @RequestParam(required = false) String user) {
        executeService.pausePipeline(executionId, user);
    }

    @ApiOperation(value = "继续流水线")
    @PutMapping("{executionId}/resume")
    void resumePipeline(@PathVariable("executionId") String executionId,
                        @RequestParam(required = false) String user) {
        executeService.resumePipeline(executionId, user);
    }

    @ApiOperation(value = "重试流水线的某个阶段")
    @PutMapping("{executionId}/retry/{stageId}")
    Execution retryPipelineStage(@PathVariable("executionId") String executionId,
                                 @PathVariable("stageId") String stageId) {
        return executeService.retryPipelineStage(executionId, stageId);
    }

}