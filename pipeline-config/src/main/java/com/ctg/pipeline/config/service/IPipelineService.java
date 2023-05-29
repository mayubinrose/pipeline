package com.ctg.pipeline.config.service;

import com.ctg.pipeline.config.entity.Pipeline;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hel
 * @since 2022-11-14
 */
public interface IPipelineService extends IService<Pipeline> {

    int savePipeline(Pipeline pipeline);

    void deletePipeLineById(Long id);

    List<Pipeline> getPipelineListByApplication(String application);
}
