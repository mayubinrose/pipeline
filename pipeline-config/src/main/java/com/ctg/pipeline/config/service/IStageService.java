package com.ctg.pipeline.config.service;

import com.ctg.pipeline.config.entity.Stage;
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
public interface IStageService extends IService<Stage> {

    List<Stage> getListByPipelineConfigId(Long pipelineConfigId);

    void deleteBatchByPipelineConfigId(Long pipelineConfigId);

}
