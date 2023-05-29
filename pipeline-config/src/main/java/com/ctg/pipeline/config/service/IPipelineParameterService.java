package com.ctg.pipeline.config.service;

import com.ctg.pipeline.config.entity.PipelineNotification;
import com.ctg.pipeline.config.entity.PipelineParameter;
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
public interface IPipelineParameterService extends IService<PipelineParameter> {

    List<PipelineParameter> getListByPipelineConfigId(Long pipelineConfigId);

    void deleteBatchByPipelineConfigId(Long pipelineConfigId);

}
