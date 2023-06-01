/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : localhost:3306
 Source Schema         : cicd

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 31/05/2023 15:52:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cicd_psg_task_extinfo_deploy_k8s
-- ----------------------------
DROP TABLE IF EXISTS `cicd_psg_task_extinfo_deploy_k8s`;
CREATE TABLE `cicd_psg_task_extinfo_deploy_k8s`  (
                                                     `id` bigint NOT NULL AUTO_INCREMENT,
                                                     `image_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '镜像名称',
                                                     `deploy_env_id` bigint NULL DEFAULT NULL,
                                                     `deploy_env_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                                     `deploy_env_namespace` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '部署命名空间',
                                                     `deploy_instance_no` int NULL DEFAULT NULL COMMENT '部署实例个数',
                                                     `create_time` datetime NULL DEFAULT NULL,
                                                     `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
                                                     `update_time` datetime NULL DEFAULT NULL,
                                                     `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
                                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cicd_pipeline_variable
-- ----------------------------
DROP TABLE IF EXISTS `cicd_pipeline_variable`;
CREATE TABLE `cicd_pipeline_variable`  (
                                           `id` bigint NOT NULL COMMENT '主键ID',
                                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cicd_pipeline_trigger
-- ----------------------------
DROP TABLE IF EXISTS `cicd_pipeline_trigger`;
CREATE TABLE `cicd_pipeline_trigger`  (
                                          `id` bigint NOT NULL COMMENT '主键ID',
                                          `pipeline_id` bigint NULL DEFAULT NULL,
                                          `pipeline_uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流水线UUID',
                                          `webhook` tinyint(1) NULL DEFAULT NULL COMMENT '是否支持',
                                          `crontab` tinyint(1) NULL DEFAULT NULL COMMENT '定时任务开关',
                                          `crontab_way` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '触发方式：单次触发、周期触发',
                                          `crontab_weak` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cicd_pipeline_stage_group_task
-- ----------------------------
DROP TABLE IF EXISTS `cicd_pipeline_stage_group_task`;
CREATE TABLE `cicd_pipeline_stage_group_task`  (
                                                   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                   `pipeline_uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                                   `pipeline_id` bigint NULL DEFAULT NULL,
                                                   `stage_id` bigint NULL DEFAULT NULL COMMENT '阶段ID',
                                                   `group_id` bigint NULL DEFAULT NULL,
                                                   `task_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务类型',
                                                   `task_label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务标签',
                                                   `task_order` int NULL DEFAULT NULL COMMENT '任务序号',
                                                   `build_cluster_id` int NULL DEFAULT NULL COMMENT '构建集群ID',
                                                   `task_extinfo_id` bigint NULL DEFAULT NULL COMMENT '阶段附属信息ID',
                                                   `task_config_id` bigint NULL DEFAULT NULL COMMENT '阶段配置ID',
                                                   `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                                   `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
                                                   `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                                   `update_by` datetime NULL DEFAULT NULL COMMENT '更新人',
                                                   `latest_execution_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最近一次执行ID',
                                                   `latest_execution_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最近一次执行时间',
                                                   `latest_execution_time` datetime NULL DEFAULT NULL COMMENT '最近一次执行时间',
                                                   `latest_execution_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最近一次执行人',
                                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '流水线阶段信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cicd_pipeline_stage_group
-- ----------------------------
DROP TABLE IF EXISTS `cicd_pipeline_stage_group`;
CREATE TABLE `cicd_pipeline_stage_group`  (
                                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                              `pipeline_uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                              `pipeline_id` bigint NULL DEFAULT NULL,
                                              `stage_id` bigint NULL DEFAULT NULL COMMENT '阶段ID',
                                              `stage_order` int NULL DEFAULT NULL COMMENT '阶段序号',
                                              `group_order` int NULL DEFAULT NULL COMMENT '分组序号',
                                              `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                              `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
                                              `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                              `update_by` datetime NULL DEFAULT NULL COMMENT '更新人',
                                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '流水线阶段信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cicd_pipeline_stage
-- ----------------------------
DROP TABLE IF EXISTS `cicd_pipeline_stage`;
CREATE TABLE `cicd_pipeline_stage`  (
                                        `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                        `pipeline_uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                        `pipeline_id` bigint NULL DEFAULT NULL,
                                        `stage_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '阶段名称',
                                        `stage_order` int NULL DEFAULT NULL COMMENT '阶段序号',
                                        `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                        `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
                                        `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                        `update_by` datetime NULL DEFAULT NULL COMMENT '更新人',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '流水线阶段信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cicd_pipeline_cache
-- ----------------------------
DROP TABLE IF EXISTS `cicd_pipeline_cache`;
CREATE TABLE `cicd_pipeline_cache`  (
                                        `id` bigint NOT NULL COMMENT '主键ID',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cicd_pipeline
-- ----------------------------
DROP TABLE IF EXISTS `cicd_pipeline`;
CREATE TABLE `cicd_pipeline`  (
                                  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                  `pipeline_uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流水线uuid',
                                  `node_id` bigint NULL DEFAULT NULL COMMENT '节点ID',
                                  `ndoe_uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '节点uuid',
                                  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户ID',
                                  `user_id` int NULL DEFAULT NULL,
                                  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者：对应username',
                                  `create_time` datetime NULL DEFAULT NULL,
                                  `last_update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最近一次修改者：对应username',
                                  `pipeline_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流水线名称',
                                  `template_id` bigint NULL DEFAULT NULL COMMENT '流水线模板ID',
                                  `pipeline_env` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流水线环境',
                                  `pipeline_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流水线类型（服务流水线、项目流水线）',
                                  `last_execution_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最近一次执行流水线ID',
                                  `last_execution_time` datetime NULL DEFAULT NULL COMMENT '最近一次运行时间',
                                  `last_execution_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最近一次运行状态',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `idx_pipeline_uuid`(`pipeline_uuid` ASC) USING BTREE COMMENT 'uuid索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '流水线信息表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
