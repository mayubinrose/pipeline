package com.ctg.cicd.common.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author hel
 * @date 2023/5/18
 */
@Data
public class NodeInfoAddVO {

    @ApiModelProperty(value = "节点名称", required = true)
    @NotBlank(message = "节点名称不能为空")
    private String nodeName;

    @ApiModelProperty(value = "节点类型", required = true)
    @NotBlank(message = "节点类型不能为空")
    private String nodeType;

    @ApiModelProperty(value = "父节点id", required = true)
    @NotNull(message = "父节点id不能为空")
    private Long parentId;

    @ApiModelProperty(value = "是否继承父节点权限（1:是,2:否）", required = true)
    @NotNull(message = "节点类型不能为空")
    private Boolean parentRoleExtends;

    private String nodeLabel;

    private String description;








}
