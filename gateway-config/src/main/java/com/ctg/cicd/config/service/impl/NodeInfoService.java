package com.ctg.cicd.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctg.cicd.common.constant.NodeConstant;
import com.ctg.cicd.common.constant.NodeConstant.NodeTypeEnum;
import com.ctg.cicd.common.exception.BusinessException;
import com.ctg.cicd.common.model.dto.NodeInfoDTO;
import com.ctg.cicd.common.model.dto.NodeTreeDTO;
import com.ctg.cicd.common.model.vo.NodeInfoAddVO;
import com.ctg.cicd.common.model.vo.NodeInfoUpdateVO;
import com.ctg.cicd.config.dao.NodeInfoDao;
import com.ctg.cicd.config.entity.NodeInfo;
import com.ctg.cicd.config.service.INodeInfoService;
import com.ctg.eadp.common.util.CollectionUtils;
import com.ctg.eadp.common.util.UuidUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
public class NodeInfoService extends ServiceImpl<NodeInfoDao, NodeInfo> implements INodeInfoService {

    @Autowired
    private NodeInfoDao nodeInfoDao;



    @Override
    public Long createNodeInfo(NodeInfoAddVO nodeInfoAddVO,String userName,Long tenantId) {
        NodeInfo insert = new NodeInfo();
        BeanUtils.copyProperties(nodeInfoAddVO,insert);
        NodeInfo parentNode = getById(insert.getParentId());
        if(parentNode == null){
            throw BusinessException.NODE_INFO_NOT_FOUND.exception(insert.getParentId());
        }
        this.checkNodeType(parentNode,insert);

        Date now = new Date();
        insert.setUpdateBy(userName);
        insert.setCreateBy(userName);
        insert.setUpdateTime(now);
        insert.setCreateTime(now);
        insert.setTenantId(tenantId);
        insert.setDeleted(Boolean.FALSE);
        insert.setNodeUuid(UuidUtils.generateUuid());
        nodeInfoDao.insertNodeInfo(insert);
        return insert.getId();
    }

    private void checkNodeType(NodeInfo parentNode, NodeInfo insert) {
        if(parentNode.getNodeType().equals(NodeConstant.NodeTypeEnum.ROOT.name())){
            if(!insert.getNodeType().equals(NodeTypeEnum.PROJECT.name())){
                throw BusinessException.ROOT_ADD_NODE_TYPE_ERROR.exception(insert.getParentId());
            }
        }else if(parentNode.getNodeType().equals(NodeTypeEnum.PROJECT.name())){
            if(!insert.getNodeType().equals(NodeTypeEnum.SERVICE.name())){
                throw BusinessException.PROJECT_ADD_NODE_TYPE_ERROR.exception(insert.getParentId());
            }
        }else if(parentNode.getNodeType().equals(NodeTypeEnum.SERVICE.name())){
            throw BusinessException.SERVICE_ADD_NODE_TYPE_ERROR.exception(insert.getParentId());
        }
    }

    @Override
    public boolean updateNodeInfo(NodeInfoUpdateVO nodeInfoUpdateVO,String userName,Long tenantId) {
        NodeInfo update = new NodeInfo();
        BeanUtils.copyProperties(nodeInfoUpdateVO,update);
        Date now = new Date();
        update.setUpdateBy(userName);
        update.setUpdateTime(now);
        nodeInfoDao.updateById(update);
        return true;
    }

    @Override
    public boolean deleteNodeInfo(Long id) {
        NodeInfo delete =  nodeInfoDao.selectById(id);
        if(delete==null){
            throw BusinessException.NODE_INFO_NOT_FOUND.exception(id);
        }

        if(delete.getNodeType().equals(NodeConstant.NodeTypeEnum.ROOT.name())){
            throw BusinessException.ROOT_NODE_DELETE_NOT_ALLOW.exception();
        }else if(delete.getNodeType().equals(NodeTypeEnum.PROJECT.name())&&!CollectionUtils.isEmpty(getChildNodeList(id))){
            throw BusinessException.EXIST_CHILD_NODE_DELETE_NOT_ALLOW.exception();
        }else if(delete.getNodeType().equals(NodeConstant.NodeTypeEnum.SERVICE.name())){
            //todo:服务节点判断是否存在流水线
            //当前节点存在流水线，不允许删除
            //throw BusinessException.EXIST_PIPELINE_DELETE_NOT_ALLOW.exception();
        }
        nodeInfoDao.deleteById(delete);
        return true;
    }

    @Override
    public NodeInfoDTO getNodeInfo(Long id) {

        NodeInfo nodeInfo = nodeInfoDao.selectById(id);
        if(nodeInfo==null){
            throw BusinessException.NODE_INFO_NOT_FOUND.exception(id);
        }
        NodeInfoDTO result = new NodeInfoDTO();
        BeanUtils.copyProperties(nodeInfo,result);
        result.setNodePath(getNodePath(nodeInfo));
        return result;
    }

