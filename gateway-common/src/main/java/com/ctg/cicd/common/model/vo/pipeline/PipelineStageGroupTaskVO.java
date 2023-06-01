package com.ctg.cicd.common.model.vo.pipeline;

import cn.hutool.json.JSONObject;
import lombok.Data;

import java.util.Date;

/**
 * @author huangZhi
 * @date 2023/05/31 15:17
 **/
@Data
public class PipelineStageGroupTaskVO {
    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 任务标签
     */
    private String taskLabel;

    /**
     * 任务序号
     */
    private Integer taskOrder;

    /**
     * 构建集群ID
     */
    private Integer buildClusterId;

    /**
     * 阶段附属信息ID
     */
    private Long taskExtinfoId;

    /**
     * 阶段配置ID
     */
    private Long taskConfigId;

    // ==============entity之外的属性

    private JSONObject taskExtinfo = new JSONObject();

    private JSONObject taskConfig = new JSONObject();
    private String latestExecutionId;

    private String latestExecutionStatus;

    private Date latestExecutionTime;

    private String latestExecutionBy;
}