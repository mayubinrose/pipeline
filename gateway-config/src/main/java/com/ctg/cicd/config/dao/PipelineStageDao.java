package com.ctg.cicd.config.dao;

import com.ctg.cicd.config.entity.pipeline.PipelineStage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 流水线阶段信息表 Mapper 接口
 * </p>
 *
 * @author 作者
 * @since 2023-05-31
 */
@Mapper
public interface PipelineStageDao extends BaseMapper<PipelineStage> {

}
