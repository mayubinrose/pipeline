package com.ctg.cicd.config.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctg.cicd.common.constant.NodeConstant;
import com.ctg.cicd.common.constant.NodeConstant.NodeTypeEnum;
import com.ctg.cicd.common.exception.BusinessException;
import com.ctg.cicd.common.model.vo.NodeUserAddVO;
import com.ctg.cicd.common.model.vo.NodeUserDeleteVO;
import com.ctg.cicd.common.model.vo.NodeUserRoleUpdateVO;
import com.ctg.cicd.common.model.vo.NodeUserRoleVO;
import com.ctg.cicd.config.dao.NodeInfoDao;
import com.ctg.cicd.config.dao.NodeUserRoleDao;
import com.ctg.cicd.config.dao.SettingsUserDao;
import com.ctg.cicd.config.entity.NodeInfo;
import com.ctg.cicd.config.entity.NodeUserRole;
import com.ctg.cicd.config.entity.SettingsRole;
import com.ctg.cicd.config.entity.SettingsUser;
import com.ctg.cicd.config.service.INodeInfoService;
import com.ctg.cicd.config.service.INodeUserRoleService;
import com.ctg.cicd.config.service.ISettingsRoleService;
import com.ctg.cloud.paascommon.exception.VisibleException;
import com.ctg.eadp.common.util.CollectionUtils;
import com.ctg.eadp.common.util.PageUtils;
import com.ctg.eadp.common.util.UuidUtils;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 节点-用户-角色业务实现类
 *
 * @author huangZhi
 * @date 2023/05/18 17:26
 **/
@Service
public class NodeUserRoleService extends ServiceImpl<NodeUserRoleDao, NodeUserRole> implements INodeUserRoleService {

    @Autowired
    NodeUserRoleDao nodeUserRoleDao;

    @Autowired
    NodeInfoDao nodeInfoDao;

    @Autowired
    SettingsUserDao settingsUserDao;

    @Autowired
    INodeInfoService nodeInfoService;

    @Autowired
    ISettingsRoleService settingsRoleService;

    @Override
    public PageInfo<NodeUserRoleVO> pageNodeRole(int pageNum, int pageSize, Long nodeId, String userName,
                                                 String nodeName) {
        List<NodeUserRoleVO> userRoleVOList = listNodeUserRoleVO(nodeId, null, userName, nodeName);
        PageInfo<NodeUserRoleVO> pageInfo = PageUtils.listToPageInfo(userRoleVOList, pageNum, pageSize);
        initExtraInfo(pageInfo.getList(), nodeId);
        return pageInfo;
    }

    @Override
    public List<NodeUserRoleVO> listNodeRole(Long nodeId, Long userId) {
        return listNodeUserRoleVO(nodeId, userId, null, null);
    }

    private List<NodeUserRoleVO> listNodeUserRoleVO(Long nodeId, Long userId, String userName, String nodeName) {
        List<NodeUserRoleVO> userRoleVOList = new ArrayList<>();
        Long nodeIdOld = nodeId;
        for (; nodeIdOld != 0L; ) {
            NodeInfo nodeInfo = nodeInfoDao.selectById(nodeIdOld);
            if (nodeInfo == null) break;
            addVoFromList(nodeUserRoleDao.listNodeUserRole(nodeIdOld, userId, userName,
                    nodeName), nodeInfo, userRoleVOList);
            if (nodeInfo.getParentRoleExtends()) {
                NodeInfo preNodeInfo = nodeInfoDao.selectById(nodeInfo.getParentId());
                nodeIdOld = preNodeInfo.getId();
            } else {
                nodeIdOld = 0L;
            }
        }
        return userRoleVOList;
    }

    private void addVoFromList(List<NodeUserRole> nodeUserRoleList, NodeInfo nodeInfo, List<NodeUserRoleVO> userRoleVOList) {
        if (CollectionUtil.isNotEmpty(nodeUserRoleList)) {
            Map<Long, List<NodeUserRole>> userRoleMap =
                    nodeUserRoleList.stream().collect(Collectors.groupingBy(NodeUserRole::getUserId));
            userRoleMap.forEach((key, value) -> {
                boolean copyOnce = true;
                NodeUserRoleVO userRoleVO = new NodeUserRoleVO();
                StringBuffer roleIds = new StringBuffer();
                for (NodeUserRole nodeUserRole : value) {
                    if (copyOnce) BeanUtils.copyProperties(nodeUserRole, userRoleVO);
                    roleIds.append(Long.toString(nodeUserRole.getRoleId()) + ",");
                    copyOnce = false;
                }
                userRoleVO.setNodeName(nodeInfo.getNodeName());
                userRoleVO.setRoleIds(roleIds.substring(0, roleIds.length() - 1));
                userRoleVOList.add(userRoleVO);
            });
        }
    }


