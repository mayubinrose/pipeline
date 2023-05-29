package com.ctg.pipeline.config.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctg.pipeline.common.model.pipeline.config.StageTemplate;
import com.ctg.pipeline.config.entity.Application;
import com.ctg.pipeline.config.entity.Template;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hel
 * @since 2022-11-23
 */
@Repository
public interface TemplateDao extends BaseMapper<Template> {

    int insertTemplate(Template entity);

    List<StageTemplate> listByGroupName(@Param("groupName")String groupName,@Param("name") String name,@Param("sort") String sort,@Param("pageNum") int pageNum,@Param("pageSize") int pageSize);

    StageTemplate selectStageTemplateById(@Param("id") Long id);
}
