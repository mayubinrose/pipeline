package com.ctg.cicd.gate.controller;


import com.ctg.cicd.common.model.dto.NodeInfoDTO;
import com.ctg.cicd.common.model.dto.NodeTreeDTO;
import com.ctg.cicd.common.model.dto.OperateReturnDTO;
import com.ctg.cicd.common.model.dto.SaveEntityReturnDTO;
import com.ctg.cicd.common.model.vo.NodeInfoAddVO;
import com.ctg.cicd.common.model.vo.NodeInfoUpdateVO;
import com.ctg.cicd.common.model.vo.NodeUserRoleVO;
import com.ctg.cicd.config.entity.SettingsFunction;
import com.ctg.cicd.config.service.INodeInfoService;
import com.ctg.cicd.config.service.INodeUserRoleService;
import com.ctg.cicd.config.service.ISettingRoleFunctionService;
import com.ctg.cloud.paascommon.json.JSONObject;
import com.ctg.cloud.paascommon.utils.SecurityUtils;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hel
 * @since 2023-05-17
 */
@Api(tags = "节点下-节点管理")
@Slf4j
@RestController
@RequestMapping("/api/v1/cicd/node")
public class NodeInfoController {
    @Autowired
    private INodeInfoService nodeInfoService;

    @Autowired
    private ISettingRoleFunctionService iSettingRoleFunctionService;

    @Autowired
    private INodeUserRoleService nodeUserRoleService;

    @ApiOperation(value = "创建节点")
    @PostMapping("/create")
    SaveEntityReturnDTO createNodeInfo(@RequestBody @Valid NodeInfoAddVO nodeInfoAddVO) {
        String userName = SecurityUtils.getUserName();
        Long tenantId = SecurityUtils.getCurrentTenantId();
        SaveEntityReturnDTO result = nodeInfoService.createNodeInfo(nodeInfoAddVO, userName, tenantId);
        return result;
    }

    @ApiOperation(value = "修改节点信息")
    @PostMapping("/update")
    OperateReturnDTO updateNodeInfo(@RequestBody @Valid NodeInfoUpdateVO nodeInfoUpdateVO) {
        String userName = SecurityUtils.getUserName();
        Long tenantId = SecurityUtils.getCurrentTenantId();
        boolean result = nodeInfoService.updateNodeInfo(nodeInfoUpdateVO,userName,tenantId);
        return new OperateReturnDTO(result);
    }

    @ApiOperation(value = "删除节点信息")
    @PostMapping("/{id}/delete")
    OperateReturnDTO deleteNodeInfo(@PathVariable("id") Long id) {
        boolean result = nodeInfoService.deleteNodeInfo(id);
        return new OperateReturnDTO(result);
    }

    @ApiOperation(value = "获取节点信息")
    @GetMapping("/{id}")
    NodeInfoDTO getNodeInfo(@PathVariable("id") Long id) {
        NodeInfoDTO result = nodeInfoService.getNodeInfo(id);
        return result;
    }

    @ApiOperation(value = "获取子节点列表")
    @GetMapping("/{id}/child")
    PageInfo<NodeInfoDTO> getChildList(@PathVariable("id") Long id,
                                   @RequestParam(name = "nodeName",required = false) String nodeName,
                                   @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageInfo<NodeInfoDTO> result = nodeInfoService.getChildList(id,nodeName,pageNum,pageSize);
        return result;
    }

    @ApiOperation(value = "节点下用户功能点查询")
    @GetMapping("/listNodeUserFunc/{nodeId}/{userId}")
    List<SettingsFunction> listNodeUserFunc(@PathVariable("nodeId") Long nodeId, @PathVariable("userId") Long userId) {
        //1、根据nodeId和userId查询出用户的角色集合
        List<NodeUserRoleVO> nodeUserRoleVOList = nodeUserRoleService.listNodeRole(nodeId, userId);
        // 2、根据角色具备的功能点查询出对应的功能点
        List<SettingsFunction> res = iSettingRoleFunctionService.listFuncByRoleList(nodeUserRoleVOList);
        return res;
    }

    @ApiOperation(value = "获取节点树")
    @GetMapping("/tree")
    NodeTreeDTO getNodeTree() {
        log.info(String.format("getNodeTree:currentUserName:%s,tenantId:%s",SecurityUtils.getUserName(),SecurityUtils.getCurrentTenantId()));
        NodeTreeDTO result = nodeInfoService.getNodeTreeByTenantId(3475L);
        return result;
    }

}

