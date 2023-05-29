package com.ctg.pipeline.common.model.pipeline.config;

import com.ctg.pipeline.common.model.execute.ExecuteTrigger;
import com.ctg.pipeline.common.util.CollectionUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 流水线配置
 *
 * @author zhiHuang
 * @date 2022/11/13 10:45
 **/
public class PipelineConfig implements Serializable {
    
    // 数据 demo
    //{"keepWaitingPipelines":false,"parameterConfig":[{"default":"master","description":"分支","name":"branch"},{"description":"名称","name":"name"}],"lastModifiedBy":"anonymous","description":"流水线描述","index":0,"triggers":[],"limitConcurrent":true,"application":"app-gateway","parallel":true,"name":"pipeline001","stages":[{"name":"编译","refId":"1","requisiteStageRefIds":[],"type":"build"},{"name":"构建","refId":"2","requisiteStageRefIds":["1"],"type":"ci"}],"id":"46710a91-8f9a-4ef3-bf30-b6a40e634462","updateTs":"1668269710137","notifications":[{"address":"http://eeeee.com","level":"pipeline","type":"webhook","when":["pipeline.complete","pipeline.failed"]}]}

    private Long id;
    private String application;
    private String name;
    private String description;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;

    private List<StageConfig> stages;
    private List<TriggerConfig> triggers;
    private List<ParameterConfig> parameterConfig;
    private List<NotificationConfig> notifications;

    private ExecuteTrigger trigger; //运行时动态传参

    /**
     * 根据trigger内参数内容 校验parameterConfig内是否有必填值未填
     */
    public void setTriggerAndCheckParameter(ExecuteTrigger trigger){
        this.trigger=trigger;
        if(CollectionUtils.isNotEmpty(parameterConfig)){


        }
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

    public List<StageConfig> getStages() {
        return stages;
    }

    public void setStages(List<StageConfig> stages) {
        this.stages = stages;
    }

    public List<TriggerConfig> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<TriggerConfig> triggers) {
        this.triggers = triggers;
    }

    public List<ParameterConfig> getParameterConfig() {
        return parameterConfig;
    }

    public void setParameterConfig(List<ParameterConfig> parameterConfig) {
        this.parameterConfig = parameterConfig;
    }

    public List<NotificationConfig> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationConfig> notifications) {
        this.notifications = notifications;
    }

    public ExecuteTrigger getTrigger() {
        return trigger;
    }

    public void setTrigger(ExecuteTrigger trigger) {
        this.trigger = trigger;
    }
}