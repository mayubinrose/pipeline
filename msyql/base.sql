DROP TABLE IF EXISTS `cicd_attr_spec`;
CREATE TABLE `cicd_attr_spec` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` bigint(20) NULL COMMENT '父ID',
  `code` varchar(50) NOT NULL COMMENT '属性编码',
  `name` varchar(100) NOT NULL COMMENT '属性名称',
  `type` varchar(50) NOT NULL COMMENT '属性类型',
  `sequence` integer NULL COMMENT '排序',
  `description` varchar(2000) NULL COMMENT '说明',
  `status_cd` varchar(20) NOT NULL COMMENT '状态',
  `status_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '状态时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_staff` varchar(50) NULL COMMENT '创建人',
  `update_time` timestamp NULL COMMENT '修改时间',
  `update_staff` varchar(50) NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
)
ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci AUTO_INCREMENT=5000 COMMENT='属性规格';

DROP TABLE IF EXISTS `cicd_attr_value`;
CREATE TABLE `cicd_attr_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `attr_spec_id` bigint(20) NOT NULL COMMENT '属性规格ID',
  `attr_value` varchar(50) NOT NULL COMMENT '属性值',
  `name` varchar(100) NOT NULL COMMENT '属性名称',
  `sequence` integer NOT NULL COMMENT '排序',
  `description` varchar(2000) NULL COMMENT '说明',
  `status_cd` varchar(20) NOT NULL COMMENT '状态',
  `status_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '状态时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_staff` varchar(50) NULL COMMENT '创建人',
  `update_time` timestamp NULL COMMENT '修改时间',
  `update_staff` varchar(50) NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
)
ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci AUTO_INCREMENT=5000 COMMENT='属性值';