package com.ctg.cicd.config.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 流水线信息表
 * </p>
 *
 * @author 作者
 * @since 2023-05-30
 */
@Getter
@Setter
@TableName("cicd_pipeline")
public class Pipeline{

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 流水线uuid
     */
    private String pipelineUuid;

    /**
     * 节点ID
     */
    private Long nodeId;

    /**
     * 节点uuid
     */
    private String ndoeUuid;

    /**
     * 租户ID
     */
    private Long tenantId;

    private Integer userId;

    /**
     * 创建者：对应username
     */
    private String createBy;

    private Date createTime;

    /**
     * 最近一次修改者：对应username
     */
    private String lastUpdateBy;

    /**
     * 流水线名称
     */
    private String pipelineName;

    /**
     * 流水线模板ID
     */
    private Long templateId;

    /**
     * 流水线环境
     */
    private String pipelineEnv;

    /**
     * 流水线类型（服务流水线、项目流水线）
     */
    private String pipelineType;

    /**
     * 最近一次执行流水线ID
     */
    private String lastExecutionId;

    /**
     * 最近一次运行时间
     */
    private Date lastExecutionTime;

    /**
     * 最近一次运行状态
     */
    private String lastExecutionStatus;


}
