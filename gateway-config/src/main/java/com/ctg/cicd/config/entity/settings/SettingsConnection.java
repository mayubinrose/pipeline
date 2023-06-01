package com.ctg.cicd.config.entity.settings;

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
 * @author 作者
 * @since 2023-05-30
 */
@Getter
@Setter
@TableName("cicd_settings_connection")
public class SettingsConnection implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String uuid;

    /**
     * 连接名称
     */
    private String connectionName;

    /**
     * 连接类型
     */
    private String connectionType;

    /**
     * 证书ID
     */
    private Long credentialId;

    /**
     * 证书UUID
     */
    private String credentialUuid;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 根节点ID
     */
    private Long nodeRootId;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;


}
