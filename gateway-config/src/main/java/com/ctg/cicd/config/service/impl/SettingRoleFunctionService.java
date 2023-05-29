package com.ctg.cicd.config.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ctg.cicd.common.exception.BusinessException;
import com.ctg.cicd.common.model.vo.NodeUserRoleVO;
import com.ctg.cicd.common.model.vo.SettingsRoleFuncAddVo;
import com.ctg.cicd.config.dao.SettingRoleFunctionDao;
import com.ctg.cicd.config.dao.SettingsFunctionDao;
import com.ctg.cicd.config.dao.SettingsRoleDao;
import com.ctg.cicd.config.entity.SettingRoleFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctg.cicd.config.entity.SettingsFunction;
import com.ctg.cicd.config.entity.SettingsRole;
import com.ctg.cicd.config.service.INodeInfoService;
import com.ctg.cicd.config.service.ISettingRoleFunctionService;
import com.ctg.cloud.paascommon.utils.SecurityUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hel
 * @since 2023-05-17
 */
@Service
public class SettingRoleFunctionService extends ServiceImpl<SettingRoleFunctionDao, SettingRoleFunction> implements ISettingRoleFunctionService {
    @Autowired
    private SettingRoleFunctionDao settingRoleFunctionDao;
    @Autowired
    private INodeInfoService iNodeInfoService;

    @Autowired
    private SettingsFunctionDao settingsFunctionDao;

    @Autowired
    private SettingsRoleDao settingsRoleDao;

    @Override
    public boolean addFunc(String userName, SettingsRoleFuncAddVo settingsRoleFuncAddVo) {
        /*
            批量插入相关的功能点id
         */
        List<SettingRoleFunction> inserts = new ArrayList<>();
        Long tenantId = SecurityUtils.getCurrentTenantId()==null?10081:SecurityUtils.getCurrentTenantId();
        Long nodeRootId = iNodeInfoService.getNodeRootId(tenantId);
        Date now = new Date();
        SettingsRole settingsRole = settingsRoleDao.selectById(settingsRoleFuncAddVo.getRoleId());
        // 当前角色id在表中没有
        if (settingsRole == null) throw BusinessException.NOT_EXIST_CURRENT_ROLE.exception();
        // 先删除相关角色的功能
        settingRoleFunctionDao.deleteByRoleId(settingsRoleFuncAddVo.getRoleId());
        for (Long funcId : settingsRoleFuncAddVo.getFuncIds()) {
            SettingRoleFunction insert = new SettingRoleFunction();
            insert.setCreateBy(userName);
            insert.setCreateTime(now);
            insert.setUpdateBy(userName);
            insert.setUpdateTime(now);
            insert.setRoleId(settingsRoleFuncAddVo.getRoleId());
            insert.setNodeRootId(nodeRootId);
            insert.setFuncId(funcId);
            inserts.add(insert);
        }
        // 批量插入
        settingRoleFunctionDao.insertList(inserts);
        return true;
    }

    @Override
    public List<SettingsFunction> listFuncByRoleList(List<NodeUserRoleVO> nodeUserRoleVOList) {
        List<SettingsFunction> resultList = new ArrayList<SettingsFunction>();
        Set<String> roleSet = new HashSet<String>();
        Set<Long> funcSet = new HashSet<Long>();
        if (CollectionUtil.isNotEmpty(nodeUserRoleVOList)) {
            nodeUserRoleVOList.forEach(item -> { //1层
                String roleIds = item.getRoleIds();
                if (StringUtils.isNotEmpty(roleIds)) {
                    String[] roleIdStrs = roleIds.split(",");
                    for (String roleId : roleIdStrs) { //2层
                        if (!roleSet.contains(roleId)) {
                            LambdaQueryWrapper<SettingRoleFunction> queryWrapper = Wrappers.lambdaQuery();
                            queryWrapper.eq(SettingRoleFunction::getRoleId, Long.parseLong(roleId));
                            List<SettingRoleFunction> roleFunctionList = settingRoleFunctionDao.selectList(queryWrapper);
                            if (CollectionUtil.isNotEmpty(roleFunctionList)) {
                                roleFunctionList.forEach(roleFunctionItem -> { //3层
                                    Long funcId = roleFunctionItem.getFuncId();
                                    if (!funcSet.contains(funcId)) {
                                        LambdaQueryWrapper<SettingsFunction> funcWrapper = Wrappers.lambdaQuery();
                                        funcWrapper.eq(SettingsFunction::getId, funcId);
                                        SettingsFunction settingsFunction = settingsFunctionDao.selectOne(funcWrapper);
                                        resultList.add(settingsFunction);
                                        funcSet.add(funcId);
                                    }
                                });
                            }
                            roleSet.add(roleId);
                        }
                    }
                }
            });
        }
        return resultList;
    }

    @Override
    public List<Long> getFuncIdsByRoleId(Long id) {
        List<Long> funIds  = settingRoleFunctionDao.selectByRoleId(id);
        return funIds;
    }
}
