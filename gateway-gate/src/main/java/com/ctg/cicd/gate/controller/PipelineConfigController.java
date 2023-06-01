package com.ctg.cicd.gate.controller;

import cn.hutool.json.JSONObject;
import com.ctg.cicd.common.model.vo.pipeline.PipelineStageGroupTaskVO;
import com.ctg.cicd.common.model.vo.pipeline.PipelineStageGroupVO;
import com.ctg.cicd.common.model.vo.pipeline.PipelineStageVO;
import com.ctg.cicd.common.model.vo.pipeline.PipelineVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author huangZhi
 * @date 2023/05/30 09:25
 **/
@Api(tags = "配置-流水线")
@Slf4j
@RestController
@RequestMapping("/api/v1/cicd/pipeline/config")
public class PipelineConfigController {


    @ApiOperation("保存流水线")
    @PostMapping("/save")
    public PipelineVO savePipeline(PipelineVO pipelineVO) {
        return null;
    }


    @ApiOperation("查询流水线详情")
    @GetMapping("/get/{pipelineUuid}")
    public PipelineVO getPipeline(@PathVariable("pipelineUuid") String pipelineUuid) {
        PipelineVO pipelineVO = new PipelineVO();
        pipelineVO.setPipelineUuid(pipelineUuid);
        pipelineVO.setTenantId(123L);
        pipelineVO.setNdoeUuid("XXXXXXXX");
        pipelineVO.setCreateBy("huangz@1111.com");
        pipelineVO.setCreateTime(new Date());
        pipelineVO.setTemplateId(789L);

        pipelineVO.setPipelineName("流水线demo1");
        pipelineVO.setCreateTime(new Date());
        pipelineVO.setPipelineType("service");
        List<PipelineStageVO> stageList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            PipelineStageVO stageVO = new PipelineStageVO();
            stageVO.setPipelineUuid(pipelineUuid);
            stageVO.setStageName("阶段-" + i);
            stageVO.setStageOrder(i);
            List<PipelineStageGroupVO> groupList = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                PipelineStageGroupVO groupVO = new PipelineStageGroupVO();
                groupVO.setStageOrder(i);
                groupVO.setGroupOrder(j);
                List<PipelineStageGroupTaskVO> taskVOList = new ArrayList<>();
                if (j == 0) {
                    PipelineStageGroupTaskVO task = new PipelineStageGroupTaskVO();
                    task.setTaskType("SOURCE");
                    task.setTaskLabel("CODE");
                    task.setLatestExecutionStatus("SUCCESS");
                    task.setLatestExecutionBy("XXX");
                    task.setLatestExecutionId("XXXXXXXXXX");
                    taskVOList.add(task);
                }
                if (j == 1) {
                    PipelineStageGroupTaskVO task = new PipelineStageGroupTaskVO();
                    task.setTaskType("BUILD");
                    task.setTaskLabel("JAVA");
                    task.setLatestExecutionStatus("SUCCESS");
                    task.setLatestExecutionBy("XXX");
                    taskVOList.add(task);
                    PipelineStageGroupTaskVO task2 = new PipelineStageGroupTaskVO();
                    task2.setTaskType("BUILD");
                    task2.setTaskLabel("NODE");
                    task2.setLatestExecutionId("XXXXX");
                    task2.setLatestExecutionStatus("SUCCESS");
                    task2.setLatestExecutionBy("XXX2");
                    taskVOList.add(task2);
                }
                groupVO.setTaskList(taskVOList);
                groupList.add(groupVO);
            }
            stageVO.setGroupList(groupList);
            stageList.add(stageVO);
        }
        pipelineVO.setLastExecutionStatus("SUCCESS");
        pipelineVO.setStageList(stageList);
        pipelineVO.setNodeId(2L);
        return pipelineVO;

    }

}