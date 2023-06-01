CREATE TABLE `cicd_settings_connection` (
                                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                            `uuid` varchar(255) DEFAULT NULL,
                                            `connection_name` varchar(255) DEFAULT NULL COMMENT '连接名称',
                                            `connection_type` varchar(255) DEFAULT NULL COMMENT '连接类型',
                                            `credential_id` bigint DEFAULT NULL COMMENT '证书ID',
                                            `credential_uuid` varchar(255) DEFAULT NULL COMMENT '证书UUID',
                                            `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
                                            `node_root_id` bigint DEFAULT NULL COMMENT '根节点ID',
                                            `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
                                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                            `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
                                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `cicd_settings_credential_code` (
                                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                 `uuid` varchar(255) DEFAULT NULL,
                                                 `credential_name` varchar(255) DEFAULT NULL COMMENT '证书名称',
                                                 `auth_type` varchar(255) DEFAULT NULL COMMENT '证书类型',
                                                 `auth_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '授权账号',
                                                 `auth_password` varchar(255) DEFAULT NULL COMMENT '令牌或密码',
                                                 `node_root_id` bigint DEFAULT NULL COMMENT '节点ID',
                                                 `tenant_id` bigint DEFAULT NULL,
                                                 `deleted` tinyint DEFAULT NULL COMMENT '是否删除',
                                                 `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
                                                 `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                                 `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
                                                 `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;