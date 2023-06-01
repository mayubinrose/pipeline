SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


-- ----------------------------
-- Table structure for cicd_stage_config_source_image
-- ----------------------------
CREATE TABLE `cicd_task_config_source_image` (
                                                  `id` bigint NOT NULL COMMENT '主键ID',
                                                  `node_id` bigint DEFAULT NULL COMMENT '节点ID',
                                                  `image_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '镜像类型（配置镜像、公共镜像）',
                                                  `image_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '镜像名称',
                                                  `image_url` varchar(255) DEFAULT NULL COMMENT '镜像地址',
                                                  `namespace` varchar(255) DEFAULT NULL COMMENT '镜像空间',
                                                  `repository_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '仓库ID',
                                                  `region_code` varchar(255) DEFAULT NULL COMMENT '资源池编码',
                                                  `instance_id` varchar(255) DEFAULT NULL COMMENT '实例ID',
                                                  `deleted` tinyint(1) DEFAULT NULL COMMENT '是否删除',
                                                  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
                                                  `create_time` datetime DEFAULT NULL COMMENT '创建人',
                                                  `create_by` varchar(255) DEFAULT NULL,
                                                  `update_time` datetime DEFAULT NULL,
                                                  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
                                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for cicd_task_config_source_code
-- ----------------------------
DROP TABLE IF EXISTS `cicd_task_config_source_code`;
CREATE TABLE `cicd_task_config_source_code` (
                                                 `id` bigint NOT NULL COMMENT '主键ID',
                                                 `node_id` bigint DEFAULT NULL COMMENT '节点ID',
                                                 `origin` varchar(255) DEFAULT NULL COMMENT '代码源(码云、github等)',
                                                 `repos_address` varchar(255) DEFAULT NULL COMMENT '代码库地址',
                                                 `connection_id` bigint DEFAULT NULL COMMENT '服务连接ID',
                                                 `default_branch` varchar(255) DEFAULT NULL COMMENT '默认分支',
                                                 `deleted` tinyint(1) DEFAULT NULL COMMENT '是否删除',
                                                 `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
                                                 `create_time` datetime DEFAULT NULL COMMENT '创建人',
                                                 `create_by` varchar(255) DEFAULT NULL,
                                                 `update_time` datetime DEFAULT NULL,
                                                 `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
                                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for cicd_task_config_source_artifact
-- ----------------------------
DROP TABLE IF EXISTS `cicd_task_config_source_artifact`;
CREATE TABLE `cicd_task_config_source_artifact` (
                                                     `id` bigint NOT NULL COMMENT '主键ID',
                                                     `node_id` bigint DEFAULT NULL COMMENT '节点ID',
                                                     `package_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '制品名称',
                                                     `deleted` tinyint(1) DEFAULT NULL COMMENT '是否删除',
                                                     `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
                                                     `create_time` datetime DEFAULT NULL COMMENT '创建人',
                                                     `create_by` varchar(255) DEFAULT NULL,
                                                     `update_time` datetime DEFAULT NULL,
                                                     `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
                                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for cicd_task_config_sonarqube
-- ----------------------------
DROP TABLE IF EXISTS `cicd_task_config_sonarqube`;
CREATE TABLE `cicd_task_config_sonarqube` (
                                              `id` bigint NOT NULL AUTO_INCREMENT,
                                              `sonar_uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '代码扫描配置uuid',
                                              `task_config_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '阶段配置名称',
                                              `task_type` bigint DEFAULT NULL COMMENT '阶段类型',
                                              `task_label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '阶段标签',
                                              `sonar_language` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '扫描的语言类型',
                                              `sonar_encoding` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'UTF-8' COMMENT '编码格式，默认为UTF-8',
                                              `sonar_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '扫描路径，必需的，默认整个工程文件',
                                              `sonar_exclusion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '排除扫描路径，不扫描哪些路径',
                                              `node_id` bigint NOT NULL COMMENT '节点id，支持服务节点',
                                              `tenant_id` bigint DEFAULT NULL COMMENT '租户Id',
                                              `deleted` tinyint(1) unsigned zerofill DEFAULT NULL COMMENT '是否删除',
                                              `create_by` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
                                              `create_time` datetime NOT NULL COMMENT '创建时间',
                                              `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
                                              `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
-- ----------------------------
-- Table structure for cicd_task_config_sonarqube_item
-- ----------------------------
DROP TABLE IF EXISTS `cicd_task_config_sonarqube_item`;
CREATE TABLE `cicd_task_config_sonarqube_item` (
                                                   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                                   `task_config_sonar_id` bigint NOT NULL COMMENT 'sonar部署配置id',
                                                   `attr_spec_id` bigint DEFAULT NULL COMMENT '属性id，例如基本属性配置，质量关卡设置',
                                                   `attr_spec_code` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '属性编码，''BASE_CONFIG''，’GATE_CONFIG''',
                                                   `att_value` varchar(2000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '属性值',
                                                   `tenant_id` bigint DEFAULT NULL COMMENT '租户Id',
                                                   `deleted` tinyint(1) unsigned zerofill DEFAULT NULL COMMENT '是否删除',
                                                   `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
                                                   `create_time` datetime NOT NULL COMMENT '创建时间',
                                                   `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
                                                   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='代码扫描配置项表';

-- ----------------------------
-- Table structure for cicd_task_config_junit_maven
-- ----------------------------
DROP TABLE IF EXISTS `cicd_task_config_junit_maven`;
CREATE TABLE `cicd_task_config_junit_maven`  (
                                                  `id` bigint NOT NULL COMMENT '主键ID',
                                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cicd_task_config_image_node
-- ----------------------------
DROP TABLE IF EXISTS `cicd_task_config_image_node`;
CREATE TABLE `cicd_task_config_image_node`  (
                                                 `id` bigint NOT NULL COMMENT '主键ID',
                                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cicd_task_config_image_java
-- ----------------------------
DROP TABLE IF EXISTS `cicd_task_config_image_java`;
CREATE TABLE `cicd_task_config_image_java`  (
                                                 `id` bigint NOT NULL COMMENT '主键ID',
                                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for cicd_task_config_deploy_k8s
-- ----------------------------
DROP TABLE IF EXISTS `cicd_task_config_deploy_k8s`;
CREATE TABLE `cicd_task_config_deploy_k8s`  (
    `id` bigint NOT NULL COMMENT '主键ID',
    `stage_config_name` varchar(255) NOT NULL COMMENT '阶段配置名称',
    `stage_type` bigint NOT NULL COMMENT '阶段类型',
    `stage_label` varchar(255) DEFAULT NULL COMMENT '阶段标签',
    `deploy_env_id` bigint NOT NULL COMMENT '部署环境ID',
    `deploy_config_name` varchar(255) NOT NULL COMMENT '部署配置名称',
    `deploy_config_uuid` varchar(50) NOT NULL COMMENT '部署配置uuid',
    `category` varchar(50) NOT NULL COMMENT '部署配置类型 IMAGE_DEPLOY、MS_IMAGE_DEPLOY和ISTIO_IMAGE_DEPLOY',
    `runtime_env_code` varchar(30) NULL DEFAULT NULL COMMENT '运行环境镜像名称',
    `runtime_env_ver` varchar(30) NULL DEFAULT NULL COMMENT '运行环境镜像版本',
    `node_id` bigint NOT NULL COMMENT '节点id，支持服务节点',
    `app_deploy_code` varchar(100) NOT NULL COMMENT 'deployment组成部分',
    `pod_num` int DEFAULT NULL COMMENT '微服务实例数量',
    `deleted` tinyint(1) DEFAULT NULL COMMENT '是否删除',
    `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for cicd_task_config_deploy_k8s_item
-- ----------------------------
DROP TABLE IF EXISTS `cicd_task_config_deploy_k8s_item`;
CREATE TABLE `cicd_task_config_deploy_k8s_item`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `task_config_deploy_k8s_id` bigint NOT NULL COMMENT '部署配置ID',
  `attr_spec_id` bigint NOT NULL COMMENT '属性ID',
  `attr_spec_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '属性编码',
  `attr_value` TINYTEXT  NOT NULL COMMENT '属性值',
  `deleted` tinyint(1) DEFAULT NULL COMMENT '是否删除',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '容器部署配置项表' ROW_FORMAT = Dynamic;



-- ----------------------------
-- Table structure for cicd_task_config_build_repository
-- ----------------------------
DROP TABLE IF EXISTS `cicd_task_config_build_repository`;
CREATE TABLE `cicd_task_config_build_repository`  (
                                                       `id` bigint NOT NULL COMMENT '主键ID',
                                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cicd_task_config_build_node
-- ----------------------------
DROP TABLE IF EXISTS `cicd_task_config_build_node`;
CREATE TABLE `cicd_task_config_build_node`  (
                                                 `id` bigint NOT NULL COMMENT '主键ID',
                                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cicd_task_config_build_java
-- ----------------------------
DROP TABLE IF EXISTS `cicd_task_config_build_java`;
CREATE TABLE `cicd_task_config_build_java`  (
                                                 `id` bigint NOT NULL COMMENT '主键ID',
                                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;



