package com.ctg.cicd.gate.controller.settings;


import com.ctg.cicd.common.base.MapUtil;
import com.ctg.cicd.config.entity.settings.SettingsConnection;
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
@RequestMapping("/api/v1/cicd/settings/connection")
public class SettingsConnectionController {

    @ApiOperation(value = "新建连接")
    @PostMapping("/add")
    Map<String, Object> add(@RequestBody SettingsConnection settingsConnection) {

        return MapUtil.mapData("add", true);
    }

    @ApiOperation(value = "删除连接")
    @PostMapping("/delete/{connectionId}")
    Map<String, Object> delete(@PathVariable("id") Long connectionId) {

        return MapUtil.mapData("remove", 1);
    }


    @ApiOperation("分页查询连接")
    @GetMapping("/page")
    public PageInfo<SettingsConnection> page(@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {
        List<SettingsConnection> settingsConnectionList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SettingsConnection entity = new SettingsConnection();
            entity.setConnectionName("代码连接" + i);
            entity.setConnectionType("CODE");
            entity.setUuid("uuid-" + i);
            entity.setCredentialUuid("credential-uuid-" + i);
            entity.setCreateTime(new Date());
            entity.setCreateBy("user-" + i);
            settingsConnectionList.add(entity);
        }

        PageInfo<SettingsConnection> pageInfo = PageUtils.listToPageInfo(settingsConnectionList, pageNum, pageSize);
        return pageInfo;
    }

}

