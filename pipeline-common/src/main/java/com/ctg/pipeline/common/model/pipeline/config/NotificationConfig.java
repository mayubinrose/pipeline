package com.ctg.pipeline.common.model.pipeline.config;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * 回调配置
 *
 * @author zhiHuang
 * @date 2022/11/13 11:01
 **/
public class NotificationConfig implements Serializable {

    private String level; //PIPELINE STAGE TASK
    private String type ; //webhook、email
    private String[] when; //["pipeline.complete","pipeline.failed"]
    private String address; //地址
    @JsonIgnore
    private String occasion; //pipeline.complete,pipeline.failed

    private String contentTemplate; //通知内容模板

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getWhen() {
        return when;
    }

    public void setWhen(String[] when) {
        this.when = when;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContentTemplate() {
        return contentTemplate;
    }

    public void setContentTemplate(String contentTemplate) {
        this.contentTemplate = contentTemplate;
    }

    public String getOccasion() {
        return occasion;
    }
    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }
}