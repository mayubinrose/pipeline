package com.ctg.cicd.config.feign;

import com.ctg.cicd.common.model.dto.PluginInfoDTO;
import com.ctg.cloud.paascommon.vo.Response;
import com.ctgudal.eadp.dto.ClusterNamespaceDTO;
import com.ctgudal.eadp.dto.ClusterV2DTO;
import com.ctgudal.eadp.dto.ResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "gateway-paas", configuration = CustomFeignRequestInterceptor.class/**, url = "10.50.208.131:8851"**/)
@RequestMapping("/api/openApi/paas/ccse")
public interface ICcseGatewayFeignClient {

    @GetMapping("/api/v2/clusters/{clusterName}/namespace/list")
    ResultDTO<List<ClusterNamespaceDTO>> listNamespaceV2(@PathVariable("clusterName") String clusterName);

    @GetMapping("/api/v2/clusters/{clusterName}")
    ResultDTO<ClusterV2DTO> getK8sClusterDetailV2(@PathVariable("clusterName") String clusterName);

    @PostMapping("/api/v2/clusters/{clusterName}/api/v1/namespace")
    ResultDTO<String> createNamespaceV2(@PathVariable("clusterName") String clusterName,
                                        @RequestBody String ymal);

    @DeleteMapping("/api/v2/clusters/{clusterName}/api/v1/namespace/{namespaceName}")
    ResultDTO<String> deleteNamespaceV2(@PathVariable("clusterName") String clusterName,
                                        @PathVariable("namespaceName") String namespaceName);


//    @PostMapping("/api/v2/clusters/{clusterName}/binding/set")
//    ResultDTO setBindK8sNamespace(@PathVariable("clusterName") String clusterName,
//                                  @RequestBody BindK8sNamespaceVO clusterRoleBindingSetVO);

    @GetMapping("/api/v2/{hubName}/repository/{repositoryName}/exists")
    Response<Boolean> repositoryExist(@RequestHeader("x-ctg-tenantId") String tenantId,
                                      @RequestHeader("x-ctg-userId") String userId,
                                      @PathVariable("hubName") String hubName,
                                      @PathVariable("repositoryName") String repositoryName);

//    @PostMapping("/api/v2/clusters/{clusterName}/appstore/pluginInstance")
//    ResultDTO pluginInstance(@PathVariable("clusterName") String clusterName,
//                             @RequestBody K8sPluginInstanceDTO k8sPluginInstanceDTO);
//
    @GetMapping("/api/v2/clusters/{clusterName}/appstore/plugins/{pluginName}")
    ResultDTO<PluginInfoDTO> pluginInstanceInfo(@PathVariable("clusterName") String clusterName,
                                                @PathVariable("pluginName") String pluginName,
                                                @RequestParam("repository") String repository);

    @GetMapping("/api/v2/clusters/{clusterName}/appstore/pluginInstance/{chartName}/exists")
    ResultDTO pluginInstanceExists(@PathVariable("clusterName") String clusterName,
                                   @PathVariable("chartName") String chartName);


//    @GetMapping("/appstore/plugins/{pluginName}")
//    ResultDTO<PluginInfoDTO> plugins(@PathVariable("pluginName") String pluginName,
//                                     @RequestParam("code") String code);

    @PostMapping("/api/v2/clusters/{clusterName}/api/v1/namespaces/{namespaceName}/Secret")
    ResultDTO createSecret(@PathVariable("clusterName") String clusterName,
                           @PathVariable("namespaceName") String namespaceName,
                           @RequestBody String ymal);

    @GetMapping("/api/v2/clusters/{clusterName}/api/v1/namespaces/{namespaceName}/Secret")
    String getSecretList(@PathVariable("clusterName") String clusterName,
                         @PathVariable("namespaceName") String namespaceName);

    @PutMapping("/api/v2/clusters/{clusterName}/api/v1/namespaces/{namespaceName}/pods/{podName}")
    ResultDTO<String> updatePods(@PathVariable("clusterName") String clusterName,
                                 @PathVariable("namespaceName") String namespaceName,
                                 @PathVariable("podName") String podName,
                                 @RequestBody String ymal);

    @GetMapping("/api/v2/clusters/{clusterName}/api/v1/namespaces/{namespaceName}/pods?labelSelector={labelSelector}")
    String listPods(@PathVariable("clusterName") String clusterName,
                    @PathVariable("namespaceName") String namespaceName,
                    @PathVariable("labelSelector") String labelSelector);

    @GetMapping("/api/v2/clusters/{clusterName}/services/checkNodePortFree")
    ResultDTO<Boolean> checkNodePortFree(@PathVariable("clusterName") String clusterName,
                                         @RequestParam("nodePort") Integer nodePort);

    @GetMapping("/api/v2/clusters/{clusterName}/services/nodePortList")
    ResultDTO<List<String>> listNodePort(@PathVariable("clusterName") String clusterName);
}
