package com.ctg.cicd.config.dao;

import com.ctg.cicd.config.entity.SettingsFunction;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hel
 * @since 2023-05-17
 */
@Repository
public interface SettingsFunctionDao extends BaseMapper<SettingsFunction> {

    List<SettingsFunction> getAllFunc();
}
