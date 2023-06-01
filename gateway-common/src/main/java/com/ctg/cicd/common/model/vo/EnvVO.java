package com.ctg.cicd.common.model.vo;

import com.ctg.cicd.common.model.dto.EnvDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author hel
 * @date 2023/5/30
 */
@Data
public class EnvVO {

    @ApiModelProperty("环境名称")
    private Long id;
    private String envUuid;
    @ApiModelProperty("环境名称")
    @NotNull(message = "环境名称不能为空")
    private String envName;
    @ApiModelProperty("环境编码")
    @NotNull(message = "环境编码不能为空")
    private String envCode;
    @ApiModelProperty("环境类型")
    @NotNull(message = "环境类型不能为空")
    private String envType;
    @ApiModelProperty("资源池ID")
    private String resPoolId;
    @ApiModelProperty("VPCID")
    @NotNull(message = "vpcID不能为空")
    private Long vpcId;
    @ApiModelProperty("VPC名称")
    @NotNull(message = "vpc名称不能为空")
    private String vpcName;
    @ApiModelProperty("资源池名称")
    private String resPoolName;
    @ApiModelProperty("资源池编码")
    private String resPoolCode;
    @ApiModelProperty("节点ID")
    private Long nodeId;
    @ApiModelProperty("环境类型名称")
    private String envTypeName;
    @ApiModelProperty("服务数")
    private int serviceNum;

    private Date createTime;

    @ApiModelProperty("基础资源")
    @NotEmpty(message = "基础资源不能为空")
    private List<EnvResourceVO> baseEnvResource;

    //可选资源数量
    private Integer baseEnvResourceCount = 0;

    @ApiModelProperty("可选资源")
    private List<EnvResourceVO> optionEnvResource;

    //可选资源数量
    private Integer optionEnvResourceCount = 0;

    public static EnvVO from(EnvDTO envDTO) {
        if (envDTO == null) {
            return null;
        }
        EnvVO envVO = new EnvVO();
        BeanUtils.copyProperties(envDTO, envVO);
        return envVO;
    }
}
