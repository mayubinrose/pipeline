package com.ctg.cicd.config.entity.pipeline;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 流水线阶段信息表
 * </p>
 *
 * @author 作者
 * @since 2023-05-31
 */
@Getter
@Setter
@TableName("cicd_pipeline_stage_group_task")
public class PipelineStageGroupTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String pipelineUuid;

    private Long pipelineId;

    /**
     * 阶段ID
     */
    private Long stageId;

    private Long groupId;

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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private Date updateBy;


    private String latestExecutionId;

    private String latestExecutionStatus;

    private Date latestExecutionTime;

    private String latestExecutionBy;


}
