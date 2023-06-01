package com.ctg.cicd.common.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.codehaus.plexus.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author hel
 * @date 2023/05/29
 **/
@Data
public class NodeUserRoleUpdateVO {


    @NotNull(message = "用户id不能为空")
    private Long userId;

    @NotNull(message = "用户名不能为空")
    private String userName;

    @NotNull(message = "节点id不能为空")
    private Long nodeId;

    @NotNull(message = "角色列表不能为空")
    private List<Long> roles;

    private String staff;

    private Long tenantId;
}