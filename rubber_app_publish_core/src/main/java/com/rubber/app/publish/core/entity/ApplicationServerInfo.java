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
@TableName("t_application_server_info")
public class ApplicationServerInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "application_id", type = IdType.AUTO)
    private Integer applicationId;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 服务7器的唯一key值
     */
    private String serverKey;

    /**
     * 服务环境 100表示项目环境 200表示测试环境 30表示预发布环境
     */
    private Integer appEnv;

    /**
     * app的打包tag
     */
    private String appPackTag;

    /**
     * 当前服务状态 10表示初始化 20表示正常 30表示停用
     */
    private Integer appServerStatus;

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
