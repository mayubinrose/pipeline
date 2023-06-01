package com.ctg.cicd.gate.controller;


import com.ctg.cicd.common.constant.EnvConstant;
import com.ctg.cicd.common.model.dto.EnvTypeDTO;
import com.ctg.cicd.common.model.dto.OperateReturnDTO;
import com.ctg.cicd.common.model.dto.SaveEntityReturnDTO;
import com.ctg.cicd.common.model.vo.EnvVO;
import com.ctg.cicd.config.service.IEnvService;
import com.ctg.cloud.paascommon.utils.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 环境表 前端控制器
 * </p>
 *
 * @author hel
 * @since 2023-05-30
 */
@RestController
@RequestMapping("/api/v1/config/env")
public class EnvController {
    @Autowired
    private IEnvService envService;

    @ApiOperation(value = "创建环境")
    @PostMapping("/create")
    SaveEntityReturnDTO create(@RequestBody @Valid EnvVO envVO) {
        String userName = SecurityUtils.getUserName();
        Long tenantId = SecurityUtils.getCurrentTenantId();
        SaveEntityReturnDTO result = envService.createEnv(envVO, userName, tenantId);
        return result;
    }


    @ApiOperation(value = "更新环境")
    @PostMapping("/{id}/update")
    OperateReturnDTO update(@PathVariable Long id, @Valid @RequestBody EnvVO env)  {
        String userName = SecurityUtils.getUserName();
        OperateReturnDTO result = envService.updateEnv(id, env,userName);
        return result;
    }

    @ApiOperation(value = "环境类型列表")
    @GetMapping("/envTypes")
    List<EnvTypeDTO> envTypes()  {
        List<EnvTypeDTO> result = new ArrayList<>(4);
        result.add(new EnvTypeDTO(EnvConstant.EnvType.DEVELOP.name(), EnvConstant.EnvType.DEVELOP.getValue()));
        result.add(new EnvTypeDTO(EnvConstant.EnvType.TEST.name(), EnvConstant.EnvType.TEST.getValue()));
        result.add(new EnvTypeDTO(EnvConstant.EnvType.PRE_ONLINE.name(), EnvConstant.EnvType.PRE_ONLINE.getValue()));
        result.add(new EnvTypeDTO(EnvConstant.EnvType.ONLINE.name(), EnvConstant.EnvType.ONLINE.getValue()));
        return result;
    }

    @ApiOperation(value = "不分页环境列表")
    @GetMapping("/list")
    public List<EnvVO> listEnv(
            @RequestParam(required = false) String envName,
            @RequestParam(required = false) String envType,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) Long nodeId
    ) {
        Long userId = SecurityUtils.getUserId();
        Long tenantId = SecurityUtils.getCurrentTenantId();
        List<EnvVO> list = envService.listEnvByNodeId(nodeId, envName, envType, sort, order, userId, tenantId);
        return list;
    }


    @ApiOperation("删除指定环境")
    @PostMapping("/{id}/delete")
    OperateReturnDTO delete(@PathVariable Long id) {
        String staff = SecurityUtils.getUserName();
        OperateReturnDTO result = envService.deleteEnvById(id, staff);
        return result;
    }

    @ApiOperation(value = "环境详情")
    @GetMapping("/{id}/detail")
    EnvVO detail( @PathVariable Long id){
        EnvVO result = envService.detailEnvById(id);
        return result;
    }
}

