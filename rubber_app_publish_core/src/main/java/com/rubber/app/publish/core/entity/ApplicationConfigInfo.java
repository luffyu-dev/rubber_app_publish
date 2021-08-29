package com.rubber.app.publish.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.rubber.base.components.mysql.plugins.admin.bean.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 应用配置表
 * </p>
 *
 * @author luffyu
 * @since 2021-08-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_application_config_info")
public class ApplicationConfigInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 应用服务名称
     */
    private String appName;

    /**
     * 应用的端口
     */
    private Integer appPort;

    /**
     * 应用类型 io内存型 cpu型
     */
    private String appType;

    /**
     * 应用服务的业务线
     */
    private String appBusinessLine;

    /**
     * github的应用地址
     */
    private String githubUrl;

    /**
     * 服务打包地址
     */
    private String mavenPath;

    /**
     * jdk版本
     */
    private String jdkVersion;

    /**
     * 部署路径
     */
    private String deployPath;

    /**
     * 扩展参数信息
     */
    private String extendParams;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    private LocalDateTime modifyTime;

    /**
     * 版本号
     */
    @Version
    private Integer version;


}
