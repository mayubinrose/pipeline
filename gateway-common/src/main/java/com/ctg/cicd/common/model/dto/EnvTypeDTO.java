package com.ctg.cicd.common.model.dto;

import lombok.Data;

/**
 * @author hel
 * @date 2023/5/30
 */
@Data
public class EnvTypeDTO {
    private String envType;

    private String envTypeName;

    public EnvTypeDTO() {}

    public EnvTypeDTO(String envType, String envTypeName) {
        this.envType = envType;
        this.envTypeName = envTypeName;
    }
}

