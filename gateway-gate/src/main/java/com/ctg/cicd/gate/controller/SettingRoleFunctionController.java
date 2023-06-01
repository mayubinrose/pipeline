package com.ctg.cicd.gate.controller;


import com.ctg.cicd.common.model.vo.SettingsRoleFuncAddVo;
import com.ctg.cicd.config.service.ISettingRoleFunctionService;
import com.ctg.cloud.paascommon.json.JSONObject;
import com.ctg.cloud.paascommon.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "设置-角色授权功能点")
@RestController
@RequestMapping("/api/v1/cicd/settings/role/function")
public class SettingRoleFunctionController {

    @Autowired
    private ISettingRoleFunctionService iSettingRoleFunctionService;


    @ApiOperation(value = "节点设置功能点")
    @PostMapping("/addFunc")
    JSONObject addFunc(@RequestBody SettingsRoleFuncAddVo settingsRoleFuncAddVo) {
        // 给某个根节点下的角色添加功能点 , 涉及的表是关联表。
        String userName = SecurityUtils.getUserName();
        // 添加角色功能失败的话，返回false，否则返回true
        JSONObject data = new JSONObject();
        boolean res = iSettingRoleFunctionService.addFunc(userName, settingsRoleFuncAddVo);
        data.put("success", res);
        return data;
    }


}

