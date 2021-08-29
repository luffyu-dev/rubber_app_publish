
DROP TABLE IF EXISTS `t_application_config_info`;
CREATE TABLE `t_application_config_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `app_name` varchar(50) NOT NULL DEFAULT '' COMMENT '应用服务名称',
  `app_port` int(11) NOT NULL DEFAULT '8080' COMMENT '应用的端口',
  `app_type` varchar(20) NOT NULL DEFAULT '0' COMMENT '应用类型 io内存型 cpu型',
  `app_business_line` varchar(50) NOT NULL DEFAULT '' COMMENT '应用服务的业务线',
  `github_url` varchar(255) NOT NULL DEFAULT '' COMMENT 'github的应用地址',
  `maven_path` varchar(255) NOT NULL DEFAULT '' COMMENT '服务打包地址',
  `jdk_version` varchar(50) NOT NULL DEFAULT '1.8' COMMENT 'jdk版本',
  `deploy_path` varchar(255) NOT NULL DEFAULT '' COMMENT '部署路径',
  `extend_params` varchar(1024) NOT NULL DEFAULT '' COMMENT '扩展参数信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_app_port` (`app_type`) USING BTREE,
  KEY `idx_app_name_key` (`app_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='应用配置表';

-- ----------------------------
-- Table structure for t_application_server_info
-- ----------------------------
DROP TABLE IF EXISTS `t_application_server_info`;
CREATE TABLE `t_application_server_info` (
  `application_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '发布id，自增',
  `app_name` varchar(50) NOT NULL DEFAULT '' COMMENT '应用名称',
  `server_key` varchar(50) NOT NULL DEFAULT '22' COMMENT '服务7器的唯一key值',
  `app_env` smallint(6) NOT NULL DEFAULT '100' COMMENT '服务环境 100表示项目环境 200表示测试环境 30表示预发布环境',
  `app_pack_tag` varchar(64) NOT NULL DEFAULT 'master' COMMENT 'app的打包tag',
  `app_server_status` smallint(6) NOT NULL DEFAULT '10' COMMENT '当前服务状态 10表示正常 20表示停用',
  `extend_params` varchar(1024) NOT NULL DEFAULT '' COMMENT '扩展参数信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`application_id`) USING BTREE,
  KEY `idx_app_name_key` (`app_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='应用配置表';

-- ----------------------------
-- Table structure for t_server_device_info
-- ----------------------------
DROP TABLE IF EXISTS `t_server_device_info`;
CREATE TABLE `t_server_device_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `server_key` varchar(50) NOT NULL DEFAULT '' COMMENT '服务设备的唯一key值',
  `server_name` varchar(50) NOT NULL DEFAULT '' COMMENT '服务名称',
  `server_type` varchar(50) NOT NULL DEFAULT '' COMMENT '服务类型 jenkins表示打包服务器，app表示应用服务器 mysql表示db服务器 redis表示缓存服务器',
  `server_ip` varchar(20) NOT NULL DEFAULT '0.0.0.0' COMMENT '服务器ip',
  `server_sh_port` int(11) NOT NULL DEFAULT '22' COMMENT '服务器的sh端口',
  `server_sh_user` varchar(255) DEFAULT NULL COMMENT '服务器sh用户',
  `server_sh_psd` varchar(255) DEFAULT NULL COMMENT '服务器sh密码',
  `server_url` varchar(255) DEFAULT NULL COMMENT '服务的地址信息',
  `server_user` varchar(50) NOT NULL DEFAULT '' COMMENT '服务的登录账户名',
  `server_token` varchar(64) NOT NULL DEFAULT '' COMMENT '服务的登录token',
  `server_group_key` varchar(50) NOT NULL DEFAULT '' COMMENT '机器所在组的key',
  `server_address` varchar(255) NOT NULL DEFAULT '' COMMENT '机器所在的地址',
  `server_status` int(3) NOT NULL DEFAULT '0' COMMENT '机器的状态 10表示正常 20表示停用 30表示负载',
  `extend_params` varchar(1024) NOT NULL DEFAULT '' COMMENT '扩展参数信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`),
  KEY `idx_server_key` (`server_key`),
  KEY `idx_server_ip_port_key` (`server_ip`,`server_sh_port`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='服务设备详情表';


-- 发布任务
CREATE TABLE `t_publish_task_info` (
  `task_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '发布任务的id',
  `task_name` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '分布任务的名称',
  `app_name` varchar(50) NOT NULL DEFAULT '' COMMENT '应用名称',
  `app_env` varchar(6) NOT NULL DEFAULT '' COMMENT '服务环境 多个环境用引文逗号隔开',
  `app_pack_tag` varchar(64) NOT NULL DEFAULT 'master' COMMENT 'app的打包tag',
  `task_status` smallint(6) NOT NULL DEFAULT '10' COMMENT '发布状态 包含了任务的创建和打包',
  `job_url` smallint(6) NOT NULL DEFAULT '10' COMMENT '任务的url',
  `job_params` varchar(1024) NOT NULL DEFAULT '' COMMENT '任务参数',
  `operator` varchar(50) NOT NULL DEFAULT '' COMMENT '操作人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`task_id`) USING BTREE,
  KEY `idx_app_name_key` (`app_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='应用配置表';


-- 应用发布单
DROP TABLE IF EXISTS `t_application_publish_order`;
CREATE TABLE `t_application_publish_order` (
  `publish_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增发布id',
  `task_id` int(11) unsigned NOT NULL DEFAULT 0 COMMENT '任务的id',
  `application_id` int(11) unsigned NOT NULL DEFAULT 0  COMMENT '需要发布的应用id',
  `publish_status` smallint(6) NOT NULL DEFAULT '10' COMMENT '发布状态',
  `publish_params` varchar(1024) NOT NULL DEFAULT '' COMMENT '发布的参数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`publish_id`) USING BTREE,
  KEY `idx_app_name_key` (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='应用配置表';