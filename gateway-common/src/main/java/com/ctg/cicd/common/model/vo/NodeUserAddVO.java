package com.ctg.cicd.common.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author hel
 * @date 2023/5/18
 */
@Data
public class NodeUserAddVO {

    /**
     * 批量设置共有角色ID集合
     */
    private List<Long> roles;
    /**
     * 是否统一设置
     */
    private Boolean uniformRole;
    /**
     * 用户信息列表
     */
    private List<IamAddUser> userList;

    private Long nodeId;

    private String staff;

    private Long tenantId;

    @Data
    public static class IamAddUser {
        private Long userId;
        private String userName;
        private List<Long> userRoles;
    }

}

