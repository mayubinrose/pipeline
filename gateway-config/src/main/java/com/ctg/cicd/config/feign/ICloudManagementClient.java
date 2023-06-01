package com.ctg.cicd.config.feign;

import com.ctg.cicd.common.model.dto.TopologyHostDTO;
import com.ctg.cloud.paascommon.interceptor.JwtRequestInterceptor;
import com.ctg.cloud.paascommon.vo.Response;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ctg-cloud-management-service", configuration = JwtRequestInterceptor.class)
public interface ICloudManagementClient {

    /**
     * 用于前端展示
     *
     * @param topologyId
     * @param hostIdList
     * @param hostName
     * @param hostIp
     * @param machine
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/tenantHost")
    Response<PageInfo<TopologyHostDTO>> getTopologyHosts(@RequestParam("topologyId") Long topologyId,
                                                         @RequestParam("hostIdList") List<Long> hostIdList,
                                                         @RequestParam("hostName") String hostName,
                                                         @RequestParam("hostIp") String hostIp,
                                                         @RequestParam("machine") Byte machine,
                                                         @RequestParam("tenantId") Long tenantId,
                                                         @RequestParam("userId") Long userId,
                                                         @RequestParam("resPoolId") String resPoolId,
                                                         @RequestParam("pageNum") int pageNum,
                                                         @RequestParam("pageSize") int pageSize);


}
