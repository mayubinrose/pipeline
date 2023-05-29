package com.ctg.pipeline.config.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ctg.cloud.paascommon.json.JSONObject;
import com.ctg.pipeline.common.base.Constant;
import com.ctg.pipeline.common.cache.redis.RedisUtil;
import com.ctg.pipeline.common.exception.BusinessException;
import com.ctg.pipeline.common.model.pipeline.config.*;
import com.ctg.pipeline.common.util.CollectionUtils;
import com.ctg.pipeline.config.entity.*;
import com.ctg.pipeline.config.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 配置业务实现类
 *
 * @author zhiHuang
 * @date 2022/11/11 23:55
 **/
@Component("configService")
public class ConfigServiceImpl implements IConfigService {

    @Autowired
    private IApplicationService applicationService;
    @Autowired
    private IPipelineService pipelineService;
    @Autowired
    private IStageService stageService;
    @Autowired
    private IPipelineTriggerService pipelineTriggerService;
    @Autowired
    private IPipelineParameterService pipelineParameterService;
    @Autowired
    private IPipelineNotificationService pipelineNotificationService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Transactional
    public Pipeline savePipeline(PipelineConfig pipelineConfig) {
        // ---toDo 注意冪等
        //1、保存application
        ApplicationConfig applicationConfig = new ApplicationConfig(pipelineConfig.getApplication(), pipelineConfig.getDescription(), pipelineConfig.getCreateBy());
        applicationService.saveApplication(applicationConfig);
        Date currentTime = new Date();


        //2、保存Pipeline
        Pipeline pipeline ;
        if(pipelineConfig.getId()!=null&&pipelineConfig.getId()>0){
            pipeline = pipelineService.getById(pipelineConfig.getId());
            if(pipeline == null){
                throw BusinessException.PIPEILINE_EXP_NOT_FOUND.exception(pipelineConfig.getName());
            }
            if(!pipeline.getApplication().equals(pipelineConfig.getApplication())){
                throw BusinessException.PIPEILINE_EXP_APPLICATION_NOT_MATCH.exception(pipelineConfig.getName());
            }
            pipeline.setName(pipelineConfig.getName());
            pipeline.setDescription(pipelineConfig.getDescription());
            pipeline.setUpdateBy(pipelineConfig.getUpdateBy());
            pipeline.setUpdateTime(currentTime);
            pipelineService.updateById(pipeline);
        }else{
            List<Pipeline> existPipelines = pipelineService.getPipelineListByApplication(pipelineConfig.getApplication());
            if(CollectionUtils.isNotEmpty(existPipelines)){
                throw BusinessException.PIPEILINE_EXP_ALREADY_EXIST.exception(pipelineConfig.getName());
            }
            pipeline = new Pipeline();
            pipeline.setApplication(pipelineConfig.getApplication());
            pipeline.setName(pipelineConfig.getName());
            pipeline.setDescription(pipelineConfig.getDescription());
            pipeline.setCreateBy(pipelineConfig.getCreateBy());
            pipeline.setUpdateBy(pipelineConfig.getUpdateBy());
            pipeline.setCreateTime(currentTime);
            pipeline.setUpdateTime(currentTime);
            pipelineService.savePipeline(pipeline);
        }


        Long pipelineId = pipeline.getId();
        //3、保存相关config
        //3.1 保存保存Stages
        if(CollectionUtils.isNotEmpty(pipelineConfig.getStages())){
            stageService.deleteBatchByPipelineConfigId(pipelineId);
            List<Stage> stageList = JSONObject.parseArray(JSONObject.toJSONString(pipelineConfig.getStages()), Stage.class);
            stageList.forEach(stag -> stag.setPipelineConfigId(pipelineId));
            stageService.saveBatch(stageList);
        }
        //3.2 保存Trigger
        if(CollectionUtils.isNotEmpty(pipelineConfig.getTriggers())){
            pipelineTriggerService.deleteBatchByPipelineConfigId(pipelineId);
            List<PipelineTrigger> triggerList = JSONObject.parseArray(JSONObject.toJSONString(pipelineConfig.getTriggers()), PipelineTrigger.class);
            triggerList.forEach(trigger -> trigger.setPipelineConfigId(pipelineId));
            pipelineTriggerService.saveBatch(triggerList);
        }
        //3.3 保存Parameter
        if(CollectionUtils.isNotEmpty(pipelineConfig.getParameterConfig())){
            pipelineParameterService.deleteBatchByPipelineConfigId(pipelineId);
            List<PipelineParameter> parameterList = JSONObject.parseArray(JSONObject.toJSONString(pipelineConfig.getParameterConfig()), PipelineParameter.class);
            parameterList.forEach(parameter -> parameter.setPipelineConfigId(pipelineId));
            pipelineParameterService.saveBatch(parameterList);
        }
        //3.4 保存Notifications
        if(CollectionUtils.isNotEmpty(pipelineConfig.getNotifications())){
            pipelineNotificationService.deleteBatchByPipelineConfigId(pipelineId);
            pipelineConfig.getNotifications().forEach(notifyConf -> notifyConf.setOccasion(String.join(",", notifyConf.getWhen())));
            List<PipelineNotification> notificationList =  BeanUtil.copyToList(pipelineConfig.getNotifications(),PipelineNotification.class);
            //List<PipelineNotification> notificationList = JSONObject.parseArray(JSONObject.toJSONString(test), PipelineNotification.class);
            notificationList.forEach(notification -> notification.setPipelineConfigId(pipelineId));
            pipelineNotificationService.saveBatch(notificationList);
        }
        //4:清空缓存信息
        String cacheKey = Constant.PIPE_LINE_CACHE_PREFIX+pipelineConfig.getApplication()+"_"+pipelineConfig.getName();
        redisUtil.delete(cacheKey);
        return pipeline;
    }


