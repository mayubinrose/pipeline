package com.ctg.pipeline.execute.config;

import com.ctg.pipeline.execute.model.UserConfiguredUrlRestrictions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhiHuang
 * @date 2022/11/15 23:48
 **/
@Configuration
@ComponentScan({
        "com.ctg.pipeline.execute",
})
public class ExecuteConfiguration {


    @Bean
    @ConfigurationProperties("user-configured-url-restrictions")
    public UserConfiguredUrlRestrictions.Builder userConfiguredUrlRestrictionProperties() {
        return new UserConfiguredUrlRestrictions.Builder();
    }

    @Bean
    UserConfiguredUrlRestrictions userConfiguredUrlRestrictions(
            UserConfiguredUrlRestrictions.Builder userConfiguredUrlRestrictionProperties) {
        return userConfiguredUrlRestrictionProperties.build();
    }
}