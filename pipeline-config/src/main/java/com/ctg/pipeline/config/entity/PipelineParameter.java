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
@TableName("p_cfg_pipeline_parameter")
public class PipelineParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 参数名称
     */
    private String name;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 描述
     */
    private String description;

    /**
     * 参数输入来源：用户输入、自动传参等
     */
    private String source;

    /**
     * 流水线配置id
     */
    private Long pipelineConfigId;


}
