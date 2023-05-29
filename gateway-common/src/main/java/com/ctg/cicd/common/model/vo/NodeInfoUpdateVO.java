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
public class NodeInfoUpdateVO {

    @ApiModelProperty(value = "节点id", required = true)
    @NotNull(message = "节点id不能为空")
    private Long id;

    private String nodeName;

    private Boolean parentRoleExtends;

    private String nodeLabel;

    private String description;
}
