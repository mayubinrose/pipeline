package com.ctg.cicd.gate.controller;


import com.ctg.cicd.common.model.dto.EnvResourceDTO;
import com.ctg.cicd.common.model.dto.OperateReturnDTO;
import com.ctg.cicd.common.model.dto.VpcInfoDTO;
import com.ctg.cicd.config.service.IEnvResourceService;
import com.ctg.cloud.paascommon.json.JSONObject;
import com.ctg.cloud.paascommon.utils.SecurityUtils;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 环境资源表 前端控制器
 * </p>
 *
 * @author hel
 * @since 2023-05-30
 */
@RestController
@RequestMapping("/api/v1/config/envResource")
public class EnvResourceController {
    @Autowired
    private IEnvResourceService envResourceService;

    @ApiOperation(value = "获取VPC列表")
    @GetMapping("/listVpc")
    public List<VpcInfoDTO> listVpc(@RequestParam String resPoolCode){
        List<VpcInfoDTO> result = envResourceService.listVpc(resPoolCode);
        return result;
    }

    @ApiOperation(value = "获取基础资源列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "subCategory", value = "资源小类（dcos，ecs，msercm）", required = true)})
    @GetMapping("/listBaseResource")
    public List<EnvResourceDTO> listBaseResource(@RequestParam Long nodeId,
                                                 @RequestParam String resPoolCode,
                                                 @RequestParam String subCategory,
                                                 @RequestParam Long vpcId){
        Long tenantId = SecurityUtils.getCurrentTenantId();
        List<EnvResourceDTO> result =  envResourceService.listBaseResource(tenantId,null,resPoolCode,subCategory, vpcId, nodeId);
        return result;
    }

    @ApiOperation(value = "获取可选资源列表")
    @GetMapping("/listOptionResource")
    public List<EnvResourceDTO> listOptionResource(@RequestParam String resPoolCode,
                                                   @RequestParam Long vpcId){
        Long tenantId = SecurityUtils.getCurrentTenantId();
        List<EnvResourceDTO> result = envResourceService.listOptionResource(tenantId,null,resPoolCode, vpcId);
        return result;
    }

    @ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public PageInfo<EnvResourceDTO> pageQuery(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                               @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                               @RequestParam Long envId,
                                               @RequestParam String mainCategory,
                                               @RequestParam(required = false) String subCategory) {
            Long tenantId = SecurityUtils.getCurrentTenantId();
            PageInfo<EnvResourceDTO> result = envResourceService.pageQuery(pageNum, pageSize, tenantId, envId, mainCategory, subCategory);
            return result;
    }

    @ApiOperation(value = "获取控制台链接")
    @GetMapping("/getAppUrl")
    public JSONObject getAppUrl(@RequestParam Long envId,
                                @RequestParam String instId,
                                @RequestParam(required = false, defaultValue = "false") Boolean isNameSpace){
        String url = envResourceService.getAppUrl(envId,instId,isNameSpace);
        JSONObject result = new JSONObject();
        result.put("url",url);
        return  result;
    }

    @ApiOperation(value = "资源状态同步")
    @GetMapping("/syncResourceInfo")
    OperateReturnDTO syncResourceInfo(@RequestParam Long envId){
        OperateReturnDTO result = envResourceService.syncResourceInfo(envId);
        return result;
    }

}

