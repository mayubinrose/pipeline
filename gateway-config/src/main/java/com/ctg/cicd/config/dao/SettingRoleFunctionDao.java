package com.ctg.cicd.config.dao;

import com.ctg.cicd.config.entity.SettingRoleFunction;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author hel
 * @since 2023-05-17
 */
@Repository
public interface SettingRoleFunctionDao extends BaseMapper<SettingRoleFunction> {
    void insertList(List<SettingRoleFunction> list);

    List<Long> selectByRoleId(@Param(value = "roleId") Long roleId);

    int deleteByRoleId(@Param(value = "roleId") Long roleId);


}
