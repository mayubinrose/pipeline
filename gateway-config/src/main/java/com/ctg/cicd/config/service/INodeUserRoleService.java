package com.ctg.cicd.config.service;

import com.ctg.cicd.common.model.vo.NodeUserAddVO;
import com.ctg.cicd.common.model.vo.NodeUserDeleteVO;
import com.ctg.cicd.common.model.vo.NodeUserRoleUpdateVO;
import com.ctg.cicd.common.model.vo.NodeUserRoleVO;
import com.ctg.cicd.config.entity.NodeUserRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 *  节点-用户-角色业务接口
 * </p>
 *
 * @author hel
 * @since 2023-05-17
 */
public interface INodeUserRoleService extends IService<NodeUserRole> {

    PageInfo<NodeUserRoleVO> pageNodeRole(int pageNum, int pageSize, Long nodeId, String userName, String nodeName);

    List<NodeUserRoleVO> listNodeRole(Long nodeId, Long userId);

    boolean deleteNodeUserRoleByUserIds(List<Long> userIdList, Long nodeId);

    boolean addNodeUser(NodeUserAddVO nodeUserAddVO);

    boolean deleteNodeUser(NodeUserDeleteVO nodeUserDeleteVO);

    List<Long> getDistinctUserByNodeId(Long nodeId);

    boolean editNodeUserRole(NodeUserRoleUpdateVO nodeUserRoleUpdateVO);


    int deleteNodeUserRoleByRoleid(Long roleId);

    int getUserNumByRoleId(Long roleId);
}
