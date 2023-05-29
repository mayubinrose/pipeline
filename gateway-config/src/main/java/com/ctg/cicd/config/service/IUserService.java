package com.ctg.cicd.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ctg.cicd.common.model.dto.UserInfoDTO;
import com.ctg.cicd.config.entity.SettingsUser;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hel
 * @since 2023-05-17
 */
public interface IUserService extends IService<SettingsUser> {

    PageInfo<UserInfoDTO> queryAddableUser(Integer pageNum, Integer pageSize, Long tenantId, String keyword);

    PageInfo<SettingsUser> page(Integer pageNum, Integer pageSize, Long nodeId, String userOrRealName);

    PageInfo<UserInfoDTO> queryUserFilterNode(Integer pageNum, Integer pageSize, String keyword, Long nodeId,Long tenantId);
}
