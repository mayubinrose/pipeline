package com.ctg.cicd.common.model.dto;


import lombok.Data;
import lombok.ToString;

@Data
public class UserInfoDTO {
    private Long userId;

    private String userName;

    private String realName;

    private String email;

    private Long tenantId;

}
