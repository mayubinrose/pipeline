package com.ctg.cicd.gate.controller;


import com.ctg.cicd.common.model.dto.OperateReturnDTO;
import com.ctg.cicd.common.model.dto.UserInfoDTO;
import com.ctg.cicd.common.model.vo.NodeUserAddVO;
import com.ctg.cicd.common.model.vo.NodeUserDeleteVO;
import com.ctg.cicd.common.model.vo.NodeUserRoleUpdateVO;
import com.ctg.cicd.common.model.vo.NodeUserRoleVO;
import com.ctg.cicd.config.service.INodeUserRoleService;
import com.ctg.cicd.config.service.IUserService;
import com.ctg.cloud.paascommon.utils.SecurityUtils;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hel
 * @since 2023-05-17
 */
@Api(tags = "节点下-成员角色管理")
@RestController
@RequestMapping("/api/v1/cicd/node/user/role")
public class NodeUserRoleController {

    @Autowired
    INodeUserRoleService nodeUserRoleService;

    @Autowired
    private IUserService userService;

    @ApiOperation("分页获取节点下成员列表")
    @GetMapping("/page")
    public PageInfo<NodeUserRoleVO> pageUserRole(@RequestParam(value = "nodeId") Long nodeId,
                                                 @RequestParam(value = "userName", required = false) String userName,
                                                 @RequestParam(value = "nodeName", required = false) String nodeName,
                                                 @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {
        return nodeUserRoleService.pageNodeRole(pageNum, pageSize, nodeId, userName, nodeName);
    }

    @ApiOperation("查询可被添加的成员")
    @GetMapping("/addable/query")
    public PageInfo<UserInfoDTO> queryAddableMember(@RequestParam Integer pageNum,
                                                              @RequestParam Integer pageSize,
                                                              @RequestParam Long nodeId,
                                                              @RequestParam(required = false) String keyword) {
        Long tenantId = SecurityUtils.getCurrentTenantId();
        return userService.queryUserFilterNode(pageNum, pageSize, keyword, nodeId,tenantId);
    }

    @ApiOperation(value = "添加成员角色")
    @PostMapping("/add")
    OperateReturnDTO createNodeUser(@RequestBody NodeUserAddVO nodeUserAddVO) {
        String userName = SecurityUtils.getUserName();
        Long tenantId = SecurityUtils.getCurrentTenantId();
        nodeUserAddVO.setStaff(userName);
        nodeUserAddVO.setTenantId(tenantId);
        boolean result = nodeUserRoleService.addNodeUser(nodeUserAddVO);
        return new OperateReturnDTO(result);
    }


    @ApiOperation(value = "移除成员角色")
    @PostMapping("/remove")
    OperateReturnDTO deleteNodeUser(@RequestBody NodeUserDeleteVO nodeUserDeleteVO) {
        String userName = SecurityUtils.getUserName();
        Long tenantId = SecurityUtils.getCurrentTenantId();
        nodeUserDeleteVO.setStaff(userName);
        nodeUserDeleteVO.setTenantId(tenantId);
        boolean result = nodeUserRoleService.deleteNodeUser(nodeUserDeleteVO);
        return new OperateReturnDTO(result);
    }

    @ApiOperation(value = "编辑成员角色")
    @PostMapping("/edit")
    OperateReturnDTO editNodeUserRole(@RequestBody @Valid NodeUserRoleUpdateVO nodeUserRoleUpdateVO) {
        String userName = SecurityUtils.getUserName();
        Long tenantId = SecurityUtils.getCurrentTenantId();
        nodeUserRoleUpdateVO.setStaff(userName);
        nodeUserRoleUpdateVO.setTenantId(tenantId);
        boolean result = false;
        try {
             result = nodeUserRoleService.editNodeUserRole(nodeUserRoleUpdateVO);
        }catch (Exception e){
            e.printStackTrace();
        }

        return new OperateReturnDTO(result);
    }
}

