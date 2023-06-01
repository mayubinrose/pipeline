package com.ctg.cicd.common.model.dto;

import lombok.Data;


/**
 * @author hel
 * @date 2023/5/30
 */
@Data
public class VpcInfoDTO {

    private Long vpcId;

    private String vpcName;

    private Integer status;

}
