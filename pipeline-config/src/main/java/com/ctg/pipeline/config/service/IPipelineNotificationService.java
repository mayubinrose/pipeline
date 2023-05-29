package com.ctg.pipeline.config.service;

import com.ctg.pipeline.config.entity.PipelineNotification;
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
public interface IPipelineNotificationService extends IService<PipelineNotification> {

    List<PipelineNotification> getListByPipelineConfigId(Long pipelineConfigId);

    void deleteBatchByPipelineConfigId(Long pipelineConfigId);
}
