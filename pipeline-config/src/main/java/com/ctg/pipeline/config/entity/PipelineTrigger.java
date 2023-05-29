package com.ctg.pipeline.config.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author hel
 * @since 2022-11-14
 */
@Getter
@Setter
@TableName("p_cfg_pipeline_trigger")
public class PipelineTrigger implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 流水线配置id
     */
    private Long pipelineConfigId;

    /**
     * 触发器类型（流水线、cron表达式）
     */
    private String type;

    /**
     * 具体配置
     */
    private String config;


}
