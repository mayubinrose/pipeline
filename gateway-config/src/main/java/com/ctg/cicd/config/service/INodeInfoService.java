package com.ctg.cicd.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ctg.cicd.common.model.dto.NodeInfoDTO;
import com.ctg.cicd.common.model.dto.NodeTreeDTO;
import com.ctg.cicd.common.model.vo.NodeInfoAddVO;
import com.ctg.cicd.common.model.vo.NodeInfoUpdateVO;
import com.ctg.cicd.config.entity.NodeInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hel
 * @since 2023-05-17
 */
public interface INodeInfoService extends IService<NodeInfo> {

    Long createNodeInfo(NodeInfoAddVO nodeInfoAddVO, String userName, Long tenantId);

    boolean updateNodeInfo(NodeInfoUpdateVO nodeInfoUpdateVO, String userName, Long tenantId);

    boolean deleteNodeInfo(Long id);

    NodeInfoDTO getNodeInfo(Long id);

    List<NodeInfoDTO> getChildList(Long id,String nodeName);

    NodeInfo getNodeInfo(Long nodeId, String nodeType);

    Long getNodeRootId(Long tenantId);

    NodeTreeDTO getNodeTreeByTenantId(Long tenantId);
}
