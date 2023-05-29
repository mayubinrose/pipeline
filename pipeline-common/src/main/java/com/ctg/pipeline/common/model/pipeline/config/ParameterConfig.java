package com.ctg.pipeline.common.model.pipeline.config;

import java.io.Serializable;

/**
 * 参数化配置
 *
 * @author zhiHuang
 * @date 2022/11/13 10:57
 **/
public class ParameterConfig implements Serializable {
    private String name;
    private String description;
    private String defaultValue;
    private String source; //参数输入来源(用户输入、自动传参等)

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}