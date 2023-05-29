package com.ctg.pipeline.execute.model;

import lombok.Data;

/**
 * @author zhiHuang
 * @date 2022/11/14 22:03
 **/
@Data
public class SystemNotification {
    private Long createdAt;
    private String group;
    private String message;
    private Boolean closed;

}