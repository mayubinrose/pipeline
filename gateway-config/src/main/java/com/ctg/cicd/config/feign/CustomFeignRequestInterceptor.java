package com.ctg.cicd.config.feign;

import com.ctg.cloud.paascommon.entity.UserInfo;
import com.ctg.cloud.paascommon.utils.CloudDeskSignUtils;
import com.ctg.cloud.paascommon.utils.SecurityUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

import static com.ctg.eadp.common.exception.BusinessException.DES_ENCODE_FAILURE;

@Slf4j
@Configuration
public class CustomFeignRequestInterceptor implements RequestInterceptor {

    @Value("${paas.gateway.app-key:}")
    private String appKey;

    @Value("${paas.gateway.app-secret:}")
    private String appSecret;

    @Override
    public void apply(RequestTemplate requestTemplate) {

        String requestBody = "";
        byte[] body = requestTemplate.body();
        if(null != body && body.length > 0){
            requestBody = new String(body);
        }
        Map<String, Collection<String>> queries = requestTemplate.queries();
        requestTemplate.header("x-ctg-apikey", appKey);
        requestTemplate.header("x-ctg-algorithm", "MD5");
        if (!StringUtils.isEmpty(requestBody)) {
            requestTemplate.header("x-ctg-reqJson", CloudDeskSignUtils.genMD5(requestBody));
        }
        requestTemplate.header("x-ctg-timestamp", System.currentTimeMillis() + "");
        requestTemplate.header("x-ctg-nonStr", RandomStringUtils.randomAlphanumeric(8));
        UserInfo userInfo = SecurityUtils.getUserInfo();
        Map<String, Collection<String>> headers = requestTemplate.headers();
        if (userInfo != null ){
            if(null == headers.get("x-ctg-tenantId")){
                requestTemplate.header("x-ctg-tenantId", String.valueOf(userInfo.getTenantId()));
            }
            if(null == headers.get("x-ctg-userId")){
                requestTemplate.header("x-ctg-userId", String.valueOf(userInfo.getUserId()));
            }
//        requestTemplate.header("x-ctg-tenantCode", userInfo.getTenantCode());
        }
        List<String> paramList = queries.keySet().stream().collect(Collectors.toList());
        List<String> signList = requestTemplate.headers().keySet().stream().filter((keyx) -> {
            return keyx.startsWith("x-ctg-");
        }).collect(Collectors.toList());

        signList.addAll(paramList);
        Collections.sort(signList);
        StringBuilder paramStb = new StringBuilder();
        StringBuilder signStb = new StringBuilder();
        Iterator var13 = signList.iterator();

        while(var13.hasNext()) {
            String key = (String)var13.next();
            if (queries.containsKey(key) && null != queries.get(key) && queries.get(key).iterator().hasNext()) {
                paramStb.append("&").append(key).append("=").append(queries.get(key).iterator().next());
                signStb.append("&").append(key).append("=").append(queries.get(key).iterator().next());
            }
            if (requestTemplate.headers().containsKey(key)) {
                signStb.append("&").append(key).append("=").append(requestTemplate.headers().get(key).iterator().next());
            }
        }

        try {
            signStb.append(appSecret);
            String sign = CloudDeskSignUtils.genMD5(URLDecoder.decode(signStb.substring(1), "UTF-8"));
            requestTemplate.header("sign", sign);
        } catch (UnsupportedEncodingException var15) {
            log.error("origin sign={}", signStb.toString(), var15.getMessage());
            throw DES_ENCODE_FAILURE.exception();
        }
    }
}
