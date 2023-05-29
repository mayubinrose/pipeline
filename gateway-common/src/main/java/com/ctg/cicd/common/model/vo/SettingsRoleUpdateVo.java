package com.ctg.cicd.common.model.vo;

import lombok.Data;

@Data
public class SettingsRoleUpdateVo {
    private Long roleId;
    private String roleName;
    private String roleDescription;
    private Integer roleStatus;
    // 前端不传，根节点id
    private Long nodeRootId;
}
