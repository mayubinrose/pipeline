package com.ctg.pipeline.gate;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
@MapperScan("com.ctg.pipeline.*.dao")
@SpringBootApplication(scanBasePackages = "com.ctg.pipeline")
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    /**
     * <p>
     * 热部署debug和运行指定profile方法:
     * </p>
     * <ul>
     * <li>使用(eclipse|idea)maven插件运行: <code>mvn spring-boot:run -Drun.profiles=dev</code></li>
     * <li>直接运行main函数:
     * <code>-Dspring.profiles.active=dev -javaagent:D:/springloaded-1.2.8.RELEASE.jar -noverify</code></li>
     * </ul>
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}