package com.ctg.cicd.config.service;

import com.ctg.cicd.common.model.dto.OperateReturnDTO;
import com.ctg.cicd.common.model.dto.SaveEntityReturnDTO;
import com.ctg.cicd.common.model.vo.EnvVO;
import com.ctg.cicd.config.entity.Env;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 环境表 服务类
 * </p>
 *
 * @author hel
 * @since 2023-05-30
 */
public interface IEnvService extends IService<Env> {

    SaveEntityReturnDTO createEnv(EnvVO envVO, String userName, Long tenantId);

    OperateReturnDTO updateEnv(Long id, EnvVO env,String userName);

    List<EnvVO> listEnvByNodeId(Long nodeId, String envName, String envType, String sort, String order, Long userId, Long tenantId);

    OperateReturnDTO deleteEnvById(Long id, String staff);

    EnvVO detailEnvById(Long id);
}
