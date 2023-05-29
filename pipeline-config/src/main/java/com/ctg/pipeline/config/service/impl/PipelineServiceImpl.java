package com.ctg.pipeline.config.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ctg.cloud.paascommon.json.JSONObject;
import com.ctg.pipeline.common.exception.BusinessException;
import com.ctg.pipeline.common.util.CollectionUtils;
import com.ctg.pipeline.config.dao.ApplicationDao;
import com.ctg.pipeline.config.entity.*;
import com.ctg.pipeline.config.dao.PipelineDao;
import com.ctg.pipeline.config.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class PipelineServiceImpl extends ServiceImpl<PipelineDao, Pipeline> implements IPipelineService {
    @Autowired
    private PipelineDao pipelineDao;
    @Autowired
    private IStageService stageService;
    @Autowired
    private IPipelineTriggerService pipelineTriggerService;
    @Autowired
    private IPipelineParameterService pipelineParameterService;
    @Autowired
    private IPipelineNotificationService pipelineNotificationService;

    @Override
    public int savePipeline(Pipeline pipeline) {
        return pipelineDao.insertPipeline(pipeline);
    }

    @Transactional
    @Override
    public void deletePipeLineById(Long id) {
        Pipeline pipeline = this.getById(id);
        if(pipeline==null){
            throw BusinessException.PIPEILINE_EXP_NOT_FOUND.exception(id);
        }
        //1 删除Stages
        stageService.deleteBatchByPipelineConfigId(pipeline.getId());
        //2 删除Trigger
        pipelineTriggerService.deleteBatchByPipelineConfigId(pipeline.getId());
        //3 删除Parameter
        pipelineParameterService.deleteBatchByPipelineConfigId(pipeline.getId());
        //4 删除Notifications
        pipelineNotificationService.deleteBatchByPipelineConfigId(pipeline.getId());
        //5 删除Pipeline
        this.deletePipeLineById(id);
    }

    @Override
    public List<Pipeline> getPipelineListByApplication(String application) {

        LambdaQueryWrapper<Pipeline> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Pipeline::getApplication,application);
        List<Pipeline> pipelineList  = this.list(queryWrapper);
        return pipelineList;
    }
}
