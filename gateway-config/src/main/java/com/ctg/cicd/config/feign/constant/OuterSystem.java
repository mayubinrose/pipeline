package com.ctg.cicd.config.feign.constant;

import lombok.Getter;

/**
 * @author jirt
 * @date 2020/8/10 17:24
 */
public enum OuterSystem {

    /**
     * 日志中心
     */
    LOG_CENTER(500001, "EADP-logcenter"),

    /**
     * agent
     */
    EADP_AGENT(500002, "EADP-agent"),

    EDAS_SCHEDULER(500003, "Edas-Scheduler"),

    EDAS_PIPELINE(500004, "Edas-Pipeline"),

    /**
     * ccse控制台
     */
    CCSE(600001, "CCSE"),

    /**
     * Paas的adminservice
     */
    ADMIN_SERVICE(700001, "PaaS-adminservice"),

    /**
     * paas的iam
     */
    IAM(700002, "PaaS-iam"),

    /**
     * 云管平台
     */
    CLOUD_MANAGEMENT(700003, "Cloud-Management"),

    MSE(700005, "MSE"),

    DESKTOP_GATEWAY(700005, "Desktop-Gateway");

    @Getter
    private int code;

    @Getter
    private String name;

    OuterSystem(int code, String name) {
        this.code = code;
        this.name = name;
    }

}
