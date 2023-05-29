package com.ctg.cicd.gate.controller;


import com.ctg.cicd.common.model.vo.NodeUserRoleVO;
import com.ctg.cicd.common.model.vo.SettingsRoleAddVo;
import com.ctg.cicd.config.entity.SettingsFunction;
import com.ctg.cicd.config.service.INodeUserRoleService;
import com.ctg.cicd.config.service.ISettingsFunctionService;
import com.ctg.cloud.paascommon.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hel
 * @since 2023-05-17
 */
@Api(tags = "设置-功能点")
@RestController
@RequestMapping("/api/v1/cicd/settings/function")
public class SettingsFunctionController {
    @Autowired
    private ISettingsFunctionService iSettingsFunctionService;


    @ApiOperation(value = "功能点预览")
    @GetMapping("/getAllFunc")
    List<SettingsFunction> getAllFunc() {
        // 得到所有的功能点，与节点角色无关。
        List<SettingsFunction> res = iSettingsFunctionService.getAllFunc();
        return res;
    }


}

