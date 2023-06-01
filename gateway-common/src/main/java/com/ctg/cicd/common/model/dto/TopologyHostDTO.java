package com.ctg.cicd.common.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Created by fnshionfx on 2020/5/21.
 */
@ApiModel("拓扑主机DTO")
@Getter
@Setter
public class TopologyHostDTO {

    @ApiModelProperty("是否动态创建，0-否 1-是")
    private Byte autoCreated;

    @ApiModelProperty("可用CPU核数")
    private Long availableCpu;

    @ApiModelProperty("可用磁盘")
    private Long availableDiskSpace;

    @ApiModelProperty("可用内存")
    private Long availableMemory;

    @ApiModelProperty("主机关联的业务全路径")
    private String bizFullName;

    @ApiModelProperty("主机关联的业务名称")
    private String bizName;

    @ApiModelProperty("CPU")
    private Long cpu;

    @ApiModelProperty("磁盘")
    private Long diskSpace;

    @ApiModelProperty("主机别名")
    private String hostAlias;

    @ApiModelProperty("主机编码")
    private String hostCode;

    @ApiModelProperty("主机描述")
    private String hostDesc;

    @ApiModelProperty("主机标识")
    private Long hostId;

    @ApiModelProperty("ssh的IP")
    private String hostIp;

    @ApiModelProperty("IPV6")
    private String hostIpV6;

    @ApiModelProperty("主机IP列表")
    private List<HostIpDTO> hostIps;

    @ApiModelProperty("机型标识")
    private Long hostModelId;

    @ApiModelProperty("主机名称")
    private String hostName;

    @ApiModelProperty("主机类型，0-虚拟机 1-物理机 2-容器")
    private Byte machine;

    @ApiModelProperty("主机规格映射标识")
    private Long mappingId;

    @ApiModelProperty("内存")
    private Long memory;

    @ApiModelProperty("占用状态，0-空闲 1-已实占 2-已废弃 3-已预占 4-待回收 5-已使用 默认空闲")
    private Byte occupyStatus;

    @ApiModelProperty("订购时间")
    private Date orderTime;

    @ApiModelProperty("操作系统类型")
    private String osType;

    @ApiModelProperty("操作系统")
    private String osVersion;

    @ApiModelProperty("宿主主机标识")
    private Long parentHostId;

    @ApiModelProperty("资源池编码")
    private String poolCode;

    @ApiModelProperty("产品镜像标识")
    private Long prodImageId;

    @ApiModelProperty("资源准备状态 0-未初始化 1-已初始化 2-正在初始化")
    private Byte readyStatus;

    @ApiModelProperty("创建时间")
    private Long createdTime;

    @ApiModelProperty("标签标识")
    private Long resLabelId;

    @ApiModelProperty("标签名称")
    private String resLabelName;

    @ApiModelProperty("标签列表")
    private List<ResourceLabelDTO> resLabels;

    @ApiModelProperty("节点全名称")
    private String resNodeFullName;

    @ApiModelProperty("节点标识")
    private Long resNodeId;

    @ApiModelProperty("节点名称")
    private String resNodeName;

    @ApiModelProperty("资源池标识")
    private Long resPoolId;

    @ApiModelProperty("资源池名称")
    private String resPoolName;

    @ApiModelProperty("销售状态")
    private String saleStatus;

    @ApiModelProperty("主机规格所属分类目录名称")
    private String specCatName;

    @ApiModelProperty("主机规格编码")
    private String specCode;

    @ApiModelProperty("主机规格名称")
    private String specName;

    @ApiModelProperty("主机规格使用场景")
    private String specUseScene;

    @ApiModelProperty("ssh账号")
    private String sshAccount;

    @ApiModelProperty("ssh密码")
    private String sshPasswd;

    @ApiModelProperty("ssh端口")
    private Integer sshPort;

    @ApiModelProperty("状态 0-无效 1-有效 2-删除")
    private Byte status;