    private void initExtraInfo(List<NodeUserRoleVO> list, Long nodeId) {
        NodeInfo rootNode = nodeInfoService.getNodeInfo(nodeId, NodeTypeEnum.ROOT.name());
        if (rootNode == null || CollectionUtil.isEmpty(list)) return;
        List<SettingsRole> roleList = settingsRoleService.listByNodeRootId(rootNode.getId());
        Map<String, SettingsRole> roleMap = new HashMap<>();
        roleList.forEach(item -> {
            roleMap.put(Long.toString(item.getId()), item);
        });
        Map<Long, SettingsUser> settingsUserMap = new HashMap<Long, SettingsUser>();
        Map<Long, NodeInfo> nodeInfoMap = new HashMap<Long, NodeInfo>();
        list.forEach(item -> {
            // 初始化 角色名称
            String roleIdStr = item.getRoleIds();
            if (StringUtils.isNotEmpty(roleIdStr)) {
                StringBuffer roleNames = new StringBuffer();
                String[] roleIds = roleIdStr.split(",");
                for (String roleId : roleIds) {
                    roleNames.append(roleMap.get(roleId).getRoleName() + ",");
                }
                item.setRoleNames(roleNames.substring(0, roleNames.length() - 1));
            }
            // 初始化 用户信息
            Long userId = item.getUserId();
            SettingsUser settingsUser = null;
            if (settingsUserMap.containsKey(userId)) {
                settingsUser = settingsUserMap.get(userId);
            } else {
                LambdaQueryWrapper<SettingsUser> queryWrapper = Wrappers.lambdaQuery();
                queryWrapper.eq(SettingsUser::getUserId, item.getUserId());
                settingsUser = settingsUserDao.selectOne(queryWrapper);
                settingsUserMap.put(userId, settingsUser);
            }
            item.setUserEmail(settingsUser != null ? settingsUser.getUserEmail() : "");
            item.setRealName(settingsUser != null ? settingsUser.getRealName() : "");
            // 初始化 节点路径
            initNodePath(item, nodeInfoMap, rootNode);
        });
    }


    private void initNodePath(NodeUserRoleVO item, Map<Long, NodeInfo> nodeInfoMap, NodeInfo rootNode) {
        Long nodeIdTemp = item.getNodeId();
        NodeInfo nodeInfo = null;
        if (nodeInfoMap.containsKey(item.getNodeId())) {
            nodeInfo = nodeInfoMap.get(item.getNodeId());
        } else {
            nodeInfo = nodeInfoDao.selectById(nodeIdTemp);
            nodeInfoMap.put(nodeIdTemp, nodeInfo);
        }
        if (nodeInfo == null) {
            return;
        }
        if (NodeTypeEnum.SERVICE.name().equals(nodeInfo.getNodeType())) {
            Long parentId = nodeInfo.getParentId();
            NodeInfo projectNodeInfo = null;
            if (nodeInfoMap.containsKey(parentId)) {
                projectNodeInfo = nodeInfoMap.get(parentId);
            } else {
                projectNodeInfo = nodeInfoDao.selectById(parentId);
                nodeInfoMap.put(parentId, projectNodeInfo);
            }
            if (projectNodeInfo != null)
                item.setNodePath("/" + rootNode.getNodeName() + "/" + projectNodeInfo.getNodeName() + "/" + item.getNodeName());
        } else if (NodeTypeEnum.PROJECT.name().equals(nodeInfo.getNodeType())) {
            item.setNodePath("/" + rootNode.getNodeName() + "/" + nodeInfo.getNodeName());
        } else if (NodeTypeEnum.ROOT.name().equals(nodeInfo.getNodeType())) {
            item.setNodePath("/" + rootNode.getNodeName());
        }
    }

