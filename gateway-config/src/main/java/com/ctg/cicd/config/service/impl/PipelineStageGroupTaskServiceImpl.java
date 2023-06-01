package com.ctg.cicd.config.service.impl;

import com.ctg.cicd.config.entity.pipeline.PipelineStageGroupTask;
import com.ctg.cicd.config.dao.PipelineStageGroupTaskDao;
import com.ctg.cicd.config.service.IPipelineStageGroupTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 流水线阶段信息表 服务实现类
 * </p>
 *
 * @author 作者
 * @since 2023-05-31
 */
@Service
public class PipelineStageGroupTaskServiceImpl extends ServiceImpl<PipelineStageGroupTaskDao, PipelineStageGroupTask> implements IPipelineStageGroupTaskService {

}
