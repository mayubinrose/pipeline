package com.ctg.pipeline.common.model.pipeline.config;

import java.io.Serializable;

/**
 * 流水线配置触发器--高级特性
 *
 * @author zhiHuang
 * @date 2022/11/13 10:56
 **/
public class TriggerConfig implements Serializable {

    private String type; // pipeline、cron

    private String config;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}