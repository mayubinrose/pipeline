package com.ctg.pipeline.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ctg.pipeline.config.entity.Stage;
import com.ctg.pipeline.config.dao.StageDao;
import com.ctg.pipeline.config.service.IStageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hel
 * @since 2022-11-14
 */
@Service
public class StageServiceImpl extends ServiceImpl<StageDao, Stage> implements IStageService {

    @Autowired
    private StageDao stageDao;

    @Override
    public List<Stage> getListByPipelineConfigId(Long pipelineConfigId) {
        LambdaQueryWrapper<Stage> stageWrapper = Wrappers.lambdaQuery();
        stageWrapper.eq(Stage::getPipelineConfigId,pipelineConfigId);
        return this.list(stageWrapper);
    }

    @Override
    public void deleteBatchByPipelineConfigId(Long pipelineConfigId) {
        LambdaQueryWrapper<Stage> stageWrapper = Wrappers.lambdaQuery();
        stageWrapper.eq(Stage::getPipelineConfigId,pipelineConfigId);
        this.remove(stageWrapper);
    }
}
