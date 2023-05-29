package com.ctg.cicd.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctg.cicd.common.exception.BusinessException;
import com.ctg.cicd.common.model.dto.SettingsRoleDTO;
import com.ctg.cicd.common.model.vo.SettingsRoleAddVo;
import com.ctg.cicd.common.model.vo.SettingsRoleUpdateVo;
import com.ctg.cicd.config.dao.SettingsRoleDao;
import com.ctg.cicd.config.entity.SettingsRole;
import com.ctg.cicd.config.service.INodeInfoService;
import com.ctg.cicd.config.service.ISettingRoleFunctionService;
import com.ctg.cicd.config.service.ISettingsRoleService;
import com.ctg.cloud.paascommon.utils.SecurityUtils;
import com.ctg.eadp.common.util.PageUtils;
import com.ctg.eadp.common.util.UuidUtils;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hel
 * @since 2023-05-17
 */
@Service
@Slf4j
public class SettingsRoleService extends ServiceImpl<SettingsRoleDao, SettingsRole> implements ISettingsRoleService {
    @Autowired
    private SettingsRoleDao settingsRoleDao;
    @Autowired
    private INodeInfoService iNodeInfoService;
    @Autowired
    private ISettingRoleFunctionService iSettingRoleFunctionService;

    @Override
//    @CacheEvict(value = "list", key = "#settingsRoleAddVo.nodeRootId") //dev、test环境目前redis挂了，暂时注解掉
    public boolean createRole(SettingsRoleAddVo settingsRoleAddVo, String userName) {
        // 如果租户的id获取不到 直接将根节点定义为1
        Long tenantId = SecurityUtils.getCurrentTenantId()==null?10081:SecurityUtils.getCurrentTenantId();
        Long nodeRootId = iNodeInfoService.getNodeRootId(tenantId);
        settingsRoleAddVo.setNodeRootId(nodeRootId);
        // 先根据节点id以及rolename 判断是否已经有存在的角色名字
        SettingsRole role = settingsRoleDao.getRoleByName(settingsRoleAddVo.getRoleName(), settingsRoleAddVo.getNodeRootId());
        if (role != null) {
            throw BusinessException.EXIST_ROLE_NAME_NOT_ALLOW.exception();
        }
        SettingsRole insert = new SettingsRole();
        BeanUtils.copyProperties(settingsRoleAddVo, insert);
        Date now = new Date();
        insert.setCreateBy(userName);
        insert.setCreateTime(now);
        insert.setUpdateTime(now);
        insert.setUpdateBy(userName);
        insert.setRoleUuid(UuidUtils.generateUuid());
        settingsRoleDao.insert(insert);
        return true;
    }

    @Override
    public boolean deleteRole(Long id) {
        // 如果已经不在数据库 不能删除了
        //todo 删除角色需要判断这个角色下面是否还有绑定的用户
        SettingsRole settingsRole = settingsRoleDao.selectById(id);
        if (settingsRole == null) {
            throw BusinessException.NOT_EXIST_ROLE_DELETE_NOT_ALLOW.exception();
        }
        settingsRoleDao.deleteById(id);
        return true;
    }


    @Override
//    @CacheEvict(value = "list", key = "#settingsRollUpdateVo.nodeRootId")
    public boolean updateRole(String userName, SettingsRoleUpdateVo settingsRollUpdateVo) {
        Long tenantId = SecurityUtils.getCurrentTenantId()==null?10081:SecurityUtils.getCurrentTenantId();
        Long nodeRootId = iNodeInfoService.getNodeRootId(tenantId);

        settingsRollUpdateVo.setNodeRootId(nodeRootId);
        SettingsRole update = new SettingsRole();
        Date now = new Date();
        update.setUpdateTime(now);
        update.setUpdateBy(userName);
        update.setId(settingsRollUpdateVo.getRoleId());
        BeanUtils.copyProperties(settingsRollUpdateVo, update);
        int res = settingsRoleDao.updateById(update);
        if (res != 1) {
            throw BusinessException.ROLE_UPDATE_FAIL.exception();
        }
        return true;
    }

    @Override
//    @Cacheable(value = "list", key = "#nodeRootId")
    public List<SettingsRole> listByNodeRootId(Long nodeRootId) {
        LambdaQueryWrapper<SettingsRole> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SettingsRole::getNodeRootId, nodeRootId);
        List<SettingsRole> roleList = settingsRoleDao.selectList(queryWrapper);
        return roleList;
    }


    @Override
    public PageInfo<SettingsRoleDTO> listByCondition(Long nodeRootId, String roleName, Integer roleStatus, int pageNum, int pageSize) {
        // 返回的不仅仅需要当前的角色信息，还需要当前的角色关联的功能id
        List<SettingsRoleDTO> roleList = settingsRoleDao.listByCondition(nodeRootId, roleName, roleStatus);
        // 按条件返回空

        if (roleList == null) PageUtils.listToPageInfo(null, pageNum, pageSize);
        for (SettingsRoleDTO roleDTO : roleList) {
            // 通过id得到功能ids
            List<Long> funcId = iSettingRoleFunctionService.getFuncIdsByRoleId(roleDTO.getId());
            if (funcId == null) roleDTO.setFuncIds(new ArrayList<>());
            roleDTO.setFuncIds(funcId);
        }
        return PageUtils.listToPageInfo(roleList, pageNum, pageSize);
    }
}
