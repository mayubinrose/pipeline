package com.ctg.cicd.config.service.impl;

import com.ctg.cicd.config.dao.SettingsFunctionDao;
import com.ctg.cicd.config.entity.SettingsFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctg.cicd.config.service.ISettingsFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hel
 * @since 2023-05-17
 */
@Service
public class SettingsFunctionService extends ServiceImpl<SettingsFunctionDao, SettingsFunction> implements ISettingsFunctionService {

    @Autowired
    private SettingsFunctionDao settingsFunctionDao;

    @Override
    public List<SettingsFunction> getAllFunc() {

        return settingsFunctionDao.getAllFunc();
    }



}
