package com.ctg.pipeline.common.model.pipeline.config;

import java.util.Arrays;

/**
 * 阶段配置
 *
 * @author zhiHuang
 * @date 2022/11/13 11:13
 **/
public class StageConfig {

    private String name;
    private Long id;
    private String type;

    private Object config;
    private String[] requisiteStageRefIds;

    private int stageOrder;

    private int groupOrder;

    private int taskOrder;

    private String refId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getConfig() {
        return config;
    }

    public void setConfig(Object config) {
        this.config = config;
    }

    public String[] getRequisiteStageRefIds() {
        return requisiteStageRefIds;
    }

    public void setRequisiteStageRefIds(String[] requisiteStageRefIds) {
        this.requisiteStageRefIds = requisiteStageRefIds;
    }

    public int getStageOrder() {
        return stageOrder;
    }

    public void setStageOrder(int stageOrder) {
        this.stageOrder = stageOrder;
    }

    public int getGroupOrder() {
        return groupOrder;
    }

    public void setGroupOrder(int groupOrder) {
        this.groupOrder = groupOrder;
    }

    public int getTaskOrder() {
        return taskOrder;
    }

    public void setTaskOrder(int taskOrder) {
        this.taskOrder = taskOrder;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    @Override
    public String toString() {
        return "StageConfig{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", requisiteStageRefIds=" + Arrays.toString(requisiteStageRefIds) +
                ", stageOrder=" + stageOrder +
                ", groupOrder=" + groupOrder +
                ", taskOrder=" + taskOrder +
                ", refId='" + refId + '\'' +
                '}';
    }
}