package com.ctg.cicd.config.service.impl;

import com.ctg.cicd.common.model.dto.PluginInfoDTO;
import com.ctg.cicd.config.feign.ICcseGatewayFeignClient;
import com.ctg.cicd.config.service.ICCSECallService;
import com.ctgudal.eadp.dto.ClusterNamespaceDTO;
import com.ctgudal.eadp.dto.ClusterV2DTO;
import com.ctgudal.eadp.dto.ResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CCSECallServiceImpl implements ICCSECallService {

    @Autowired
    private ICcseGatewayFeignClient ccseGatewayFeignClient;


    @Override
    public ResultDTO<List<ClusterNamespaceDTO>> listNamespaceV2(String clusterCode) {

        return ccseGatewayFeignClient.listNamespaceV2(clusterCode);
    }

    @Override
    public ResultDTO<ClusterV2DTO> getK8sClusterDetail(String clusterCode) {

        return ccseGatewayFeignClient.getK8sClusterDetailV2(clusterCode);
    }

    @Override
    public ResultDTO<String> createNamespaceV2(String clusterCode, String namespace) {
        //创建namespace
        ResultDTO resultDTO = ccseGatewayFeignClient.createNamespaceV2(clusterCode,namespace);

        return resultDTO;
    }

    @Override
    public ResultDTO<String> deleteNameSpaceV2(String clusterCode, String namespace) {

        ResultDTO resultDTO = ccseGatewayFeignClient.deleteNamespaceV2(clusterCode,namespace);

        return resultDTO;
    }

    @Override
    public ResultDTO<PluginInfoDTO> plugins(String pluginName, String clusterName, String code) {

        ResultDTO<PluginInfoDTO> resultDTO = ccseGatewayFeignClient.pluginInstanceInfo(clusterName, pluginName, code);

        return resultDTO;
    }
}
