-- 环境配置
-- ----------------------------
-- Table structure for cicd_env
-- ----------------------------
DROP TABLE IF EXISTS `cicd_env`;
CREATE TABLE `cicd_env` (
                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                            `env_uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'uuid',
                            `env_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '环境名称',
                            `env_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '环境编码',
                            `env_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '环境类型',
                            `env_type_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '环境类型名称',
                            `res_pool_id` bigint DEFAULT NULL COMMENT '资源池ID',
                            `res_pool_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '资源池名称',
                            `res_pool_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '资源池编码',
                            `node_id` bigint DEFAULT NULL COMMENT '节点id',
                            `node_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '节点名称',
                            `vpc_id` bigint DEFAULT '0' COMMENT 'vpcId',
                            `vpc_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'vpc名称',
                            `docker_deploy` tinyint(1) DEFAULT '0' COMMENT '是否可以容器部署  0 否 1 是',
                            `host_deploy` tinyint(1) DEFAULT '0' COMMENT '是否可以主机部署  0 否 1 是',
                            `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
                            `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人',
                            `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
                            `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人',
                            `tenant_id` bigint NOT NULL,
                            PRIMARY KEY (`id`) USING BTREE,
                            KEY `index_cicd_env_uuid` (`env_uuid`) USING BTREE COMMENT 'UUID索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='环境表';


-- ----------------------------
-- Table structure for cicd_env_resource
-- ----------------------------
DROP TABLE IF EXISTS `cicd_env_resource`;
CREATE TABLE `cicd_env_resource` (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                     `env_id` bigint NOT NULL COMMENT '环境ID',
                                     `inst_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '资源实例ID',
                                     `resource_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '资源名称',
                                     `resource_pool_id` bigint NOT NULL COMMENT '资源池ID',
                                     `resource_pool_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '资源池编码',
                                     `namespace_inst_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '命名空间实例ID',
                                     `namespace` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '命名空间',
                                     `spu_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品名称',
                                     `spec` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '规格',
                                     `ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'IP',
                                     `order_time` timestamp NULL DEFAULT NULL COMMENT '订购时间',
                                     `inst_count` int DEFAULT NULL COMMENT '实例数',
                                     `status_cd` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '状态',
                                     `main_category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '资源大类（基础资源，可选资源）',
                                     `sub_category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '资源小类（k8s，ECS，注册中心，数据库）',
                                     `appr_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '控制台地址',
                                     `version` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '资源版本',
                                     `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
                                     `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人',
                                     `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
                                     `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人',
                                     `tenant_id` bigint NOT NULL COMMENT '租户',
                                     PRIMARY KEY (`id`) USING BTREE,
                                     KEY `index_env_id` (`env_id`) USING BTREE COMMENT '环境ID索引',
                                     KEY `index_inst_id` (`inst_id`) USING BTREE COMMENT '实例ID索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='环境资源关联表';


SET FOREIGN_KEY_CHECKS = 1;