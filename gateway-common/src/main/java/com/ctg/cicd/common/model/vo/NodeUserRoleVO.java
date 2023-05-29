package com.ctg.cicd.common.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.codehaus.plexus.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 节点下角色成员列表
 *
 * @author huangZhi
 * @date 2023/05/18 16:59
 **/
@Data
public class NodeUserRoleVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 节点id
     */
    private Long nodeId;

    private String nodeName;

    private String nodePath;

    /**
     * 角色id集合
     */
    @JsonIgnore
    private String roleIds;
    
    private Long[] roleIdArray = new Long[0];


    private String roleNames;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * user_id对应的用户名
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
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;


    public Long[] getRoleIdArray() {
        if (StringUtils.isNotEmpty(roleIds)) {
            String[] roles = roleIds.split(",");
            Long[] roleIdArray = new Long[roles.length];
            for (int i = 0; i < roles.length; i++) {
                roleIdArray[i] = Long.parseLong(roles[i]);
            }
            return roleIdArray;
        }
        return roleIdArray;
    }
}