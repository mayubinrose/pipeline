package com.ctg.cicd.gate.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ctg.cicd.common.base.MapUtil;
import com.ctg.cicd.common.model.dto.UserInfoDTO;
import com.ctg.cicd.common.model.vo.NodeUserRoleVO;
import com.ctg.cicd.common.model.vo.SettingUserDeleteVO;
import com.ctg.cicd.common.model.vo.SettingsUserVO;
import com.ctg.cicd.config.dao.SettingsUserDao;
import com.ctg.cicd.config.entity.SettingsUser;
import com.ctg.cicd.config.service.IUserService;
import com.ctg.cloud.paascommon.utils.SecurityUtils;
import com.ctg.cloud.paascommon.vo.Response;
import com.ctg.eadp.common.util.StringUtils;
import com.ctg.eadp.common.util.UuidUtils;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "设置-成员管理")
@RestController
@RequestMapping("/api/v1/cicd/settings/user")
public class SettingUserController {
    @Autowired
    private SettingsUserDao settingsUserDao;
    @Autowired
    private IUserService userService;

    @ApiOperation(value = "添加成员")
    @PostMapping("/add")
    Map<String, Object> add(@RequestBody List<SettingsUser> settingsUsers) {
        if (CollectionUtil.isNotEmpty(settingsUsers)) {
            for (SettingsUser settingsUser : settingsUsers) {
                settingsUser.setCreateBy(StringUtils.isNullOrEmpty(SecurityUtils.getUserName()) ? "system" :
                        SecurityUtils.getUserName());
                settingsUser.setUserUuid(UuidUtils.generateUuid());
                settingsUserDao.insert(settingsUser);
            }
        }
        return MapUtil.mapData("add", true);
    }


    @ApiOperation(value = "移除成员")
    @PostMapping("/remove")
    Map<String, Object> remove(@RequestBody SettingUserDeleteVO settingUserDeleteVO) {
        LambdaQueryWrapper<SettingsUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SettingsUser::getUserUuid, settingUserDeleteVO.getUserUuid());
        int result = settingsUserDao.delete(queryWrapper);
        return MapUtil.mapData("remove", result);
    }

    @ApiOperation("查询成员列表")
    @GetMapping("/page/member")
    PageInfo<UserInfoDTO> pageMember(@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                     @RequestParam(required = false) String keyword) {
        return userService.queryAddableUser(pageNum, pageSize, SecurityUtils.getCurrentTenantId()==null?3475:SecurityUtils.getCurrentTenantId(), keyword);
    }


    @ApiOperation("分页获取节点下成员列表")
    @GetMapping("/page")
    public PageInfo<SettingsUser> page(@RequestParam(value = "nodeId") Long nodeId,
                                       @RequestParam(value = "userOrRealName", required = false) String userOrRealName,
                                       @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {
        return userService.page(pageNum, pageSize, nodeId, userOrRealName);
    }
}

