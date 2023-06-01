package com.ctg.cicd.config.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 环境资源关联表
 * </p>
 *
 * @author hel
 * @since 2023-05-30
 */
@Data
@TableName("cicd_env_resource")
public class EnvResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 环境ID
     */
    private Long envId;

    /**
     * 资源实例ID
     */
    private String instId;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源池ID
     */
    private Long resourcePoolId;

    /**
     * 资源池编码
     */
    private String resourcePoolCode;

    /**
     * 命名空间实例ID
     */
    private String namespaceInstId;

    /**
     * 命名空间
     */
    private String namespace;

    /**
     * 产品名称
     */
    private String spuName;

    /**
     * 规格
     */
    private String spec;

    /**
     * IP
     */
    private String ip;

    /**
     * 订购时间
     */
    private Date orderTime;

    /**
     * 实例数
     */
    private Integer instCount;

    /**
     * 状态
     */
    private String statusCd;

    /**
     * 资源大类（基础资源，可选资源）
     */
    private String mainCategory;

    /**
     * 资源小类（k8s，ECS，注册中心，数据库）
     */
    private String subCategory;

    /**
     * 控制台地址
     */
    private String apprUrl;

    /**
     * 资源版本
     */
    @Version
    private String version;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 租户
     */
    private Long tenantId;


}
