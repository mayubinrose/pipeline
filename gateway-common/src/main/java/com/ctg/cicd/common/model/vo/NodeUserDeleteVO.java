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
public class NodeUserDeleteVO {

    /**
     * 用户集合
     */
    @Valid
    @NotEmpty(message = "删除应用成员集合不能为空")
    private List<UserVO> userList;

    private String staff;

    private Long tenantId;

    private Long nodeId;

    @Data
    public static class UserVO {
        @NotNull(message = "用户ID不能为空")
        private Long userId;
        @NotNull(message = "用户名称不能为空")
        private String userName;
    }
}

