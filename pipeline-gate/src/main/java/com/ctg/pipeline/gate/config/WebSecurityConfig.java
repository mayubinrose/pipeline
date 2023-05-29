package com.ctg.pipeline.gate.config;

import com.ctg.pipeline.gate.interceptor.PipelinePasswordEncoder;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * spring-security配置
 *
 * @author zhiHuang
 * @date 2022/11/18 16:31
 **/
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(new PipelinePasswordEncoder()).withUser("user")
                .password("123456").roles("USER");
        auth.inMemoryAuthentication().passwordEncoder(new PipelinePasswordEncoder()).withUser("admin")
            .password("123456").roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().anyRequest().permitAll(); // 放行所有接口
        // .antMatchers("/user").hasRole("USER") //访问 /user这个接口，需要有USER角色
        // .antMatchers("/admin").hasRole("ADMIN")
        // .anyRequest().authenticated() //剩余的其他接口，登录之后就能访问
        // .and()
        // .formLogin().defaultSuccessUrl("/hello");
    }

}