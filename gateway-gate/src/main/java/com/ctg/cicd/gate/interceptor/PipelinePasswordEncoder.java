package com.ctg.cicd.gate.interceptor;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author zhiHuang
 * @date 2022/11/18 16:39
 **/
public class PipelinePasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return charSequence.equals(s);
    }

}