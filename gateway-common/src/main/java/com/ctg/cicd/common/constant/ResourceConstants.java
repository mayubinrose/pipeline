package com.ctg.cicd.common.constant;


import java.util.HashMap;
import java.util.Map;

public class ResourceConstants {


    public static final String RESOURCESTATE = "resourcestate";

    public static final String ECS_URL = "ecs_url";

    public static final Integer MSERCM_SUCCESS_CODE = 200;

    /**
     * ECS运行状态
     */
    public static final Byte ECS_RUN_STATUS =  1;

    /**
     * 运行状态
     */
    public static final int RUN_STATUS =  1;
    /**
     * 变更中
     */
    public static final int CHANGING_STATUS =  7;

    public static final String K8S_NAMESPACE_ADMIN_AUTHORITY = "ccse:preset:admin";
    public static final String K8S_NAMESPACE_OPERATION_AUTHORITY = "ccse:preset:ops";
    public static final String K8S_NAMESPACE_DEVELOPER_AUTHORITY = "ccse:preset:dev";
    public static final String K8S_NAMESPACE_VIEW_AUTHORITY = "ccse:preset:view";

    public static final Map<String,String> SUBCATEGORY_MAP = new HashMap<String,String>(){{
        put("ccse","dcos");
        put("ccseagent", "dcos");
        put("msercm","msercm");
        put("ecs","ecs");
        put("mysql","mysql");
        put("mse","mse");
    }};

    /**
     * 资源大类
     */
    public enum MainCategory {

        /**
         * 代码合并
         */
        BASE("1"),
        OPTION("2");

        private String value;

        private MainCategory(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

    /**
     * 资源大类
     */
    public enum SubCategory {

        /**
         * 代码合并
         */
        ECS("ecs"),
        DCOS("dcos"),
        MSERCM("msercm"),
        MSEGW("msegw"),
        MYSQL("mysql"),
        VPC("vpc"),
        REDIS("redis"),
        CTYUNDB("ctyundb"),
        KAFKA("kafka"),
        ROCKETMQ("rocketmq");
        private String value;

        private SubCategory(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

    public static final String ARMSMONITORURL  = "armsMonitor";

    public static final String MSEMSCMONITORURL = "msemsc";

}
