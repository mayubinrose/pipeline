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
@TableName("cicd_pipeline_stage_group")
public class PipelineStageGroup implements Serializable {

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

    /**
     * 阶段序号
     */
    private Integer stageOrder;

    /**
     * 分组序号
     */
    private Integer groupOrder;

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


}
