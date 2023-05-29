package com.ctg.cicd.config.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author hel
 * @since 2023-05-17
 */
@Getter
@Setter
@TableName("cicd_settings_role")
public class SettingsRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
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
