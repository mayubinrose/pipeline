package com.ctg.cicd.config.feign;

import com.ctg.cicd.common.model.dto.UserInfoDTO;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "gateway-paas", configuration = CustomFeignRequestInterceptor.class)
@RequestMapping("/api/openApi/paas/iam")
public interface IamGatewayFeignClient {

    /**
     * 获取租户下用户信息列表
     * @param tenantId
     * @return
     */
    @GetMapping("/openapi/v1/users")
    PageInfo<UserInfoDTO> listUsers(@RequestParam(value = "pageNow", required = false) Integer pageNum,
                                    @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                    @RequestParam(value = "orgId", required = false) Long orgId,
                                    @RequestParam("tenantId") Long tenantId,
                                    @RequestParam(value = "userNames", required = false) List<String> userNames,
                                    @RequestParam(value = "excludeUserIds", required = false) List<Long> excludeUserIds,
                                    @RequestParam(value = "fuzzyQuery", required = false) Boolean fuzzyQuery,
                                    @RequestParam(value = "includeSubOrgs", required = false, defaultValue = "true") Boolean includeSubOrgs);



}