    @ApiModelProperty("状态备注")
    private String statusMemo;

    @ApiModelProperty("租户标识")
    private Long tenantId;

    @ApiModelProperty("业务拓扑树标识")
    private Long topologyId;

    @ApiModelProperty("使用范围")
    private String useScope;

    @ApiModelProperty("最近版本升级时间")
    private Date versionTime;

    @ApiModelProperty("VPC编码")
    private String vpcCode;

    @ApiModelProperty("VPC标识")
    private Long vpcId;

    @ApiModelProperty("VPC名称")
    private String vpcName;

    @ApiModelProperty("VPC状态")
    private String vpcStatus;

    @Getter
    @Setter
    @ApiModel("主机IP")
    public static class HostIpDTO {

        @ApiModelProperty("创建人")
        private Long createdBy;

        @ApiModelProperty("创建时间")
        private Date createdTime;

        @ApiModelProperty("主机IP")
        private String hostIp;

        @ApiModelProperty("主机IP的ID")
        private Long hostIpsId;

        @ApiModelProperty("IP类型 1-ipv4 2-ipv6")
        private Byte ipType;

        @ApiModelProperty("修改人")
        private Long modifiedBy;

        @ApiModelProperty("修改时间")
        private Date modifiedTime;

        @ApiModelProperty("IP网络标识 0-通用 1-内网IP 2-公网IP")
        private Byte netFlag;

        @ApiModelProperty("IP网络标识名称")
        private String netFlagName;

        @ApiModelProperty("是否实IP 1-实IP 0-虚IP")
        private Byte relFlag;

        @ApiModelProperty("状态 0-无效 1-有效 2-删除")
        private Byte status;

        @ApiModelProperty("租户主机ID")
        private Long teHostId;
    }

    @Getter
    @Setter
    @ApiModel("资源标签")
    private static class ResourceLabelDTO {

        @ApiModelProperty("标签类别名称")
        private String catName;

        @ApiModelProperty("创建人")
        private Long createdBy;

        @ApiModelProperty("创建时间")
        private Date createdTime;

        @ApiModelProperty("主机标识")
        private Long hostId;

        @ApiModelProperty("关联主机数")
        private int hostNum;

        @ApiModelProperty("修改人")
        private Long modifiedBy;

        @ApiModelProperty("修改时间")
        private Date modifiedTime;

        @ApiModelProperty("模块标识")
        private Long moduleId;

        @ApiModelProperty("标签类别ID")
        private Long resLabelCatId;

        @ApiModelProperty("资源标签编码")
        private String resLabelCode;

        @ApiModelProperty("资源标签描述")
        private String resLabelDesc;

        @ApiModelProperty("资源标签标识")
        private Long resLabelId;

        @ApiModelProperty("资源标签名称")
        private String resLabelName;

        @ApiModelProperty("资源标签规则")
        private List<ResourceLabelRuleDTO> resLabelRuleList;

        @ApiModelProperty("状态 0-无效 1-有效 2-删除")
        private Byte status;

        @ApiModelProperty("租户标识")
        private Long tenantId;
    }

    @Getter
    @Setter
    @ApiModel("资源标签规则")
    private static class ResourceLabelRuleDTO {

        @ApiModelProperty("创建人")
        private Long createdBy;

        @ApiModelProperty("创建时间")
        private Date createdTime;

        @ApiModelProperty("资源标签标识")
        private Long resLabelId;

        @ApiModelProperty("资源标签规则编码，内存-MEM CPU-CPU 磁盘-DISK")
        private String ruleCode;

        @ApiModelProperty("标签规则标识")
        private Long ruleId;

        @ApiModelProperty("资源标签规则操作符，>-大于 =-大于等于")
        private String ruleOps;

        @ApiModelProperty("标签规则比较值，内存单位为G,CPU单位为核，磁盘单位为G")
        private String ruleValue;
    }

}
