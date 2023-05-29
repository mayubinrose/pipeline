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
@TableName("cicd_node_user_role")
public class NodeUserRole {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 节点id
     */
    private Long nodeId;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * user_id对应的用户名
     */
    private String userName;

    /**
     * 创建者
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
