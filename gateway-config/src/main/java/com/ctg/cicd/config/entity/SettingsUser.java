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
@TableName("cicd_settings_user")
public class SettingsUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
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


    /**
     * 创建的人
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
     * 修改时间
     */
    private Date updateTime;


}
