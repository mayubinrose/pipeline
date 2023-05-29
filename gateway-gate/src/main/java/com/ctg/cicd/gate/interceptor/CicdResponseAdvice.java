package com.ctg.cicd.gate.interceptor;

/**
 * 统一返回处理
 *
 * @author zhiHuang
 * @date 2022/11/18 18:19
 **/

import com.ctg.cicd.common.base.ResponseVO;
import com.ctg.cloud.paascommon.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@RestControllerAdvice(basePackages = "com.ctg.cicd.gate.controller")
public class CicdResponseAdvice implements ResponseBodyAdvice {
    private Logger logger = LoggerFactory.getLogger(CicdResponseAdvice.class);

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return !aClass.equals(StringHttpMessageConverter.class); // 非string返回拦截处理
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass,
                                  ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (mediaType != null && o != null && mediaType.includes(MediaType.APPLICATION_JSON)
                && serverHttpRequest instanceof ServletServerHttpRequest) {
            // 打印请求时间
            ServletServerHttpRequest request = (ServletServerHttpRequest) serverHttpRequest;
            HttpServletRequest httpServletRequest = request.getServletRequest();
            Date requestTime = (Date) httpServletRequest.getAttribute(RequestInterceptor.REQUEST_TIME);
            if (requestTime != null) {
                long useTime = System.currentTimeMillis() - requestTime.getTime();
                Method method = methodParameter.getMethod();
                if (logger.isInfoEnabled()) {
                    logger.info("Request.controller=[{}],Request.method=[{}],Request.uri=[{}],Request.costTime=[{}]ms",
                            method.getDeclaringClass().getSimpleName(), method.getName(), serverHttpRequest.getURI(),
                            useTime);
                }
            }
            // 结果统一转换为 Response标准格式
            if (o instanceof ResponseVO) {
                return o;
            }
            return ResponseVO.success(o);
        }
        return ResponseVO.success(o);
    }
}