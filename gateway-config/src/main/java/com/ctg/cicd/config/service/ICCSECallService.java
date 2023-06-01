package com.ctg.cicd.config.service;

import com.ctg.cicd.common.model.dto.PluginInfoDTO;
import com.ctgudal.eadp.dto.ClusterNamespaceDTO;
import com.ctgudal.eadp.dto.ClusterV2DTO;
import com.ctgudal.eadp.dto.ResultDTO;

import java.util.List;

public interface ICCSECallService {

    /**
     * 获取CCSE集群命名空间
     * @param clusterCode
     * @return
     */
    ResultDTO<List<ClusterNamespaceDTO>> listNamespaceV2(String clusterCode);

    /**
     * 查询K8S集群详细信息
     * @param clusterCode
     * @return
     */
    ResultDTO<ClusterV2DTO> getK8sClusterDetail(String clusterCode);

    /**
     * 创建命名空间
     * @param
     * @param namespace
     * @return
     */
    ResultDTO<String> createNamespaceV2(String clusterCode, String namespace);

    /**
     * 删除命名空间
     * @param clusterCode
     * @param namespace
     * @return
     */
    ResultDTO<String> deleteNameSpaceV2(String clusterCode, String namespace);

    ResultDTO<PluginInfoDTO> plugins(String pluginName, String clusterName, String code);
}
