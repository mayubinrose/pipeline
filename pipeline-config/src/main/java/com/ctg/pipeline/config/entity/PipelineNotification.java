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
@TableName("p_cfg_pipeline_notification")
public class PipelineNotification implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 通知级别，当前默认为PIPELINE
     */
    private String level;

    /**
     * 通知类型，如webhook、email
     */
    private String type;

    /**
     * 通知时机，如"pipeline.complete"
     */
    private String occasion;

    /**
     * 通知地址
     */
    private String address;

    /**
     * 通知内容模板
     */
    private String contentTemplate;

    /**
     * 流水线配置id
     */
    private Long pipelineConfigId;



}
