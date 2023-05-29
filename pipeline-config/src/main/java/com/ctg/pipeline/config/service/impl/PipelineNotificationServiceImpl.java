package com.ctg.pipeline.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ctg.pipeline.config.entity.PipelineNotification;
import com.ctg.pipeline.config.dao.PipelineNotificationDao;
import com.ctg.pipeline.config.entity.PipelineParameter;
import com.ctg.pipeline.config.service.IPipelineNotificationService;
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
public class PipelineNotificationServiceImpl extends ServiceImpl<PipelineNotificationDao, PipelineNotification> implements IPipelineNotificationService {
    @Autowired
    private PipelineNotificationDao pipelineNotificationDao;

    @Override
    public List<PipelineNotification> getListByPipelineConfigId(Long pipelineConfigId) {
        LambdaQueryWrapper<PipelineNotification> notificationWrapper = Wrappers.lambdaQuery();
        notificationWrapper.eq(PipelineNotification::getPipelineConfigId,pipelineConfigId);
        return this.list(notificationWrapper);
    }

    @Override
    public void deleteBatchByPipelineConfigId(Long pipelineConfigId) {
        LambdaQueryWrapper<PipelineNotification> notificationWrapper = Wrappers.lambdaQuery();
        notificationWrapper.eq(PipelineNotification::getPipelineConfigId,pipelineConfigId);
        this.remove(notificationWrapper);
    }
}
