package com.ctg.cicd.config.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctg.cicd.config.entity.settings.SettingsConnection;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 作者
 * @since 2023-05-30
 */
@Mapper
public interface SettingsConnectionDao extends BaseMapper<SettingsConnection> {

}
