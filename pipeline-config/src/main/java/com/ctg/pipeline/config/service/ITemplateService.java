package com.ctg.pipeline.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ctg.pipeline.common.model.pipeline.config.StageTemplate;
import com.ctg.pipeline.config.entity.Template;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hel
 * @since 2022-11-23
 */
public interface ITemplateService extends IService<Template> {

    Map saveStageTemplate(StageTemplate stageTemplate);

    PageInfo<StageTemplate> pageQuery(String groupName, String name, String sort, int pageNum, int pageSize);

    Map deleteTemplate(Long id);

    Map getTemplateById(Long templateId);
}
