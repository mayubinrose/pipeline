package com.ctg.pipeline.config.service;

import com.ctg.pipeline.common.model.pipeline.config.PipelineConfig;
import com.ctg.pipeline.config.entity.Pipeline;

import java.util.Map;

/**
 * 配置业务接口
 *
 * @author zhiHuang
 * @date 2022/11/11 23:54
 **/
public interface IConfigService {

    Pipeline savePipeline(PipelineConfig pipelineConfig);

    PipelineConfig getPipelineConfig(String application,String pipelineNameOrId);

    Map deletePipeline(Long id);

    Map deleteApplication(String application);
}