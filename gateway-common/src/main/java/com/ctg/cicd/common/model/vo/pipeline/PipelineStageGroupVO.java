package com.ctg.cicd.common.model.vo.pipeline;

import lombok.Data;

import java.util.List;

/**
 * @author huangZhi
 * @date 2023/05/31 15:16
 **/
@Data
public class PipelineStageGroupVO {

    /**
     * 阶段序号
     */
    private Integer stageOrder;

    /**
     * 分组序号
     */
    private Integer groupOrder;

    private List<PipelineStageGroupTaskVO> taskList;

}