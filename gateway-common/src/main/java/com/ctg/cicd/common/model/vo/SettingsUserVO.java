package com.ctg.cicd.common.model.vo;

import lombok.Data;

/**
 * @author huangZhi
 * @date 2023/05/23 20:57
 **/
@Data
public class SettingsUserVO {
    private Long id;

    private Long userId;

    /**
     * 用户uuid
     */
    private String userUuid;

    /**
     * 关联的项目根节点的id
     */
    private Long nodeRootId;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 用户名
     */
    private String userName;


    /**
     * 用户账号
     */
    private String realName;


    /**
     * 用户邮箱
     */
    private String userEmail;
}