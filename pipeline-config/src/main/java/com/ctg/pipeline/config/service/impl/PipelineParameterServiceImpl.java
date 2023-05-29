package com.ctg.pipeline.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ctg.pipeline.config.entity.PipelineParameter;
import com.ctg.pipeline.config.dao.PipelineParameterDao;
import com.ctg.pipeline.config.entity.PipelineTrigger;
import com.ctg.pipeline.config.service.IPipelineParameterService;
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
public class PipelineParameterServiceImpl extends ServiceImpl<PipelineParameterDao, PipelineParameter> implements IPipelineParameterService {
    @Autowired
    private PipelineParameterDao pipelineParameterDao;

    @Override
    public List<PipelineParameter> getListByPipelineConfigId(Long pipelineConfigId) {
        LambdaQueryWrapper<PipelineParameter> parameterWrapper = Wrappers.lambdaQuery();
        parameterWrapper.eq(PipelineParameter::getPipelineConfigId,pipelineConfigId);
        return this.list(parameterWrapper);
    }

    @Override
    public void deleteBatchByPipelineConfigId(Long pipelineConfigId) {
        LambdaQueryWrapper<PipelineParameter> parameterWrapper = Wrappers.lambdaQuery();
        parameterWrapper.eq(PipelineParameter::getPipelineConfigId,pipelineConfigId);
        this.remove(parameterWrapper);
    }
}
