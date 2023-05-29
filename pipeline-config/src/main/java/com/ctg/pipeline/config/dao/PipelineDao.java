package com.ctg.pipeline.config.dao;

import com.ctg.pipeline.config.entity.Pipeline;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hel
 * @since 2022-11-14
 */
@Repository
public interface PipelineDao extends BaseMapper<Pipeline> {

    int insertPipeline(Pipeline pipeline);
}
