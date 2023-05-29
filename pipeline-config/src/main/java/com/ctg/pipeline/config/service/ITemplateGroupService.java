package com.ctg.pipeline.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ctg.pipeline.config.entity.TemplateGroup;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hel
 * @since 2022-11-23
 */
public interface ITemplateGroupService extends IService<TemplateGroup> {

    void saveTemplateGroup(String groupName,Long templateId);

    void deleteTemplateGroupByTemplateId(Long templateId);
}
