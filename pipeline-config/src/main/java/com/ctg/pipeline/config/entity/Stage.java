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
@TableName("p_ctg_stage")
public class Stage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 类型
     */
    private String type;

    /**
     * 阶段名称
     */
    private String name;

    /**
     * 阶段配置
     */
    private String config;

    /**
     * 运行依赖的外部stage id集合
     */
    private String requisiteStagerefIds;

    /**
     * 阶段顺序
     */
    private Integer stageOrder;

    /**
     * 分组顺序
     */
    private Integer groupOrder;

    /**
     * 任务顺序
     */
    private Integer taskOrder;

    /**
     * 流水线配置id
     */
    private Long pipelineConfigId;


}
