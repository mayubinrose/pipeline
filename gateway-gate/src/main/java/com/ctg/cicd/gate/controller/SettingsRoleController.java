package com.ctg.cicd.gate.controller;


import com.ctg.cicd.common.model.dto.SettingsRoleDTO;
import com.ctg.cicd.common.model.vo.SettingsRoleAddVo;
import com.ctg.cicd.common.model.vo.SettingsRoleUpdateVo;
import com.ctg.cicd.config.service.INodeInfoService;
import com.ctg.cicd.config.service.ISettingsRoleService;
import com.ctg.cloud.paascommon.json.JSONObject;
import com.ctg.cloud.paascommon.utils.SecurityUtils;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hel
 * @since 2023-05-17
 */
@RestController
@Slf4j
@Api(tags = "设置-角色管理")
@RequestMapping("/api/v1/cicd/settings/role")
public class SettingsRoleController {
    @Autowired
    private ISettingsRoleService iSettingsRoleService;

    @Autowired
    private INodeInfoService iNodeInfoService;

    @ApiOperation(value = "节点角色增加")
    @PostMapping("/add")
    JSONObject addRoleUser(@RequestBody SettingsRoleAddVo settingsRoleAddVo) {
        String userName =  SecurityUtils.getUserName();
        JSONObject data = new JSONObject();
        boolean res = iSettingsRoleService.createRole(settingsRoleAddVo, userName);
        data.put("success", res);
        return data;
    }

    @ApiOperation(value = "节点角色删除")
    @PostMapping("/delete/{id}")
    JSONObject deleteRoleUser(@PathVariable(name = "id") Long id) {
        JSONObject data = new JSONObject();
        boolean res = iSettingsRoleService.deleteRole(id);
        data.put("success", res);
        return data;
    }

    @ApiOperation(value = "节点角色修改")
    @PostMapping("/update")
    JSONObject updateRole(@RequestBody SettingsRoleUpdateVo settingsRollUpdateVo) {
        // 角色修改成功 返回true  否则返回false
        String userName =  SecurityUtils.getUserName();
        boolean res = iSettingsRoleService.updateRole(userName, settingsRollUpdateVo);
        JSONObject data = new JSONObject();
        data.put("success", res);
        return data;
    }

    @ApiOperation(value = "分页获取根节点下的所有角色，按照角色名，与角色状态进行过滤,还需要包含功能ID列表")
    @GetMapping("/getAllRoles")
    PageInfo<SettingsRoleDTO> listByNodeRootId(
            @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(name = "roleName", required = false) String roleName,
            @RequestParam(name = "roleStatus", required = false) Integer roleStatus) {
        // 当前租户为空的话，直接返回根节点的id为1
        Long tenantId = SecurityUtils.getCurrentTenantId();
        Long nodeRootId = iNodeInfoService.getNodeRootId(tenantId);
        return iSettingsRoleService.listByCondition(nodeRootId, roleName, roleStatus, pageNum, pageSize);
    }


}

