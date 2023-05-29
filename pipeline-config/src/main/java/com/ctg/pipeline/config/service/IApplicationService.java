package com.ctg.pipeline.config.service;

import com.ctg.pipeline.common.model.pipeline.config.ApplicationConfig;
import com.ctg.pipeline.config.entity.Application;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 应用表 服务类
 * </p>
 *
 * @author hel
 * @since 2022-11-14
 */
public interface IApplicationService extends IService<Application> {

    ApplicationConfig saveApplication(ApplicationConfig config);

    Application getApplication(String name);
}
