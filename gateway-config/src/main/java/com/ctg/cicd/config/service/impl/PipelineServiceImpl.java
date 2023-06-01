package com.ctg.cicd.config.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctg.cicd.config.dao.PipelineDao;
import com.ctg.cicd.config.entity.Pipeline;
import com.ctg.cicd.config.service.IPipelineService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 流水线信息表 服务实现类
 * </p>
 *
 * @author 作者
 * @since 2023-05-30
 */
@Service
public class PipelineServiceImpl extends ServiceImpl<PipelineDao, Pipeline> implements IPipelineService {

}
