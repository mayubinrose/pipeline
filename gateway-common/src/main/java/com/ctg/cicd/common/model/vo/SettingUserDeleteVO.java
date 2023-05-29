package com.ctg.cicd.common.model.vo;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author hel
 * @date 2023/5/18
 */
@Data
public class SettingUserDeleteVO {

    /**
     * 用户集合
     */
    @Valid
    @NotEmpty(message = "成员UUID不能为空")
    private String userUuid;

}

