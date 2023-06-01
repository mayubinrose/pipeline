package com.ctg.cicd.config.service.impl;

import com.ctg.cicd.config.dao.SettingsConnectionDao;
import com.ctg.cicd.config.entity.settings.SettingsConnection;
import com.ctg.cicd.config.service.ISettingsConnectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 作者
 * @since 2023-05-30
 */
@Service
public class SettingsConnectionServiceImpl extends ServiceImpl<SettingsConnectionDao, SettingsConnection> implements ISettingsConnectionService {

}
