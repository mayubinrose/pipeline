package com.ctg.cicd.common.model.vo.pipeline;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

/**
 * @author huangZhi
 * @date 2023/05/31 15:16
 **/
@Data
public class PipelineStageVO {

    private String pipelineUuid;
    /**
     * 阶段名称
     */
    private String stageName;

    /**
     * 阶段序号
     */
    private Integer stageOrder;

    private List<PipelineStageGroupVO> groupList;
}