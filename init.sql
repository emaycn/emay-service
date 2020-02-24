DROP TABLE IF EXISTS `system_user`;
CREATE TABLE `system_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(64) NOT NULL COMMENT '用户名',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `realname` varchar(64) NOT NULL COMMENT '姓名',
  `mobile` varchar(32) DEFAULT NULL COMMENT '手机',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `user_state` tinyint NOT NULL COMMENT '状态[0-停用，1-启用]',
  `last_change_password_time` datetime DEFAULT NULL COMMENT '最后一次修改密码时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `operator_id` bigint NOT NULL COMMENT '创建人',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `user_for` varchar(16) DEFAULT '0' COMMENT '所属系统（OPER-运营系统、CLIENT-客户系统）',
  PRIMARY KEY (`id`),
  KEY `su_un` (`username`),
  KEY `su_mobile` (`mobile`),
  KEY `su_us` (`user_state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统用户';
INSERT INTO `system_user` VALUES 
(1,'admin','8CB691B84841370D1AC8158B0CCD91B4','管理员','12311111111','admin@admin.cn',1,now(),now(),1,now(),'OPER');


DROP TABLE IF EXISTS `system_role`;
CREATE TABLE `system_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_name` varchar(64) DEFAULT NULL COMMENT '角色名',
  `remark` varchar(1024) DEFAULT NULL COMMENT '说明',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `role_type` varchar(16) DEFAULT NULL COMMENT '角色类型[OPER-运营端，CLIENT-销售端]',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色';
INSERT INTO `system_role` VALUES 
(1,'管理员','运营系统管理员',now(),now(),'OPER'),
(2,'管理员','客户系统管理员',now(),now(),'CLIENT');


DROP TABLE IF EXISTS `system_user_role_assign`;
CREATE TABLE `system_user_role_assign` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  KEY `sura_ui` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户角色关联';
INSERT INTO `system_user_role_assign` VALUES (1,1,1,now());


DROP TABLE IF EXISTS `system_resource`;
CREATE TABLE `system_resource` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `resource_code` varchar(64) DEFAULT NULL COMMENT '资源CODE',
  `resource_name` varchar(64) DEFAULT NULL COMMENT '资源名称',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `resource_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '资源类型[OPER-运营端，CLIENT客户端]',
  PRIMARY KEY (`id`),
  KEY `sres_rc` (`resource_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='资源表';
