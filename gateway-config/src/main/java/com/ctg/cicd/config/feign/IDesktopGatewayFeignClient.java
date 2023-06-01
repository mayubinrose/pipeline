package com.ctg.cicd.config.feign;

import com.ctg.cicd.common.model.dto.VpcInfoDTO;
import com.ctg.cloud.paascommon.vo.Response;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "gateway-paas", configuration = CustomFeignRequestInterceptor.class)
@RequestMapping("/api/openApi/paas")
public interface IDesktopGatewayFeignClient {


    @GetMapping("/mgrvm/ecloudvpc/pageVpc")
    Response<PageInfo<VpcInfoDTO>> pageVpc(@RequestHeader("x-ctg-regionUuid") String regionUuidCode,
                                           @RequestParam(name = "status", required = false, defaultValue = "1") int status,
                                           @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum,
                                           @RequestParam(name = "pageSize", required = false, defaultValue = "100") int pageSize);



}
