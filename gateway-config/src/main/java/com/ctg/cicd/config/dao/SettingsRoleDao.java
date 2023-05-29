package com.ctg.cicd.config.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctg.cicd.common.model.dto.SettingsRoleDTO;
import com.ctg.cicd.config.entity.SettingsRole;
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
public interface SettingsRoleDao extends BaseMapper<SettingsRole> {

    int insertSettingsRole(SettingsRole insert);

    SettingsRole getRoleByName(String roleName, Long nodeRootId);

    int updateById(SettingsRole update);

    List<SettingsRoleDTO> listByCondition(@Param(value = "nodeRootId") Long nodeRootId,
                                          @Param(value = "roleName") String roleName,
                                          @Param(value = "roleStatus") Integer roleStatus);
}
