package com.ctg.cicd.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SaveEntityReturnDTO {

    private Long id;

    private String uuid;
}
