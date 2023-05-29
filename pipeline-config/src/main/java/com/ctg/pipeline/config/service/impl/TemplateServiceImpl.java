package com.ctg.pipeline.config.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctg.pipeline.common.exception.BusinessException;
import com.ctg.pipeline.common.model.pipeline.config.StageTemplate;
import com.ctg.pipeline.common.util.Utils;
import com.ctg.pipeline.config.dao.TemplateDao;
import com.ctg.pipeline.config.entity.Template;
import com.ctg.pipeline.config.service.ITemplateGroupService;
import com.ctg.pipeline.config.service.ITemplateService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hel
 * @since 2022-11-23
 */
@Service
public class TemplateServiceImpl extends ServiceImpl<TemplateDao, Template> implements ITemplateService {
    @Autowired
    private ITemplateGroupService templateGroupService;
    @Autowired
    private TemplateDao templateDao;


    @Override
    public Map saveStageTemplate(StageTemplate stageTemplate) {
        Template template ;
        Date currentTime = new Date();
        if(stageTemplate.getId()!=null&&stageTemplate.getId()>0){
            template = this.getById(stageTemplate.getId());
            if(template == null){
                throw BusinessException.STAGE_TEMPLATE_EXP_NOT_FOUND.exception(stageTemplate.getName());
            }
            BeanUtil.copyProperties(stageTemplate,template);
            template.setUpdateTime(currentTime);
            this.updateById(template);
        }else{
            template = new Template();
            BeanUtil.copyProperties(stageTemplate,template);
            template.setCreateTime(currentTime);
            template.setUpdateTime(currentTime);
            this.saveTemplate(template);
        }
        this.saveOrUpdate(template);
        templateGroupService.saveTemplateGroup(stageTemplate.getGroupName(),template.getId());
        return null;
    }

    @Override
    public PageInfo<StageTemplate> pageQuery(String groupName, String name, String sort, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<StageTemplate> list = templateDao.listByGroupName(groupName, name, sort,pageNum, pageSize);
        PageInfo<StageTemplate> result = new PageInfo<>(list);
        return result;
    }

    @Override
    public Map deleteTemplate(Long id) {
        Template template = this.getById(id);
        if(template==null){
            throw BusinessException.STAGE_TEMPLATE_EXP_NOT_FOUND.exception(id);
        }
        this.removeById(template);
        templateGroupService.deleteTemplateGroupByTemplateId(id);
        return null;
    }

    @Override
    public Map getTemplateById(Long templateId) {
        Map result = new HashMap<>();
        StageTemplate stageTemplate = getStageTemplateById(templateId);
        if(stageTemplate!=null){
            result = Utils.objectToMap(stageTemplate);
        }
        return result;
    }

    private StageTemplate getStageTemplateById(Long templateId) {
        return  templateDao.selectStageTemplateById(templateId);
    }

    private void saveTemplate(Template template) {
        templateDao.insertTemplate(template);
    }


}
