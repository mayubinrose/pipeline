package com.ctg.pipeline.config.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctg.eadp.common.util.StringUtils;
import com.ctg.pipeline.config.entity.Application;
import com.ctg.pipeline.config.entity.TemplateGroup;
import com.ctg.pipeline.config.dao.TemplateGroupDao;
import com.ctg.pipeline.config.service.ITemplateGroupService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hel
 * @since 2022-11-23
 */
@Service
public class TemplateGroupServiceImpl extends ServiceImpl<TemplateGroupDao, TemplateGroup> implements ITemplateGroupService {

    @Override
    public void saveTemplateGroup(String groupName,Long templateId) {
        LambdaQueryWrapper<TemplateGroup> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(TemplateGroup::getGroupName,groupName).eq(TemplateGroup::getTemplateId,templateId);
        TemplateGroup entity  = this.getOne(queryWrapper,Boolean.FALSE);
        if(entity==null){
            entity = new TemplateGroup();
            entity.setGroupName(groupName);
            entity.setTemplateId(templateId);
            this.save(entity);
        }
    }

    @Override
    public void deleteTemplateGroupByTemplateId(Long templateId) {
        LambdaQueryWrapper<TemplateGroup> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(TemplateGroup::getTemplateId,templateId);
        this.remove(queryWrapper);
    }
}
