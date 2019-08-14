drop table if exists system_resource;
CREATE TABLE `system_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `resource_code` varchar(255) DEFAULT NULL COMMENT '资源CODE',
  `resource_name` varchar(255) DEFAULT NULL COMMENT '资源名称',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='资源表';

drop table if exists system_role_resource_assign;
CREATE TABLE `system_role_resource_assign` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `resource_id` bigint(20) DEFAULT NULL COMMENT '资源id',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色id',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='资源角色表';

drop table if exists system_role;
CREATE TABLE `system_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(64) DEFAULT NULL COMMENT '角色名',
  `remark` varchar(1024) DEFAULT NULL COMMENT '说明',
  `is_delete` tinyint(4) DEFAULT NULL COMMENT '是否删除0未删除(默认) 1已删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建者id',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='角色';

drop table if exists system_user_role_assign;
CREATE TABLE `system_user_role_assign` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建者id',
  PRIMARY KEY (`id`),
  KEY `index_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户角色关联';

drop table if exists system_user;
CREATE TABLE `system_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) NOT NULL COMMENT '用户名',
  `password` varchar(256) NOT NULL COMMENT '密码',
  `realname` varchar(64) NOT NULL COMMENT '姓名',
  `mobile` varchar(32) DEFAULT NULL COMMENT '手机',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `user_state` tinyint(4) NOT NULL COMMENT '状态[0-删除，1-停用，2-启用,3-锁定]',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `last_change_password_time` datetime DEFAULT NULL COMMENT '最后一次修改密码时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `operator_id` bigint(20) NOT NULL COMMENT '创建人',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='系统用户';

drop table if exists system_enterprise_department;
CREATE TABLE `system_enterprise_department` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `department_name` varchar(64) NOT NULL COMMENT '部门名称',
  `department_code` varchar(64) DEFAULT NULL COMMENT '部门编码',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `full_path` varchar(256) NOT NULL COMMENT '部门ID全路径',
  `enterprise_id` bigint(20) NOT NULL COMMENT '所属企业ID',
  `parent_department_id` bigint(20) NOT NULL COMMENT '上级部门ID',
  `is_delete` tinyint(4) NOT NULL COMMENT '是否删除  0:未删除,1:已删除',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  KEY `index_enterprise_id` (`enterprise_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='部门表';

drop table if exists system_user_department_assign;
CREATE TABLE `system_user_department_assign` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `enterprise_department_id` bigint(20) NOT NULL COMMENT '部门ID',
  `system_user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  KEY `UDA_ED_ID` (`enterprise_department_id`),
  KEY `UDA_U_ID` (`system_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户部门表';

drop table if exists system_user_oper_log;
CREATE TABLE `system_user_oper_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `user_name` varchar(128) NOT NULL COMMENT '用户名',
  `module` varchar(32) NOT NULL COMMENT '操作模块',
  `content` varchar(5120) NOT NULL COMMENT '操作内容',
  `oper_type` varchar(32) NOT NULL COMMENT '操作类型',
  `oper_time` datetime NOT NULL COMMENT '操作时间[ADD-增，DELETE-删，MODIFY-改，SELECT-查]',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  KEY `index_username` (`user_name`,`oper_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户操作日志表';

INSERT INTO `system_resource` VALUES ('1', 'USER_VIEW', '用户管理', now());
INSERT INTO `system_resource` VALUES ('2', 'USER_MODIFY', '用户修改', now());
INSERT INTO `system_resource` VALUES ('3', 'USER_ADD', '用户添加', now());
INSERT INTO `system_resource` VALUES ('4', 'USER_DELETE', '用户删除', now());
INSERT INTO `system_resource` VALUES ('5', 'USER_OPER', '用户操作', now());
INSERT INTO `system_resource` VALUES ('6', 'ROLE_VIEW', '角色管理', now());
INSERT INTO `system_resource` VALUES ('7', 'ROLE_MODIFY', '角色修改', now());
INSERT INTO `system_resource` VALUES ('8', 'ROLE_ADD', '角色添加', now());
INSERT INTO `system_resource` VALUES ('9', 'ROLE_DELETE', '角色删除', now());
INSERT INTO `system_resource` VALUES ('10', 'DEPARTMENT_VIEW', '部门管理', now());
INSERT INTO `system_resource` VALUES ('11', 'DEPARTMENT_ADD', '部门添加', now());
INSERT INTO `system_resource` VALUES ('12', 'DEPARTMENT_DELETE', '部门删除', now());
INSERT INTO `system_resource` VALUES ('13', 'DEPARTMENT_MODIFY', '部门修改', now());
INSERT INTO `system_resource` VALUES ('14', 'LOG_VIEW', '日志管理', now());

INSERT INTO `system_role_resource_assign` VALUES ('1', '1', '1', now());
INSERT INTO `system_role_resource_assign` VALUES ('2', '2', '1', now());
INSERT INTO `system_role_resource_assign` VALUES ('3', '3', '1', now());
INSERT INTO `system_role_resource_assign` VALUES ('4', '4', '1', now());
INSERT INTO `system_role_resource_assign` VALUES ('5', '5', '1', now());
INSERT INTO `system_role_resource_assign` VALUES ('6', '6', '1', now());
INSERT INTO `system_role_resource_assign` VALUES ('7', '7', '1', now());
INSERT INTO `system_role_resource_assign` VALUES ('8', '8', '1', now());
INSERT INTO `system_role_resource_assign` VALUES ('9', '9', '1', now());
INSERT INTO `system_role_resource_assign` VALUES ('10', '10', '1', now());
INSERT INTO `system_role_resource_assign` VALUES ('11', '11', '1', now());
INSERT INTO `system_role_resource_assign` VALUES ('12', '12', '1', now());
INSERT INTO `system_role_resource_assign` VALUES ('13', '13', '1', now());
INSERT INTO `system_role_resource_assign` VALUES ('14', '14', '1', now());

INSERT INTO `system_role` VALUES ('1', '管理员', '管理员', '0', now(), '1', now());
INSERT INTO `system_user` VALUES ('1', 'admin', '911E5C48DDC066CF6114138AC4FC26E5', '管理员', '13801000000', 'admin@emay.cn', '1', '管理', now(), now(), '1', now());
INSERT INTO `system_user_role_assign` VALUES ('1', '1', '1', now(), '1');