    private String getNodePath(NodeInfo nodeInfo) {
        LambdaQueryWrapper<NodeInfo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(NodeInfo::getTenantId, nodeInfo.getTenantId());
        List<NodeInfo> nodeList = this.list(queryWrapper);
        List<Long> parentNode = new ArrayList<>();
        parentNode.add(nodeInfo.getId());
        List<String> parentNodeName = new ArrayList<>();
        parentNodeName.add(nodeInfo.getNodeName());
        getParentCategoryObject(nodeInfo,nodeList,parentNode,parentNodeName);
        String nodePath = String.join("/", parentNodeName);
        return nodePath;
    }

    /**
     * ，，
     * @param category  要查询父节点的实体
     * @param nodeList  存放所有节点的列表
     * @param parentNode 最终存放父节点的列表
     * @param parentNode 最终存放父节点名称的列表
     * @return
     */
    public List<String> getParentCategoryObject(NodeInfo category,List<NodeInfo> nodeList,List<Long> parentNode,List<String> parentNodeName) {
        if (category.getParentId() == -1) {
            Collections.reverse(parentNodeName);
            return parentNodeName;
        }
        List<NodeInfo> nodelist = nodeList.stream().filter(x -> Objects.equals(x.getId(), category.getParentId())).collect(Collectors.toList());
        parentNode.add(nodelist.get(0).getId());
        parentNodeName.add(nodelist.get(0).getNodeName());
        //递归
        return getParentCategoryObject(nodelist.get(0),nodeList, parentNode,parentNodeName);
    }

    public List<NodeInfoDTO> getChildList(Long id,String nodeName) {
        List<NodeInfoDTO> list  = nodeInfoDao.selectChildList(id,nodeName);
        if(CollectionUtils.isEmpty(list)){
            return Collections.emptyList();
        }
        for(NodeInfoDTO nodeInfo:list){
            NodeInfo obj = new NodeInfo();
            BeanUtils.copyProperties(nodeInfo,obj);
            String nodePath = getNodePath(obj);
            nodeInfo.setNodePath(nodePath);
        }
        return list;
    }

    List<NodeInfo> getChildNodeList(Long parentNodeId) {
        LambdaQueryWrapper<NodeInfo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(NodeInfo::getParentId, parentNodeId);
        List<NodeInfo> childNodeList = this.list(queryWrapper);
        return CollectionUtils.isEmpty(childNodeList) ? Collections.EMPTY_LIST : childNodeList;
    }

    public NodeInfo getNodeInfo(Long nodeId, String nodeType) {
        NodeInfo nodeInfo = nodeInfoDao.selectById(nodeId);
        if (nodeInfo == null) return null;
        for (; !NodeTypeEnum.ROOT.name().equals(nodeInfo.getNodeType()); ) {
            if (nodeInfo.getNodeType().equals(nodeType)) {
                return nodeInfo;
            } else {
                nodeInfo = nodeInfoDao.selectById(nodeInfo.getParentId());
            }
        }
        if (nodeInfo.getNodeType().equals(nodeType)) {
            return nodeInfo;
        }
        return null;
    }

    @Override
    public Long getNodeRootId(Long tenantId) {
        //根据租户查根节点的id ,parentId 为-1
        Long nodeRootId = nodeInfoDao.getNodeRootId(tenantId);
        return nodeRootId;
    }

    @Override
    public NodeTreeDTO getNodeTreeByTenantId(Long tenantId) {
        LambdaQueryWrapper<NodeInfo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(NodeInfo::getTenantId, tenantId);
        List<NodeInfo> nodeList = this.list(queryWrapper);
        NodeTreeDTO tree = new NodeTreeDTO();
        List<NodeTreeDTO> nodeTreeList = null;
        if (CollectionUtils.isNotEmpty(nodeList)) {
            nodeTreeList = nodeList.stream().map(nodeInfo -> {
                NodeTreeDTO userCheckData = new NodeTreeDTO();
                BeanUtils.copyProperties(nodeInfo, userCheckData);
                return userCheckData;
            }).collect(Collectors.toList());
        }

        for (NodeTreeDTO node : nodeTreeList) {
            //找到根节点进行处理，找下一级节点
            if (node.getParentId() == -1) {
                tree = findChildren(node, nodeTreeList);
            }
        }

        return tree;
    }

    public static NodeTreeDTO findChildren(NodeTreeDTO rootNode, List<NodeTreeDTO> list) {
        for (NodeTreeDTO child : list) {
            if (rootNode.getId().equals(child.getParentId())) {
                if (rootNode.getChildren() == null) {
                    rootNode.setChildren(new ArrayList<>());
                }
                rootNode.getChildren().add(findChildren(child, list));
            }
        }

        return rootNode;
    }

}