    @Override
    public boolean deleteNodeUserRoleByUserIds(List<Long> userIdList, Long nodeId) {
        LambdaQueryWrapper<NodeUserRole> removeWrapper = Wrappers.lambdaQuery();
        removeWrapper.in(NodeUserRole::getUserId, userIdList).eq(NodeUserRole::getNodeId, nodeId);
        this.remove(removeWrapper);
        return false;
    }

    @Override
    public boolean addNodeUser(NodeUserAddVO nodeUserAddVO) {
        if(CollectionUtils.isEmpty(nodeUserAddVO.getUserList())){
            return true;
        }
        if (Boolean.TRUE.equals(nodeUserAddVO.getUniformRole())&&CollectionUtils.isEmpty(nodeUserAddVO.getRoles())) {
            throw new VisibleException("统一设置成员项目角色不能为空");
        }
        List<NodeUserRole> insertList = new ArrayList<>();
        for (NodeUserAddVO.IamAddUser userVO : nodeUserAddVO.getUserList()) {
            if (!Boolean.TRUE.equals(nodeUserAddVO.getUniformRole())&&CollectionUtils.isEmpty(userVO.getUserRoles())) {
                throw new VisibleException("成员项目角色不能为空");
            }
            if(nodeUserAddVO.getUniformRole()){
                for(Long roleId :nodeUserAddVO.getRoles()){
                    insertList.add(buildNodeUserRole(userVO.getUserId(),userVO.getUserName(),roleId,nodeUserAddVO.getNodeId(),nodeUserAddVO.getStaff()));
                }
            }else{
                for(Long roleId :userVO.getUserRoles()){
                    insertList.add(buildNodeUserRole(userVO.getUserId(),userVO.getUserName(),roleId,nodeUserAddVO.getNodeId(),nodeUserAddVO.getStaff()));
                }
            }
        }
        if(CollectionUtils.isNotEmpty(insertList)){
            this.saveBatch(insertList);
        }
        return true;
    }

    private NodeUserRole buildNodeUserRole(Long userId,String userName,Long roleId,Long nodeId,String staff){
        Date now = new Date();
        NodeUserRole userRoleInsert = new NodeUserRole();
        userRoleInsert.setUserId(userId);
        userRoleInsert.setUserName(userName);
        userRoleInsert.setRoleId(roleId);
        userRoleInsert.setNodeId(nodeId);
        userRoleInsert.setUpdateBy(staff);
        userRoleInsert.setCreateBy(staff);
        userRoleInsert.setUpdateTime(now);
        userRoleInsert.setCreateTime(now);
        return userRoleInsert;
    }

    @Override
    public boolean deleteNodeUser(NodeUserDeleteVO nodeUserDeleteVO) {
        List<Long> userIdList = nodeUserDeleteVO.getUserList().stream().map(u -> u.getUserId()).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(userIdList)){
            //删除关联表
            deleteNodeUserRoleByUserIds(userIdList,nodeUserDeleteVO.getNodeId());
        }
        return true;
    }

    @Override
    public List<Long> getDistinctUserByNodeId(Long nodeId) {
        return nodeUserRoleDao.getDistinctUserByNodeId(nodeId);
    }

    @Override
    public boolean editNodeUserRole(NodeUserRoleUpdateVO updateVO) {
        if(CollectionUtils.isEmpty(updateVO.getRoles())){
            throw BusinessException.USER_ROLES_NOT_NULL.exception();
        }
        List<Long> userIds = new ArrayList<>(1);
        userIds.add(updateVO.getUserId());
        deleteNodeUserRoleByUserIds(userIds,updateVO.getNodeId());
        List<NodeUserRole> insertList = new ArrayList<>(updateVO.getRoles().size());
        for(Long roleId:updateVO.getRoles()){
            NodeUserRole entity = buildNodeUserRole(updateVO.getUserId(),updateVO.getUserName(),roleId,updateVO.getNodeId(),updateVO.getStaff());
            insertList.add(entity);
        }
        this.saveBatch(insertList);
        return true;
    }
    @Override
    public int getUserNumByRoleId(Long roleId) {
        return nodeUserRoleDao.getUserNumByaRoleId(roleId);
    }
    @Override
    public int deleteNodeUserRoleByRoleid(Long roleId) {

        return nodeUserRoleDao.deleteNodeUserRoleByRoleid(roleId);
    }

}