INSERT INTO `system_resource` (id,resource_code,resource_name,resource_type) VALUES 
(1,'USER_VIEW','用户管理','OPER'),
(2,'USER_MODIFY','用户修改','OPER'),
(3,'USER_ADD','用户添加','OPER'),
(4,'USER_DELETE','用户删除','OPER'),
(5,'USER_OPER','用户操作','OPER'),
(6,'ROLE_VIEW','角色管理','OPER'),
(7,'ROLE_MODIFY','角色修改','OPER'),
(8,'ROLE_ADD','角色添加','OPER'),
(9,'ROLE_DELETE','角色删除','OPER'),
(10,'DEPARTMENT_VIEW','部门管理','OPER'),
(11,'DEPARTMENT_ADD','部门添加','OPER'),
(12,'DEPARTMENT_DELETE','部门删除','OPER'),
(13,'DEPARTMENT_MODIFY','部门修改','OPER'),
(14,'LOG_VIEW','日志管理','OPER'),
(15,'CLIENT_VIEW','客户管理','OPER'),
(16,'CLIENT_ADD','新增客户','OPER'),
(17,'CLIENT_MODIFY','客户修改','OPER'),
(18,'CLIENT_CHARGE','账务管理','OPER'),
(19,'RECHARGE_CHARGE','充值扣費','OPER'),
(20,'EMPTY_VIEW','空号管理','OPER'),
(21,'EMPTY_ADD','新增空号','OPER'),
(22,'EMPTY_DELETE','删除空号','OPER'),
(23,'EMPTY_IMPORT','空号导入','OPER'),
(24,'PORTABLE_VIEW','携号转网','OPER'),
(25,'PORTABLE_ADD','新增携号转网','OPER'),
(26,'PORTABLE_MODIFY','修改携号转网','OPER'),
(27,'PORTABLE_DELETE','删除携号转网','OPER'),
(28,'PORTABLE_IMPORT','携号转网导入','OPER'),
(29,'BASENUMBER_VIEW','基础号段','OPER'),
(30,'BASENUMBER_ADD','基础号段新增','OPER'),
(31,'BASENUMBER_MODIFY','基础号段修改','OPER'),
(32,'BASENUMBER_DELETE','基础号段删除','OPER'),
(33,'NUMBER_VIEW','详细号段','OPER'),
(34,'NUMBER_ADD','详细号段新增','OPER'),
(35,'NUMBER_MODIFY','详细号段修改','OPER'),
(36,'NUMBER_DELETE','详细号段删除','OPER'),
(37,'NUMBER_IMPORT','详细号段导入','OPER'),
(38,'APP_VIEW','应用列表','OPER'),
(39,'APP_ADD','应用新增','OPER'),
(40,'APP_PRICE','应用设置单价','OPER'),
(41,'APP_ONOFF','应用起停','OPER'),
(42,'CLIENTROLE_VIEW','客户角色管理','OPER'),
(43,'CLIENTROLE_MODIFY','客户角色修改','OPER'),
(44,'CLIENTROLE_ADD','客户角色添加','OPER'),
(45,'CLIENTROLE_DELETE','客户角色删除','OPER'),
(46,'CLIENTUSER_VIEW','客户用户管理','OPER'),
(47,'CLIENTUSER_MODIFY','客户用户修改','OPER'),
(48,'CLIENTUSER_ADD','客户用户添加','OPER'),
(49,'CLIENTUSER_DELETE','客户用户删除','OPER'),
(50,'CLIENTUSER_OPER','客户用户操作','OPER'),
(51,'MESSAGE_VIEW','短信详情列表','OPER'),
(52,'CLIENT_USER_VIEW','客户端-用户查询','CLIENT'),
(53,'CLIENT_APP_VIEW','客户端-应用列表','CLIENT'),
(54,'CLIENT_COMMPANY_INFO','客户端-公司信息查询','CLIENT'),
(55,'CLIENT_MESSAGE_VIEW','客户端-短信详情列表','CLIENT');

DROP TABLE IF EXISTS `system_role_resource_assign`;
CREATE TABLE `system_role_resource_assign` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `resource_id` bigint DEFAULT NULL COMMENT '资源id',
  `role_id` bigint DEFAULT NULL COMMENT '角色id',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  KEY `srrea_rc` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='资源角色表';
INSERT INTO `system_role_resource_assign` (resource_id,role_id) VALUES 
(1,1),
(2,1),
(3,1),
(4,1),
(5,1),
(6,1),
(7,1),
(8,1),
(9,1),
(10,1),
(11,1),
(12,1),
(13,1),
(14,1),
(15,1),
(16,1),
(17,1),
(18,1),
(19,1),
(20,1),
(21,1),
(22,1),
(23,1),
(24,1),
(25,1),
(26,1),
(27,1),
(28,1),
(29,1),
(30,1),
(31,1),
(32,1),
(33,1),
(34,1),
(35,1),
(36,1),
(37,1),
(38,1),
(39,1),
(40,1),
(41,1),
(42,1),
(43,1),
(44,1),
(45,1),
(46,1),
(47,1),
(48,1),
(49,1),
(50,1),
(51,1),
(52,2),
(53,2),
(54,2),
(55,2);


