package com.ctg.cicd.common.model.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SettingsRoleDTO {
    private List<Long> funcIds;
    private Long id;

    /**
     * 角色uuid
     */
    private String roleUuid;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色英文简称
     */
    private String roleCode;

    /**
     * 角色权限的描述
     */
    private String roleDescription;

    /**
     * 创建角色者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 角色修改时间
     */
    private Date updateTime;

    /**
     * 角色状态(0-否,1-是)
     */
    private Integer roleStatus;

    /**
     * 关联的根节点的id
     */
    private Long nodeRootId;
}
