package com.ctg.pipeline.config.service.impl;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ctg.eadp.common.util.StringUtils;
import com.ctg.pipeline.common.model.pipeline.config.ApplicationConfig;
import com.ctg.pipeline.config.entity.Application;
import com.ctg.pipeline.config.dao.ApplicationDao;
import com.ctg.pipeline.config.entity.PipelineNotification;
import com.ctg.pipeline.config.service.IApplicationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 应用表 服务实现类
 * </p>
 *
 * @author hel
 * @since 2022-11-14
 */
@Service
public class ApplicationServiceImpl extends ServiceImpl<ApplicationDao, Application> implements IApplicationService {
    @Autowired
    private ApplicationDao applicationDao;

    @Override
    public ApplicationConfig saveApplication(ApplicationConfig config) {
        LambdaQueryWrapper<Application> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Application::getApplication,config.getApplication());
        Application entity  = this.getOne(queryWrapper,Boolean.FALSE);
        Date currentTime = new Date();
        if(entity!=null){
            entity.setApplication(config.getApplication());
            entity.setUpdateTime(currentTime);
            entity.setUpdateBy(config.getUpdateBy());
            if(StringUtils.isNotBlank(config.getDescription())){
                entity.setDescription(config.getDescription());
            }
            applicationDao.updateById(entity);
            BeanUtil.copyProperties(entity,config);
            return config;
        }else{
            entity = new Application();
            entity.setApplication(config.getApplication());
            if(StringUtils.isNotBlank(config.getDescription())){
                entity.setDescription(config.getDescription());
            }
            entity.setDeleted(Boolean.FALSE);
            entity.setCreateTime(currentTime);
            entity.setUpdateTime(currentTime);
            entity.setCreateBy(config.getUpdateBy());
            entity.setUpdateBy(config.getUpdateBy());
            applicationDao.insertApplication(entity);
        }
        BeanUtil.copyProperties(entity,config);
        return config;
    }

    @Override
    public Application getApplication(String name) {
        LambdaQueryWrapper<Application> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Application::getApplication,name);
        Application entity  = this.getOne(queryWrapper,Boolean.FALSE);
        return entity;
    }

}
