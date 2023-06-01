/*
 Navicat Premium Data Transfer

 Source Server         : bendi
 Source Server Type    : MySQL
 Source Server Version : 80033
 Source Host           : localhost:3306
 Source Schema         : cicd

 Target Server Type    : MySQL
 Target Server Version : 80033
 File Encoding         : 65001

 Date: 22/05/2023 16:04:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cicd_node_info
-- ----------------------------
DROP TABLE IF EXISTS `cicd_node_info`;
CREATE TABLE `cicd_node_info`  (
                                   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                   `node_uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节点uuid',
                                   `node_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节点名称',
                                   `node_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节点编码',
                                   `node_type` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节点类型',
                                   `node_label` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节点标签',
                                   `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节点描述',
                                   `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点创建人',
                                   `create_time` datetime NOT NULL COMMENT '创建时间',
                                   `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
                                   `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                   `deleted` tinyint(1) NOT NULL COMMENT '是否删除(0-否,1-是)',
                                   `parent_id` bigint NULL DEFAULT NULL COMMENT '父节点的id（父节点为-1表示当前为根节点）',
                                   `parent_role_extends` tinyint(1) NOT NULL COMMENT '是否要继承父节点的角色成员(0-否,1-是)',
                                   `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户的id',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '节点信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for cicd_node_user_role
-- ----------------------------
DROP TABLE IF EXISTS `cicd_node_user_role`;
CREATE TABLE `cicd_node_user_role`  (
                                        `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                        `node_id` bigint NULL DEFAULT NULL COMMENT '节点id',
                                        `role_id` bigint NULL DEFAULT NULL COMMENT '角色id',
                                        `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
                                        `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'user_id对应的用户名',
                                        `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建者',
                                        `create_time` datetime NOT NULL COMMENT '创建时间',
                                        `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
                                        `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '节点角色成员表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for cicd_settings_function
-- ----------------------------
DROP TABLE IF EXISTS `cicd_settings_function`;
CREATE TABLE `cicd_settings_function`  (
                                           `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                           `func_uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '功能uuid',
                                           `func_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '功能类型',
                                           `func_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '功能代码',
                                           `func_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '功能名称',
                                           `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
                                           `create_time` datetime NOT NULL COMMENT '创建时间',
                                           `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
                                           `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设置权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for cicd_settings_role
-- ----------------------------
DROP TABLE IF EXISTS `cicd_settings_role`;
CREATE TABLE `cicd_settings_role`  (
                                       `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                       `role_uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色uuid',
                                       `role_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
                                       `role_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色英文简称',
                                       `role_description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色权限的描述',
                                       `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建角色者',
                                       `create_time` datetime NOT NULL COMMENT '创建时间',
                                       `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
                                       `update_time` datetime NULL DEFAULT NULL COMMENT '角色修改时间',
                                       `role_status` tinyint(1) NOT NULL COMMENT '角色状态',
                                       `node_root_id` bigint NULL DEFAULT NULL COMMENT '关联的项目根节点的id',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设置角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for cicd_settings_role_function
-- ----------------------------
DROP TABLE IF EXISTS `cicd_settings_role_function`;
CREATE TABLE `cicd_settings_role_function`  (
                                                `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                                `role_id` bigint NULL DEFAULT NULL COMMENT '角色id',
                                                `func_id` bigint NULL DEFAULT NULL COMMENT '功能id',
                                                `node_root_id` bigint NULL DEFAULT NULL COMMENT '关联的根节点的id',
                                                `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建者',
                                                `create_time` datetime NOT NULL COMMENT '创建时间',
                                                `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
                                                `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
                                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设置角色权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for cicd_settings_user
-- ----------------------------
DROP TABLE IF EXISTS `cicd_settings_user`;
CREATE TABLE `cicd_settings_user`  (
                                       `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                       `user_id` bigint NOT NULL,
                                       `user_uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户uuid',
                                       `node_root_id` bigint NULL DEFAULT NULL COMMENT '关联的项目根节点的id',
                                       `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
                                       `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
                                       `real_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户真实名',
                                       `user_email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
                                       `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL COMMENT '创建的人',
                                       `create_time` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
                                       `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设置成员表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
