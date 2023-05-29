package com.ctg.pipeline.gate.controller;

import com.ctg.pipeline.common.cache.redis.RedisUtil;
import com.ctg.pipeline.common.model.pipeline.config.PipelineConfig;
import com.ctg.pipeline.config.entity.Pipeline;
import com.ctg.pipeline.config.service.IApplicationService;
import com.ctg.pipeline.config.service.IConfigService;
import com.ctg.pipeline.gate.service.ITaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 流水线控制器
 *
 * @author zhiHuang
 * @date 2022/11/12 10:56
 **/
@Api(value = "流水线配置相关")
@Slf4j
@RestController
@RequestMapping("/config")
public class PipelineConfigController {


    @Autowired
    IConfigService configService;

    @Autowired
    IApplicationService applicationService;


    @Autowired
    ITaskService taskService;

    @ApiOperation(value = "Save application and pipeline")
    @PostMapping("/createPipeline")
    Pipeline saveApplicationAndPipeline(@RequestBody PipelineConfig config) {
//        Pipeline pipeline = new Pipeline();
//        for (int i = 0; i < 4000; i++) {
//            config.setName("pipeline" + i);
//            pipeline = configService.savePipeline(config);
//        }
//        return pipeline;
        return configService.savePipeline(config);
    }

    @ApiOperation(value = "get pipeline config")
    @GetMapping("/{application}/{pipelineName}")
    PipelineConfig getPipelineConfig(@PathVariable("application") String application,@PathVariable("pipelineName") String pipelineName){
        PipelineConfig pipelineConfig = configService.getPipelineConfig(application,pipelineName);
        return pipelineConfig;
    }

     @ApiOperation(value = "delete pipeline")
    @PostMapping("/deletePipeline")
     Map deletePipeline(@RequestParam(value = "id") Long id) {
        return configService.deletePipeline(id);
    }

    @ApiOperation(value = "delete application and pipeline")
    @PostMapping("/deleteApplication")
    Map deleteApplication(@RequestParam(value = "application") String application) {
        return configService.deleteApplication(application);
    }



}