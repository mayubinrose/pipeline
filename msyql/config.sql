SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for p_cfg_application
-- ----------------------------
DROP TABLE IF EXISTS `p_cfg_application`;
CREATE TABLE `p_cfg_application`  (
                                      `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                      `application` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '应用名称',
                                      `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
                                      `create_by` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
                                      `create_time` datetime NOT NULL COMMENT '创建时间',
                                      `update_by` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
                                      `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
                                      `deleted` tinyint(1) NOT NULL DEFAULT b'0' COMMENT '是否删除(0-否,1-是)',
                                      PRIMARY KEY (`id`) USING BTREE,
                                      UNIQUE INDEX `idx_application`(`application` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '应用表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for p_cfg_pipeline
-- ----------------------------
DROP TABLE IF EXISTS `p_cfg_pipeline`;
CREATE TABLE `p_cfg_pipeline`  (
                                   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                   `application` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '应用名称',
                                   `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流水线名称',
                                   `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
                                   `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
                                   `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                   `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
                                   `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                   PRIMARY KEY (`id`) USING BTREE,
                                   UNIQUE INDEX `idx_app_pipeline`(`application` ASC, `name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for p_cfg_pipeline_notification
-- ----------------------------
DROP TABLE IF EXISTS `p_cfg_pipeline_notification`;
CREATE TABLE `p_cfg_pipeline_notification`  (
                                                `id` bigint NOT NULL AUTO_INCREMENT,
                                                `level` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'PIPELINE' COMMENT '通知级别，当前默认为PIPELINE',
                                                `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '通知类型，如webhook、email',
                                                `occasion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '通知时机，如\"pipeline.complete\"',
                                                `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '通知地址',
                                                `content_template` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '通知内容模板',
                                                `pipeline_config_id` bigint NOT NULL COMMENT '流水线配置id',
                                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for p_cfg_pipeline_parameter
-- ----------------------------
DROP TABLE IF EXISTS `p_cfg_pipeline_parameter`;
CREATE TABLE `p_cfg_pipeline_parameter`  (
                                             `id` bigint NOT NULL AUTO_INCREMENT,
                                             `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数名称',
                                             `default_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '默认值',
                                             `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
                                             `source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数输入来源：用户输入、自动传参等',
                                             `pipeline_config_id` bigint NOT NULL COMMENT '流水线配置id',
                                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for p_cfg_pipeline_trigger
-- ----------------------------
DROP TABLE IF EXISTS `p_cfg_pipeline_trigger`;
CREATE TABLE `p_cfg_pipeline_trigger`  (
                                           `id` bigint NOT NULL AUTO_INCREMENT,
                                           `pipeline_config_id` bigint NOT NULL COMMENT '流水线配置id',
                                           `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '触发器类型（流水线、cron表达式）',
                                           `config` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '具体配置',
                                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for p_ctg_stage
-- ----------------------------
DROP TABLE IF EXISTS `p_ctg_stage`;
CREATE TABLE `p_ctg_stage`  (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类型',
                                `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '阶段名称',
                                `config` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '阶段配置',
                                `requisite_stageref_ids` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '运行依赖的外部stage id集合',
                                `stage_order` int NULL DEFAULT NULL COMMENT '阶段顺序',
                                `group_order` int NULL DEFAULT NULL COMMENT '分组顺序',
                                `task_order` int NULL DEFAULT NULL COMMENT '任务顺序',
                                `pipeline_config_id` bigint NOT NULL COMMENT '流水线配置id',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for p_ctg_template
-- ----------------------------
DROP TABLE IF EXISTS `p_ctg_template`;
CREATE TABLE `p_ctg_template` (
                                  `id` BIGINT NOT NULL AUTO_INCREMENT,
                                  `name` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '模板名称',
                                  `type` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '类型',
                                  `config` VARCHAR(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '阶段配置',
                                  `icon` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '模板图标',
                                  `description` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '描述',
                                  `sort` INT DEFAULT '0'  COMMENT '排序',
                                  `create_by` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
                                  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
                                  `update_by` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
                                  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE=INNODB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for p_ctg_template_group
-- ----------------------------
DROP TABLE IF EXISTS `p_ctg_template_group`;
CREATE TABLE `p_ctg_template_group` (
                                        `id` BIGINT NOT NULL AUTO_INCREMENT,
                                        `group_name` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '分组名称',
                                        `template_id` BIGINT NOT NULL COMMENT '模板id',
                                        `sort` INT DEFAULT '0' COMMENT '排序',
                                        `remark` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE=INNODB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;