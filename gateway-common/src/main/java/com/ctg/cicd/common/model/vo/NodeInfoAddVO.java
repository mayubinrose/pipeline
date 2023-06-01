package com.ctg.cicd.common.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author hel
 * @date 2023/5/18
 */
@Data
public class NodeInfoAddVO {

    @ApiModelProperty(value = "节点名称", required = true)
    @NotBlank(message = "节点名称不能为空")
    private String nodeName;

    @ApiModelProperty(value = "编码", required = true)
    @NotBlank(message = "项目编码")
    @Length(min = 3, max = 30, message = "项目编码长度不能低于3个字符，不能超过30个字符")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_-]{2,29}$", message = "项目编码必须以字母开头，后面可以是字母、数字、下划线或短横线，长度在3到30之间")
    private String nodeCode;

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
