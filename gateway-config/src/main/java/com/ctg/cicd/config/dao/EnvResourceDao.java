package com.ctg.cicd.config.dao;

import com.ctg.cicd.common.model.dto.EnvResourceDTO;
import com.ctg.cicd.config.entity.EnvResource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 环境资源关联表 Mapper 接口
 * </p>
 *
 * @author hel
 * @since 2023-05-30
 */
@Repository
public interface EnvResourceDao extends BaseMapper<EnvResource> {

    EnvResource getEnvResourceByInstIdAndNameSpace(@Param("envId") Long envId, @Param("instId") String instId, @Param("namespace") String namespace);

    List<EnvResourceDTO> page(@Param("tenantId") Long tenantId, @Param("envId") Long envId, @Param("mainCategory") String mainCategory, @Param("subCategory") String subCategory);

    EnvResource getEnvResourceByEnvIdAndInstId(Long envId, String instId);
}
