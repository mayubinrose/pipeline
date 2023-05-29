package com.ctg.pipeline.common.model.pipeline.config;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 应用配置类
 *
 * @author zhiHuang
 * @date 2022/11/13 16:29
 **/
@Data
public class ApplicationConfig implements Serializable {
    private Long id;
    private String application;
    private String description;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private boolean deleted; //是否删除(0-否,1-是)

    public ApplicationConfig(String application, String description, String updateBy) {
        this.application = application;
        this.description = description;
        this.updateBy = updateBy;
    }

    public ApplicationConfig() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}