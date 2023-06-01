package com.ctg.cicd.config.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 环境表
 * </p>
 *
 * @author hel
 * @since 2023-05-30
 */
@Data
@TableName("cicd_env")
public class Env implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * uuid
     */
    private String envUuid;

    /**
     * 环境名称
     */
    private String envName;

    /**
     * 环境编码
     */
    private String envCode;

    /**
     * 环境类型
     */
    private String envType;

    /**
     * 环境类型名称
     */
    private String envTypeName;

    /**
     * 资源池ID
     */
    private Long resPoolId;

    /**
     * 资源池名称
     */
    private String resPoolName;

    /**
     * 资源池编码
     */
    private String resPoolCode;

    /**
     * 节点id
     */
    private Long nodeId;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * vpcId
     */
    private Long vpcId;

    /**
     * vpc名称
     */
    private String vpcName;

    /**
     * 是否可以容器部署  0 否 1 是
     */
    private Boolean dockerDeploy;

    /**
     * 是否可以主机部署  0 否 1 是
     */
    private Boolean hostDeploy;

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

    private Long tenantId;


}
