package com.ctg.pipeline.gate.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author zhiHuang
 * @date 2022/11/18 18:36
 **/
@Configuration
public class RequestInterceptor implements WebMvcConfigurer {


    public static String REQUEST_TIME = "pipeline_http_request_time";

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                                     Object handler) throws Exception {
                request.setAttribute(REQUEST_TIME, new Date());
                return true;
            }
        }).addPathPatterns("/pipelines/**","/test/**");
    }
}