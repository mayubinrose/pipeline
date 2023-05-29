package com.ctg.cicd.config.service;

import com.ctg.cicd.config.entity.SettingsFunction;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hel
 * @since 2023-05-17
 */
public interface ISettingsFunctionService extends IService<SettingsFunction> {

    List<SettingsFunction> getAllFunc();

}
