package com.ctg.cicd.common.model.vo;

import lombok.Data;

import java.util.List;
@Data
public class SettingsRoleFuncAddVo {

    private Long roleId;
    private List<Long> funcIds;

}
