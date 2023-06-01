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
@TableName("cicd_settings_credential_code")
public class SettingsCredentialCode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String uuid;

    /**
     * 证书名称
     */
    private String credentialName;

    /**
     * 证书类型
     */
    private String authType;

    /**
     * 授权账号
     */
    private String authAccount;

    /**
     * 令牌或密码
     */
    private String authPassword;

    /**
     * 节点ID
     */
    private Long nodeRootId;

    private Long tenantId;

    /**
     * 是否删除
     */
    private Integer deleted;

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
