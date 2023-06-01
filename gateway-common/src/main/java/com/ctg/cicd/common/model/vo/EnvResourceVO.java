package com.ctg.cicd.common.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.annotation.sql.DataSourceDefinition;
import java.util.Date;

/**
 * @author hel
 * @date 2023/5/30
 */
@Data
public class EnvResourceVO {

    @ApiModelProperty("环境ID")
    private Long envId;

    @ApiModelProperty("租户")
    private Long tenantId;

    @ApiModelProperty("资源实例ID")
    private String instId;

    @ApiModelProperty("资源名称")
    private String resourceName;

    @ApiModelProperty("资源池ID")
    private Long resourcePoolId;

    @ApiModelProperty("资源池编码")
    private String resourcePoolCode;

    @ApiModelProperty("命名空间实例ID")
    private String namespaceInstId;

    @ApiModelProperty("命名空间")
    private String namespace;

    @ApiModelProperty("产品名称")
    private String spuName;

    @ApiModelProperty("规格")
    private String spec;

    @ApiModelProperty("IP")
    private String ip;

    @ApiModelProperty("端口")
    private String port;

    @ApiModelProperty("订购时间")
    private Date orderTime;

    @ApiModelProperty("实例数")
    private Integer instCount;

    @ApiModelProperty("状态")
    private String statusCd;

    @ApiModelProperty("状态名称")
    private String statusCdName;

    @ApiModelProperty("资源大类（基础资源，可选资源）")
    private String mainCategory;

    @ApiModelProperty("资源小类（dcos，ecs，msercm）")
    private String subCategory;

    @ApiModelProperty("控制台地址")
    private String apprUrl;

    @ApiModelProperty("资源版本")
    private String version;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;
}
