package com.ctg.cicd.gate.controller.settings;

import com.ctg.cicd.common.base.MapUtil;
import com.ctg.cicd.config.entity.settings.SettingsConnection;
import com.ctg.cicd.config.entity.settings.SettingsCredentialCode;
import com.ctg.eadp.common.util.PageUtils;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 作者
 * @since 2023-05-30
 */
@RestController
@RequestMapping("/api/v1/cicd/settings/credential")
public class SettingsCredentialCodeController {

    @ApiOperation(value = "新建连接")
    @PostMapping("/add")
    Map<String, Object> add(@RequestBody SettingsCredentialCode settingsCredentialCode) {

        return MapUtil.mapData("add", true);
    }

    @ApiOperation(value = "删除连接")
    @PostMapping("/delete/{id}")
    Map<String, Object> delete(@PathVariable("id") Long id) {

        return MapUtil.mapData("remove", 1);
    }


    @ApiOperation("分页查询连接")
    @GetMapping("/page")
    public PageInfo<SettingsCredentialCode> page(@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {
        List<SettingsCredentialCode> settingsCredentialCodeList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SettingsCredentialCode entity = new SettingsCredentialCode();
            entity.setUuid("uuid-" + i);
            entity.setCredentialName("证书连接-" + i);
            entity.setAuthAccount("账号-" + i);
            entity.setAuthType("GITHUB");
            entity.setAuthPassword("pwd-" + i);
            entity.setTenantId(111L);
            entity.setCreateTime(new Date());
            entity.setCreateBy("user-" + i);
            settingsCredentialCodeList.add(entity);
        }

        PageInfo<SettingsCredentialCode> pageInfo = PageUtils.listToPageInfo(settingsCredentialCodeList, pageNum, pageSize);
        return pageInfo;
    }

}