DROP TABLE IF EXISTS `system_department`;
CREATE TABLE `system_department` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `department_name` varchar(64) NOT NULL COMMENT '部门名称',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `full_path` varchar(256) NOT NULL COMMENT '部门ID全路径',
  `parent_department_id` bigint NOT NULL COMMENT '上级部门ID',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  KEY `sd_pdi` (`parent_department_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门表';
INSERT INTO `system_department` VALUES (1,'总部','','1,',0,now());


DROP TABLE IF EXISTS `system_user_department_assign`;
CREATE TABLE `system_user_department_assign` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `system_department_id` bigint NOT NULL COMMENT '部门ID',
  `system_user_id` bigint NOT NULL COMMENT '用户ID',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  KEY `suda_sdi` (`system_department_id`),
  KEY `suda_ui` (`system_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户部门表';
INSERT INTO `system_user_department_assign` VALUES (1,1,1,now());


DROP TABLE IF EXISTS `system_user_oper_log`;
CREATE TABLE `system_user_oper_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `username` varchar(64) NOT NULL COMMENT '用户名',
  `realname` varchar(64) NOT NULL COMMENT '姓名',
  `module` varchar(32) NOT NULL COMMENT '操作模块',
  `content` varchar(5120) NOT NULL COMMENT '操作内容',
  `oper_type` varchar(32) NOT NULL COMMENT '操作类型',
  `oper_time` datetime NOT NULL COMMENT '操作时间[ADD-增，DELETE-删，MODIFY-改，SELECT-查]',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  KEY `suol_un` (`username`),
  KEY `suol_ot` (`oper_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户操作日志表';


DROP TABLE IF EXISTS `base_mobile_empty`;
CREATE TABLE `base_mobile_empty` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `mobile` varchar(16) DEFAULT NULL COMMENT '手机号码',
  `is_delete` tinyint DEFAULT '0' COMMENT '是否删除  0:未删除,1:已删除',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `U_C` (`mobile`,`is_delete`) USING BTREE,
  KEY `index_last_update_time` (`last_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='空号库表';


DROP TABLE IF EXISTS `base_mobile_portable`;
CREATE TABLE `base_mobile_portable` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `mobile` varchar(16) DEFAULT NULL COMMENT '手机号码',
  `operator_code` varchar(8) DEFAULT NULL COMMENT '运营商CODE[CM-移动，CU-联通，CT-电信]',
  `is_delete` tinyint DEFAULT '0' COMMENT '是否删除 0： 未删除 ，1：已删除',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `U_C` (`mobile`,`is_delete`),
  KEY `last_update_time` (`last_update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='携号转网号码';


DROP TABLE IF EXISTS `base_section_number_base`;
CREATE TABLE `base_section_number_base` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `number` varchar(16) DEFAULT NULL COMMENT '号段',
  `operator_code` varchar(8) DEFAULT NULL COMMENT '运营商CODE[CM-移动，CU-联通，CT-电信]',
  `is_delete` tinyint DEFAULT '0' COMMENT '是否删除 0： 未删除 ，1：已删除',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `U_C` (`number`,`is_delete`),
  KEY `LAST_UP` (`last_update_time`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='基础号段';
INSERT INTO `base_section_number_base` (`number`,operator_code,is_delete) VALUES 
('135','CM',0),
('136','CM',0),
('137','CM',0),
('138','CM',0),
('139','CM',0),
('147','CM',0),
('148','CM',0),
('150','CM',0),
('151','CM',0),
('152','CM',0),
('157','CM',0),
('158','CM',0),
('159','CM',0),
('178','CM',0),
('182','CM',0),
('183','CM',0),
('184','CM',0),
('187','CM',0),
('188','CM',0),
('198','CM',0),
('1440','CM',0),
('1703','CM',0),
('1705','CM',0),
('1706','CM',0),
('130','CU',0),
('131','CU',0),
('132','CU',0),
('155','CU',0),
('156','CU',0),
('185','CU',0),
('186','CU',0),
('145','CU',0),
('146','CU',0),
('166','CU',0),
('167','CU',0),
('175','CU',0),
('176','CU',0),
('1704','CU',0),
('1707','CU',0),
('1708','CU',0),
('1709','CU',0),
('133','CT',0),
('153','CT',0),
('177','CT',0),
('180','CT',0),
('181','CT',0),
('189','CT',0),
('191','CT',0),
('199','CT',0),
('1349','CT',0),
('1410','CT',0),
('1700','CT',0),
('1701','CT',0),
('1702','CT',0),
('1740','CT',0),
('171','CU',0),
('165','CM',0),
('172','CM',0),
('173','CT',0),
('149','CT',0),
('1340','CM',0),
('1341','CM',0),
('1342','CM',0),
('1343','CM',0),
('1344','CM',0),
('1345','CM',0),
('1346','CM',0),
('1347','CM',0),
('1348','CM',0),
('193','CT',0),
('162','CT',0),
('190','CT',0),
('196','CU',0),
('197','CM',0);


DROP TABLE IF EXISTS `base_section_number`;
CREATE TABLE `base_section_number` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `number` varchar(16) DEFAULT NULL COMMENT '号段',
  `province_code` varchar(16) DEFAULT NULL COMMENT '省份编码',
  `province_name` varchar(128) DEFAULT NULL COMMENT '省份名称',
  `city` varchar(128) DEFAULT NULL COMMENT '城市',
  `operator_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '运营商CODE[CM-移动，CU-联通，CT-电信]',
  `is_delete` tinyint DEFAULT '0' COMMENT '是否删除 0： 未删除 ，1：已删除',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `U_C_1` (`number`,`is_delete`),
  KEY `last_update_time` (`last_update_time`) USING BTREE,
  KEY `number` (`number`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='运营商号段';


DROP TABLE IF EXISTS `client`;
CREATE TABLE `client` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `client_name` varchar(128) DEFAULT NULL COMMENT '名称',
  `linkman` varchar(128) DEFAULT NULL COMMENT '联系人',
  `mobile` varchar(16) DEFAULT NULL COMMENT '手机号',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `address` varchar(100) DEFAULT NULL COMMENT '地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `operator_id` bigint DEFAULT NULL COMMENT '创建人',
  `balance` decimal(12,4) DEFAULT '0.0000' COMMENT '资金余额',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `is_balance_warning` tinyint DEFAULT '1' COMMENT '是否余额预警',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='客户';


DROP TABLE IF EXISTS `client_user_assign`;
CREATE TABLE `client_user_assign` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `client_id` bigint NOT NULL COMMENT '客戶ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  KEY `sura_ui` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='客戶用户关联';


DROP TABLE IF EXISTS `client_charge_record`;
CREATE TABLE `client_charge_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `client_id` bigint DEFAULT NULL COMMENT '客户ID',
  `charge_type` tinyint DEFAULT NULL COMMENT '类型：1-充值。2-扣费',
  `charge` decimal(12,4) DEFAULT NULL COMMENT '金额，有负数',
  `create_time` datetime DEFAULT NULL COMMENT '充值时间',
  `charge_user_id` bigint DEFAULT NULL COMMENT '充值操作人',
  `remark` varchar(255) DEFAULT NULL COMMENT '充值备注',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  KEY `I_CLIENT_CHARGE_RECORD_CHARGE_TYPE` (`charge_type`),
  KEY `I_CLIENT_CHARGE_RECORD_USER_ID` (`charge_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='充值扣费明细表';


DROP TABLE IF EXISTS `sms_app`;
CREATE TABLE `sms_app` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `client_id` bigint NOT NULL COMMENT '所属客户ID',
  `app_name` varchar(128) DEFAULT NULL COMMENT '应用名字',
  `app_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '鏈嶅姟鍙?',
  `app_type` varchar(32) NOT NULL COMMENT '应用类型(SMS-短信)',
  `app_key` varchar(32) NOT NULL COMMENT '应用标识',
  `app_secret` varchar(32) DEFAULT NULL COMMENT '应用密钥',
  `price` decimal(8,4) NOT NULL DEFAULT '0.1000' COMMENT '应用单价',
  `state` tinyint NOT NULL DEFAULT '0' COMMENT '应用状态(0-停用，1-启用)',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  KEY `sms_black_dictionary_last_update_time` (`last_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='应用';