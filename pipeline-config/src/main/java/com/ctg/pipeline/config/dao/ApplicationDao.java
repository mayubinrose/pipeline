package com.ctg.pipeline.config.dao;

import com.ctg.pipeline.config.entity.Application;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 应用表 Mapper 接口
 * </p>
 *
 * @author hel
 * @since 2022-11-14
 */
@Repository
public interface ApplicationDao extends BaseMapper<Application> {

    int insertApplication(Application entity);

}
