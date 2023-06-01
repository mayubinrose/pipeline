package com.ctg.cicd.common.model.dto;

import com.ctg.eadp.common.dto.portal.InstAttrsDTO;
import com.ctg.eadp.common.dto.portal.InstHostresDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;

/**
 * Created by fnshionfx on 2020/5/21.
 */
@ApiModel("拓扑实例DTO")
@Getter
@Setter
@ToString
public class TopologyInstInfoDTO {

    @ApiModelProperty("实例ID")
    private String spuInstId;

    @ApiModelProperty("实例名称")
    private String spuInstName;

    @ApiModelProperty("组件编码")
    private String spuCode;
    @ApiModelProperty("组件名称")
    private String spuName;

    @ApiModelProperty("产品属性")
    private HashMap<String,Object> productProperty;

    @ApiModelProperty("创建时间")
    private String createdTime;
    @ApiModelProperty("过期时间")
    private String expTime;

    @ApiModelProperty("应用编码")
    private String appCode;

    @ApiModelProperty("系列")
    private String seriesName;
    @ApiModelProperty("版本号")
    private String innerVersionNo;

    @ApiModelProperty("部署主机数")
    private String deployHostCount;

    @ApiModelProperty("控制台地址")
    private String appUrl;

    @ApiModelProperty("资源池ID")
    private Long resPoolId;

    @ApiModelProperty("资源池编码")
    private String resPoolCode;

    @ApiModelProperty("资源池名称")
    private String resPoolName;

    @ApiModelProperty("内部版本号")
    private String innerVersionNO;

    @ApiModelProperty("内核版本号")
    private String kernelVersionNO;

    @ApiModelProperty("运行状态编码(0:未知,1:运行中,2:已停止,3:已注销)")
    private int bizState;
    @ApiModelProperty("运行状态名称")
    private String runningState;

    @ApiModelProperty("规格名称")
    private String specificationName;

    @ApiModelProperty("父实例ID")
    private String parSpuInstId;

    @ApiModelProperty("VPC网络信息")
    private VpcDetail vpcDetail;

    private Long tenantId;

    private Long crtUserId;

    private String crtUserName;

    private String skuName;

    private List<InstHostresDTO> paasInstHostress;

    private List<InstAttrsDTO> paasInstAttrs;


    @ApiModel("VPC网络信息")
    @Getter
    @Setter
    public static class VpcDetail {

        @ApiModelProperty("VPC ID")
        private Integer vpcId;

        @ApiModelProperty("VPC编码")
        private String vpcCode;
        @ApiModelProperty("VPC名称")
        private String vpcName;

        @ApiModelProperty("虚拟路由ID")
        private int virtualRouterId;

    }


}
