package com.ctg.cicd.config.feign;

import com.ctg.cicd.common.model.dto.TopologyInstInfoDTO;
import com.ctg.cloud.paascommon.vo.Response;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "gateway-paas", configuration = CustomFeignRequestInterceptor.class)
@RequestMapping("/api/openApi/paas/orderproxy")
public interface IOrderproxyGatewayFeignClient {

    @GetMapping("/openapi/v2/tenants/spuInst")
    Response<PageInfo<TopologyInstInfoDTO>> spuInst(
            @RequestParam("tenantId") Long tenantId,
            @RequestParam("userId") Long userId,
            @RequestParam("regionUuid") String regionUuid,
            @RequestParam("spuCodes") List<String> spuCodes,
            @RequestParam("parSpuInstId") String parSpuInstId,
            @RequestParam("inStatus") List<Integer> inStatus,
            @RequestParam("pageNow") Integer pageNow,
            @RequestParam("pageSize") Integer pageSize
    );

}
