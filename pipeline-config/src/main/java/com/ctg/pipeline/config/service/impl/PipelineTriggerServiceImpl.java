package com.ctg.pipeline.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ctg.pipeline.config.entity.PipelineTrigger;
import com.ctg.pipeline.config.dao.PipelineTriggerDao;
import com.ctg.pipeline.config.entity.Stage;
import com.ctg.pipeline.config.service.IPipelineTriggerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hel
 * @since 2022-11-14
 */
@Service
public class PipelineTriggerServiceImpl extends ServiceImpl<PipelineTriggerDao, PipelineTrigger> implements IPipelineTriggerService {

    @Autowired
    private PipelineTriggerDao pipelineTriggerDao;

    @Override
    public List<PipelineTrigger> getListByPipelineConfigId(Long pipelineConfigId) {
        LambdaQueryWrapper<PipelineTrigger> triggerWrapper = Wrappers.lambdaQuery();
        triggerWrapper.eq(PipelineTrigger::getPipelineConfigId,pipelineConfigId);
        return this.list(triggerWrapper);
    }

    @Override
    public void deleteBatchByPipelineConfigId(Long pipelineConfigId) {
        LambdaQueryWrapper<PipelineTrigger> triggerWrapper = Wrappers.lambdaQuery();
        triggerWrapper.eq(PipelineTrigger::getPipelineConfigId,pipelineConfigId);
        this.remove(triggerWrapper);
    }
}
