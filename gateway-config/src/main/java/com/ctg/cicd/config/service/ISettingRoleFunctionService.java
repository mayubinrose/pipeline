package com.ctg.cicd.config.service;

import com.ctg.cicd.common.model.vo.NodeUserRoleVO;
import com.ctg.cicd.common.model.vo.SettingsRoleFuncAddVo;
import com.ctg.cicd.config.entity.SettingRoleFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ctg.cicd.config.entity.SettingsFunction;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hel
 * @since 2023-05-17
 */
public interface ISettingRoleFunctionService extends IService<SettingRoleFunction> {
    boolean addFunc(String userName, SettingsRoleFuncAddVo settingsRoleFuncAddVo);

    List<SettingsFunction> listFuncByRoleList(List<NodeUserRoleVO> nodeUserRoleVOList);


    List<Long> getFuncIdsByRoleId(Long id);
}
