package com.ctg.cicd.config.service;

import com.ctg.cicd.common.model.dto.EnvResourceDTO;
import com.ctg.cicd.common.model.dto.OperateReturnDTO;
import com.ctg.cicd.common.model.dto.VpcInfoDTO;
import com.ctg.cicd.common.model.vo.EnvResourceVO;
import com.ctg.cicd.config.entity.EnvResource;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

import java.text.ParseException;
import java.util.List;

/**
 * <p>
 * 环境资源关联表 服务类
 * </p>
 *
 * @author hel
 * @since 2023-05-30
 */
public interface IEnvResourceService extends IService<EnvResource> {

    void insertBatch(List<EnvResourceVO> baseEnvResource);

    EnvResource getEnvResourceByInstIdAndNameSpace(Long envId, String instId, String namespace);

    void deleteResourceByEnvId(Long id);

    List<VpcInfoDTO> listVpc(String resPoolId);

    List<EnvResourceDTO> listBaseResource(Long tenantId, Long userId, String resPoolCode, String subCategory, Long vpcId, Long nodeId);

    List<EnvResourceDTO> listOptionResource(Long tenantId, Long userId, String resPoolCode, Long vpcId);

    List<EnvResource> getEnvResourceByEnvIds(List<Long> idList);

    PageInfo<EnvResourceDTO> pageQuery(Integer pageNum, Integer pageSize, Long tenantId, Long envId, String mainCategory, String subCategory);

    String getAppUrl(Long envId, String instId, Boolean isNameSpace);

    OperateReturnDTO syncResourceInfo(Long envId);
}
