package com.ctg.cicd.common.model.dto;

import com.ctg.eadp.common.dto.portal.TopologyInstInfoDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class EnvResourceDTO {

    @ApiModelProperty("主键")
    private Long id;

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

    @ApiModelProperty("集群下命名空间列表")
    List<TopologyInstInfoDTO> namespaceList;

    @ApiModelProperty("资源版本")
    private String version;

    @ApiModelProperty("是否开启服务网格")
    private Boolean openIstio = false;

    @ApiModelProperty("是否已授权CCSE")
    private Boolean hadDataAuthority = false;
}