package com.ctg.cicd.config.dao;

import com.ctg.cicd.common.model.dto.EnvDTO;
import com.ctg.cicd.config.entity.Env;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 环境表 Mapper 接口
 * </p>
 *
 * @author hel
 * @since 2023-05-30
 */
@Repository
public interface EnvDao extends BaseMapper<Env> {

    int insertEnv(Env env);

    List<EnvDTO> listByNodeIdAndEnvName(@Param("nodeId")Long nodeId, @Param("envName") String envName, @Param("envType")String envType, @Param("tenantId") Long tenantId);
}
