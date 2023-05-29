package com.ctg.cicd.config.service;

import com.ctg.cicd.common.model.dto.SettingsRoleDTO;
import com.ctg.cicd.common.model.vo.SettingsRoleAddVo;
import com.ctg.cicd.common.model.vo.SettingsRoleUpdateVo;
import com.ctg.cicd.config.entity.SettingsRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hel
 * @since 2023-05-17
 */
public interface ISettingsRoleService extends IService<SettingsRole> {

    boolean createRole(SettingsRoleAddVo settingsRoleAddVo, String userName);

    boolean deleteRole(Long id);

    List<SettingsRole> listByNodeRootId(Long nodeRootId);

    PageInfo<SettingsRoleDTO> listByCondition(Long nodeRootId, String roleName, Integer roleStatus, int pageNum, int pageSize);

    boolean updateRole(String userName, SettingsRoleUpdateVo settingsRollUpdateVo);
}
