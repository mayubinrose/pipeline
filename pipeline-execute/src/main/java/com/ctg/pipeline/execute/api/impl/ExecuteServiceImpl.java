package com.ctg.pipeline.execute.api.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ctg.pipeline.common.model.execute.ExecuteTrigger;
import com.ctg.pipeline.common.model.pipeline.config.PipelineConfig;
import com.ctg.pipeline.common.model.pipeline.config.StageConfig;
import com.ctg.pipeline.common.util.CollectionUtils;
import com.ctg.pipeline.execute.api.IExecuteService;
import com.ctg.pipeline.execute.model.Execution;
import com.ctg.pipeline.execute.pipeline.ExecutionLauncher;
import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository;
import com.ctg.pipeline.execute.queue.model.Trigger;
import com.ctg.pipeline.execute.util.CompoundExecutionOperator;
import com.ctg.pipeline.execute.util.ContextParameterProcessor;
import com.ctg.pipeline.execute.util.ExecutionRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.ctg.pipeline.execute.model.Execution.ExecutionType.PIPELINE;

/**
 * 执行业务实现类
 *
 * @author zhiHuang
 * @date 2022/11/12 16:57
 **/

@Service
@Slf4j
public class ExecuteServiceImpl implements IExecuteService {

    @Override
    public Map getTask(String id) {
        return null;
    }

    @Autowired
    ContextParameterProcessor contextParameterProcessor;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ExecutionLauncher executionLauncher;

    @Autowired
    ExecutionRepository executionRepository;

    @Autowired
    CompoundExecutionOperator executionOperator;
    @Autowired
    ExecutionRunner executionRunner;

