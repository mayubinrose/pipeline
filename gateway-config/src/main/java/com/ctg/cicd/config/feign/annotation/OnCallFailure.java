package com.ctg.cicd.config.feign.annotation;

import com.ctg.cicd.config.feign.constant.OuterSystem;
import com.ctg.eadp.common.exception.BusinessException;

import java.lang.annotation.*;

/**
 * @author jirt
 * @date 2020/8/10 11:18
 */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnCallFailure {

    /**
     * 所属系统
     */
    OuterSystem system();

    /**
     * 业务异常
     */
    BusinessException exception();
}
