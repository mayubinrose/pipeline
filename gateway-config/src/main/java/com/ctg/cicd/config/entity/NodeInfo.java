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
@TableName("cicd_node_info")
public class NodeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 节点uuid
     */
    private String nodeUuid;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 节点类型
     */
    private String nodeType;

    /**
     * 节点标签
     */
    private String nodeLabel;

    /**
     * 节点描述
     */
    private String description;

    /**
     * 节点创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除(0-否,1-是)
     */
    private Boolean deleted;

    /**
     * 父节点的id（父节点为-1表示当前为根节点）
     */
    private Long parentId;

    /**
     * 租户的id
     */
    private Long tenantId;

    /**
     * 是否要继承父节点的角色成员(0-否,1-是)
     */
    private Boolean parentRoleExtends;


}