    @Override
    public String startPipeline(PipelineConfig pipelineConfig) {
        initRequisiteStageRefIds(pipelineConfig.getStages());
        Map pipelineMap = parseAndValidatePipeline(pipelineConfig, true);
        Map processedPipeline = contextParameterProcessor.processPipeline(pipelineMap, initAugmentedContext(pipelineMap), false);
        processedPipeline.put("trigger", objectMapper.convertValue(processedPipeline.get("trigger"), Trigger.class));
        Execution execution = null;
        try {
            execution = executionLauncher.start(PIPELINE, objectMapper.writeValueAsString(processedPipeline));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return execution != null ? execution.getId() : "";
    }

    @Override
    public Execution getPipeline(String executionId) {
        return executionRepository.retrieve(PIPELINE, executionId);
    }

    @Override
    public void cancelPipeline(String executionId, String user, String reason) {
        executionOperator.cancel(PIPELINE, executionId, user, reason);
    }

    @Override
    public void pausePipeline(String executionId, String user) {
        executionOperator.pause(PIPELINE, executionId, user);
    }

    @Override
    public void resumePipeline(String executionId, String user) {
        executionOperator.resume(PIPELINE, executionId, user, false);
    }

    @Override
    public Execution retryPipelineStage(String executionId, String stageId) {
        Execution execution = executionRepository.retrieve(PIPELINE, executionId);
        executionRunner.restart(execution, stageId);
        return execution;
    }

    private Map initAugmentedContext(Map pipelineMap) {
        Map augmentedContext = new HashMap();
        augmentedContext.put("trigger", pipelineMap.get("trigger"));
        augmentedContext.put("templateVariables", new HashMap<>());
        return augmentedContext;
    }


    public Map<String, Object> parseAndValidatePipeline(PipelineConfig pipelineConfig, boolean resolveArtifacts) {
        ExecuteTrigger trigger = pipelineConfig.getTrigger();
        if (StringUtils.isNotEmpty(trigger.getParentPipelineId())) {
            // 查询 父流水线 并赋值
        }

        // 从配置参数中检验必填参数是否有赋值
        if (CollectionUtils.isNotEmpty(pipelineConfig.getParameterConfig())) {
            pipelineConfig.getParameterConfig().forEach(item -> {
                if (trigger.getParameters() == null) trigger.setParameters(new HashMap());
                if (!trigger.getParameters().containsKey(item.getName())) {
                    trigger.getParameters().put(item.getName(), item.getDefaultValue());
                }
            });
        }

        if (resolveArtifacts) {
            trigger.setArtifcats(new ArrayList<>());
            trigger.setExpectedArtifacts(new ArrayList<>());
            trigger.setExpectedArtifacts(new ArrayList<>());
        }
        return JSON.parseObject(JSON.toJSONString(pipelineConfig), new TypeReference<Map<String, Object>>() {
        });


    }

    /**
     * stage中 refId和requisiteStageRefIds 计算
     *
     * @param stages
     */
    private void initRequisiteStageRefIds(List<StageConfig> stages) {
        if (CollectionUtils.isNotEmpty(stages)) {
            // 归类排序
            Map<Integer, Map<Integer, Map<Integer, StageConfig>>> stageOrderGroups = groupByOrder(stages);
            // 计算 requisiteStageRefIds
            computeRequisiteRefIds(stageOrderGroups);
        }
    }

    private Map<Integer, Map<Integer, Map<Integer, StageConfig>>> groupByOrder(List<StageConfig> stages) {
        Map<Integer, Map<Integer, Map<Integer, StageConfig>>> stageOrderGroups = new TreeMap<Integer, Map<Integer, Map<Integer, StageConfig>>>();
        stages.forEach(item -> {
            item.setRefId(Long.toString(item.getId()));
            if (stageOrderGroups.containsKey(item.getStageOrder())) {
                Map<Integer, Map<Integer, StageConfig>> groupOrderGroupsTemp = stageOrderGroups.get(item.getStageOrder());
                if (groupOrderGroupsTemp.containsKey(item.getGroupOrder())) {
                    Map<Integer, StageConfig> taskOrderGroupsTemp = groupOrderGroupsTemp.get(item.getGroupOrder());
                    taskOrderGroupsTemp.put(item.getTaskOrder(), item);
                } else {
                    Map<Integer, StageConfig> taskOrderGroupsTemp = new TreeMap<Integer, StageConfig>();
                    taskOrderGroupsTemp.put(item.getTaskOrder(), item);
                    groupOrderGroupsTemp.put(item.getGroupOrder(), taskOrderGroupsTemp);
                }
            } else {
                Map<Integer, Map<Integer, StageConfig>> groupOrderGroups = new TreeMap<Integer, Map<Integer, StageConfig>>();
                Map<Integer, StageConfig> taskOrderGroups = new TreeMap<Integer, StageConfig>();
                taskOrderGroups.put(item.getTaskOrder(), item);
                groupOrderGroups.put(item.getGroupOrder(), taskOrderGroups);
                stageOrderGroups.put(item.getStageOrder(), groupOrderGroups);
            }
        });
        log.info("groupByOrder stageOrderGroups=[{}]", JSON.toJSONString(stageOrderGroups));
        return stageOrderGroups;
    }

    private void computeRequisiteRefIds(Map<Integer, Map<Integer, Map<Integer, StageConfig>>> stageOrderGroups) {
        List<String> beforeRequisiteRefIdList = new ArrayList<String>();
        List<String> nextRequisiteRefIdList = new ArrayList<String>();
        for (Map.Entry<Integer, Map<Integer, Map<Integer, StageConfig>>> entry1 : stageOrderGroups.entrySet()) {
            Map<Integer, Map<Integer, StageConfig>> groupOrderGroups = entry1.getValue();
            for (Map.Entry<Integer, Map<Integer, StageConfig>> entry2 : groupOrderGroups.entrySet()) {
                Map<Integer, StageConfig> taskOrderGroups = entry2.getValue();
                boolean taskFirst = true;
                int index = 0;
                String previousTaskRefId = null;
                for (Map.Entry<Integer, StageConfig> entry3 : taskOrderGroups.entrySet()) {
                    StageConfig stageConfig = entry3.getValue();
                    if (taskFirst) {
                        stageConfig.setRequisiteStageRefIds(beforeRequisiteRefIdList.toArray(new String[beforeRequisiteRefIdList.size()]));
                        previousTaskRefId = stageConfig.getRefId();
                        taskFirst = false;
                    } else {
                        stageConfig.setRequisiteStageRefIds(new String[]{previousTaskRefId});
                    }
                    if (++index == taskOrderGroups.size()) {
                        nextRequisiteRefIdList.add(stageConfig.getRefId());
                    }
                }
            }
            beforeRequisiteRefIdList = nextRequisiteRefIdList;
            nextRequisiteRefIdList = new ArrayList<String>();
        }
        log.info("computeRequisiteRefIds stageOrderGroups=[{}]", JSON.toJSONString(stageOrderGroups));
    }

    public static void main(String[] args) {
        List<StageConfig> stages = new ArrayList<>();
        StageConfig stageConfig1 = new StageConfig();
        stageConfig1.setId(1L);
        stageConfig1.setStageOrder(1);
        stageConfig1.setGroupOrder(1);
        stageConfig1.setTaskOrder(1);
        stageConfig1.setName("第一个");
        stages.add(stageConfig1);

        StageConfig stageConfig2 = new StageConfig();
        stageConfig2.setId(2L);
        stageConfig2.setStageOrder(2);
        stageConfig2.setGroupOrder(1);
        stageConfig2.setTaskOrder(1);
        stageConfig2.setName("第2个");
        stages.add(stageConfig2);

        StageConfig stageConfig3 = new StageConfig();
        stageConfig3.setId(3L);
        stageConfig3.setStageOrder(2);
        stageConfig3.setGroupOrder(1);
        stageConfig3.setTaskOrder(2);
        stageConfig3.setName("第3个");
        stages.add(stageConfig3);

        StageConfig stageConfig4 = new StageConfig();
        stageConfig4.setId(4L);
        stageConfig4.setStageOrder(2);
        stageConfig4.setGroupOrder(2);
        stageConfig4.setTaskOrder(1);
        stageConfig4.setName("第4个");
        stages.add(stageConfig4);

        StageConfig stageConfig5 = new StageConfig();
        stageConfig5.setId(5L);
        stageConfig5.setStageOrder(3);
        stageConfig5.setGroupOrder(1);
        stageConfig5.setTaskOrder(1);
        stageConfig5.setName("第5个");
        stages.add(stageConfig5);

        ExecuteServiceImpl executeService = new ExecuteServiceImpl();
        executeService.initRequisiteStageRefIds(stages);

    }

}