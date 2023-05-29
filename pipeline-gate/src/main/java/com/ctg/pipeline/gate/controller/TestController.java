package com.ctg.pipeline.gate.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ctg.pipeline.common.model.execute.ExecuteTrigger;
import com.ctg.pipeline.common.model.pipeline.config.PipelineConfig;
import com.ctg.pipeline.config.service.IConfigService;
import com.ctg.pipeline.execute.api.IExecuteService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 流水线运行相关
 *
 * @author zhiHuang
 * @date 2022/11/13 17:48
 **/
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {


    @Autowired
    IExecuteService executeService;

    @Autowired
    IConfigService configService;

    @ApiOperation(value = "Trigger a pipeline execution")
    @GetMapping("/{name}")
    Map invokePipelineConfig(@PathVariable String name) {
        Map result=new HashMap();
        result.put("name",name);
        return  result;
    }



}