    @Override
    public PipelineConfig getPipelineConfig(String application, String pipelineNameOrId) {

        String cacheKey = Constant.PIPE_LINE_CACHE_PREFIX+application+"_"+pipelineNameOrId;
        // 1、从缓存中获取
        Object cacheObj = redisUtil.getObject(cacheKey);
        if(cacheObj!=null){
            return  (PipelineConfig) cacheObj;
        }
        //2、缓存没有 查数据库
        PipelineConfig pipelineConfig = selectPipelineConfig(application, pipelineNameOrId);
        if(pipelineConfig!=null){
            //3、更新缓存
            redisUtil.setObject(cacheKey,pipelineConfig);
        }
        return pipelineConfig;
    }

    @Override
    public Map deletePipeline(Long id) {
        pipelineService.deletePipeLineById(id);
        return null;
    }

    @Override
    public Map deleteApplication(String name) {
        Application application = applicationService.getApplication(name);
        if(application==null){
            throw BusinessException.APPLICATION_EXP_NOT_FOUND.exception(name);
        }
        List<Pipeline> pipelineList = pipelineService.getPipelineListByApplication(name);
        if(CollectionUtils.isNotEmpty(pipelineList)){
            for(Pipeline pipeline:pipelineList){
                deletePipeline(pipeline.getId());
            }
        }
        applicationService.removeById(application);
        return null;
    }

    private PipelineConfig selectPipelineConfig(String application, String pipelineNameOrId) {
        LambdaQueryWrapper<Pipeline> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Pipeline::getApplication,application);
        if(StringUtils.isNumeric(pipelineNameOrId)){
            queryWrapper.and(wrapper -> wrapper.eq(Pipeline::getName, pipelineNameOrId)
                    .or().eq(Pipeline::getId, pipelineNameOrId));
        }else{
            queryWrapper.eq(Pipeline::getName, pipelineNameOrId);
        }
        Pipeline pipeline  = pipelineService.getOne(queryWrapper,Boolean.FALSE);
        if(pipeline==null){
            return null;
        }
        PipelineConfig config = new PipelineConfig();
        BeanUtil.copyProperties(pipeline,config);

        List<Stage> stageList = stageService.getListByPipelineConfigId(pipeline.getId());
        if(CollectionUtils.isNotEmpty(stageList)){
            List<StageConfig> stageConfigList = JSONObject.parseArray(JSONObject.toJSONString(stageList), StageConfig.class);
            config.setStages(stageConfigList);
        }

        List<PipelineTrigger> triggerList = pipelineTriggerService.getListByPipelineConfigId(pipeline.getId());
        if(CollectionUtils.isNotEmpty(triggerList)){
            List<TriggerConfig> triggerConfigList = JSONObject.parseArray(JSONObject.toJSONString(triggerList), TriggerConfig.class);
            config.setTriggers(triggerConfigList);
        }

        List<PipelineParameter> parameterList = pipelineParameterService.getListByPipelineConfigId(pipeline.getId());
        if(CollectionUtils.isNotEmpty(parameterList)){
            List<ParameterConfig> parameterConfigList = JSONObject.parseArray(JSONObject.toJSONString(parameterList), ParameterConfig.class);
            config.setParameterConfig(parameterConfigList);
        }

        List<PipelineNotification> notificationList = pipelineNotificationService.getListByPipelineConfigId(pipeline.getId());
        if(CollectionUtils.isNotEmpty(notificationList)){
            List<NotificationConfig> notificationConfigList = JSONObject.parseArray(JSONObject.toJSONString(notificationList), NotificationConfig.class);
            notificationConfigList.forEach(notification -> notification.setWhen(notification.getOccasion().split(",")));
            config.setNotifications(notificationConfigList);

        }

        return config;
    }
}