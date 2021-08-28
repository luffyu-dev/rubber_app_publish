create database rubber_oa_admin_db CHARACTER SET utf8 COLLATE utf8_general_ci;
use rubber_oa_admin_db;

CREATE TABLE t_server_device_info (
    id int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
    server_key varchar (50) NOT NULL default '' COMMENT '服务设备的唯一key值',
    server_name varchar (50) NOT NULL default '' COMMENT '服务名称',
    server_type varchar (50) NOT NULL default '' COMMENT '服务类型 jenkins表示打包服务器，app表示应用服务器 mysql表示db服务器 redis表示缓存服务器',
    server_ip varchar (20) NOT NULL default '0.0.0.0' COMMENT '服务器ip',
    server_port int(11) NOT NULL default '22' COMMENT '服务器端口',
    server_user varchar (50) NOT NULL default '' COMMENT '服务的登录账户名',
    server_token varchar (64) NOT NULL default '' COMMENT '服务的登录token',
    server_group_key   varchar (50) NOT NULL default '' COMMENT '机器所在组的key',
    server_address   varchar (255) NOT NULL default '' COMMENT '机器所在的地址',
    server_status int(3) not null  default 0 COMMENT '机器的状态 10表示正常 20表示停用 30表示负载',
    extend_params varchar(1024) not null default '' COMMENT '扩展参数信息',
    create_time datetime DEFAULT NULL COMMENT '创建时间',
    modify_time datetime DEFAULT NULL COMMENT '最后更新时间',
    version int(11) not null  default 0 COMMENT '版本号',
    PRIMARY KEY (id),
    KEY idx_server_key (server_key),
    KEY idx_server_ip_port_key (server_ip,server_port)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务设备详情表';



CREATE TABLE t_application_config_info (
    id int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
    app_name varchar (50) NOT NULL default '' COMMENT '应用服务名称',
    app_port smallint(6) NOT NULL default '8080' COMMENT '应用的端口',
    app_type varchar(20) NOT NULL default '0' COMMENT '应用类型 io内存型 cpu型',
    app_business_line varchar(50) NOT NULL default '' COMMENT '应用服务的业务线',

    github_url varchar (255) NOT NULL default '' COMMENT 'github的应用地址',
    maven_port varchar (255) NOT NULL default '' COMMENT '服务打包地址',
    jdk_version varchar (50) NOT NULL default '1.8' COMMENT 'jdk版本',
    deploy_path varchar (255) NOT NULL default '' COMMENT '部署路径',

    extend_params varchar(1024) not null default '' COMMENT '扩展参数信息',
    create_time datetime DEFAULT NULL COMMENT '创建时间',
    modify_time datetime DEFAULT NULL COMMENT '最后更新时间',
    version int(11) not null  default 0 COMMENT '版本号',
    PRIMARY KEY (id),
    KEY idx_app_name_key (app_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用配置表';



CREATE TABLE t_application_server_info (
    id int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
    app_name varchar (50) NOT NULL default '' COMMENT '应用名称',
    server_key varchar(50) NOT NULL default '22' COMMENT '服务7器的唯一key值',
    app_env smallint(6) NOT NULL default '100' COMMENT '服务环境 100表示项目环境 200表示测试环境 30表示预发布环境',
    app_pack_tag varchar (64) NOT NULL default 'master' COMMENT 'app的打包tag',
    app_server_status smallint(6) NOT NULL default '10' COMMENT '当前服务状态 10表示正常 20表示停用',
    push_status smallint(6) NOT NULL default '10' COMMENT '发布状态 10表示待打包  20表待推送  30表示待发布',

    extend_params varchar(1024) not null default '' COMMENT '扩展参数信息',
    create_time datetime DEFAULT NULL COMMENT '创建时间',
    modify_time datetime DEFAULT NULL COMMENT '最后更新时间',
    version int(11) not null  default 0 COMMENT '版本号',
    PRIMARY KEY (id),
    KEY idx_app_name_key (app_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用配置表';