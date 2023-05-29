package com.ctg.cicd.config.manager;

import cn.hutool.core.util.StrUtil;
import com.ctg.cicd.common.model.dto.UserInfoDTO;
import com.ctg.cicd.config.feign.IamGatewayFeignClient;
import com.ctg.cicd.config.feign.annotation.OnCallFailure;
import com.ctg.cicd.config.feign.constant.OuterSystem;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static com.ctg.eadp.common.exception.BusinessException.QUERY_ADDABLEMEMBER_FAILURE;

/**
 * @author jirt
 * @date 2020/8/14 18:33
 */
@Slf4j
@Component
public class IamManager {

    @Autowired
    private IamGatewayFeignClient iamGatewayFeignClient;

    @OnCallFailure(system = OuterSystem.IAM, exception = QUERY_ADDABLEMEMBER_FAILURE)
    public PageInfo<UserInfoDTO> queryTenantMembers(Integer pageNum, Integer pageSize, Long orgId, Long tenantId, String keyword, List<Long> excludeUserIds) {
        List<String> userNames = null;
        boolean fuzzyQuery = false;
        if (StrUtil.isNotBlank(keyword)) {
            fuzzyQuery = true;
            userNames = Arrays.asList(keyword);
        }
        return iamGatewayFeignClient.listUsers(pageNum, pageSize, orgId, tenantId, userNames, excludeUserIds, fuzzyQuery, true);
    }


}
