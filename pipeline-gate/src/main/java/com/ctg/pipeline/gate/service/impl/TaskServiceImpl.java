package com.ctg.pipeline.gate.service.impl;

import com.ctg.pipeline.gate.service.ITaskService;
import com.ctg.pipeline.execute.api.IExecuteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 任务类
 *
 * @author zhiHuang
 * @date 2022/11/12 16:32
 **/

@Service
public class TaskServiceImpl implements ITaskService {
    private static final org.slf4j.Logger log
            = org.slf4j.LoggerFactory.getLogger(TaskServiceImpl.class);
    private  final int MAX_POLLS=32;
    private final  int INTERVAL_MS=1000;

    @Autowired
    IExecuteService executeService;


}