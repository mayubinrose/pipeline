package com.ctg.cicd.config.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctg.cicd.common.constant.NodeConstant;
import com.ctg.cicd.common.model.dto.UserInfoDTO;
import com.ctg.cicd.config.dao.SettingsUserDao;
import com.ctg.cicd.config.entity.NodeInfo;
import com.ctg.cicd.config.entity.SettingsUser;
import com.ctg.cicd.config.manager.IamManager;
import com.ctg.cicd.config.service.INodeInfoService;
import com.ctg.cicd.config.service.INodeUserRoleService;
import com.ctg.cicd.config.service.IUserService;
import com.ctg.eadp.common.util.CollectionUtils;
import com.ctg.eadp.common.util.PageUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hel
 * @since 2023-05-17
 */
@Service
public class UserService extends ServiceImpl<SettingsUserDao, SettingsUser> implements IUserService {

    @Autowired
    private INodeUserRoleService nodeUserRoleService;

    @Autowired
    private INodeInfoService nodeService;

    @Autowired
    private INodeInfoService nodeInfoService;

    @Autowired
    private IamManager iamManager;

    @Autowired
    private SettingsUserDao settingsUserDao;


    @Override
    public PageInfo<UserInfoDTO> queryAddableUser(Integer pageNum, Integer pageSize, Long tenantId, String keyword) {
        //已存在的项目成员数据
        List<SettingsUser> projectMemberDTOS = this.queryTenantUser(tenantId);
        List<Long> excludeUserIds =
                projectMemberDTOS.stream().map(pm -> pm.getUserId()).distinct().collect(Collectors.toList());
        if (CollUtil.isEmpty(excludeUserIds)) {
            excludeUserIds = null;
        }

        return iamManager.queryTenantMembers(pageNum, pageSize, null, tenantId,
                keyword, excludeUserIds);
    }

    private List<SettingsUser> queryTenantUser(Long tenantId) {
        LambdaQueryWrapper<SettingsUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(SettingsUser::getTenantId,tenantId);
        return this.list(queryWrapper);
    }


    @Override
    public PageInfo<SettingsUser> page(Integer pageNum, Integer pageSize, Long nodeId, String userOrRealName) {
        NodeInfo rootNode = nodeInfoService.getNodeInfo(nodeId, NodeConstant.NodeTypeEnum.ROOT.name());
        LambdaQueryWrapper<SettingsUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SettingsUser::getNodeRootId, rootNode.getId());
        List<SettingsUser> userList = this.list(queryWrapper);
        PageInfo<SettingsUser> pageInfo = PageUtils.listToPageInfo(userList, pageNum, pageSize);
        return pageInfo;
    }



    @Override
    public PageInfo<UserInfoDTO> queryUserFilterNode(Integer pageNum, Integer pageSize, String keyword, Long nodeId,Long tenantId) {
        //已存在的项目成员数据
        List<Long> excludeUserIds = nodeUserRoleService.getDistinctUserByNodeId(nodeId);

        Long nodeRootId = nodeInfoService.getNodeRootId(tenantId);
        if (CollUtil.isEmpty(excludeUserIds)) {
            excludeUserIds = null;
        }
        List<UserInfoDTO> list = settingsUserDao.queryUserByExcludeUser(excludeUserIds,nodeRootId,keyword);
        PageInfo<UserInfoDTO> pageInfo = PageUtils.listToPageInfo(list, pageNum, pageSize);
        return pageInfo;
    }
}
