package com.ctg.cicd.common.constant;

import lombok.Getter;

/**
 * @author hel
 * @date 2023/5/30
 */
public class EnvConstant {

    public enum EnvType {
        /**
         * 开发环境
         */
        DEVELOP("开发环境"),

        /**
         * 测试环境
         */
        TEST("测试环境"),
        /**
         *预发环境
         */
        PRE_ONLINE("预发环境"),
        /**
         *生产环境
         */
        ONLINE("生产环境");

        @Getter
        private String value;

        private EnvType(String value) {
            this.value = value;
        }
    }
}
