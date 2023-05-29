package com.ctg.cicd.common.model.vo;

import lombok.Data;

@Data
public class SettingsRoleAddVo {
    private String roleName;
    private String roleDescription;
    private Integer roleStatus;
    private Long nodeRootId;
}
