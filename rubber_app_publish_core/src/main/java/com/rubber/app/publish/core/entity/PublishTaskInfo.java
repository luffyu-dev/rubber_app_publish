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
 * @since 2021-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_publish_task_info")
public class PublishTaskInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 发布任务的id
     */
    @TableId(value = "task_id", type = IdType.AUTO)
    private Integer taskId;

    /**
     * 分布任务的名称
     */
    private String taskName;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 服务环境 多个环境用引文逗号隔开
     */
    private String appEnv;

    /**
     * app的打包tag
     */
    private String appPackTag;

    /**
     * 发布状态 包含了任务的创建和打包
     */
    private Integer taskStatus;

    /**
     * 推送的子模块
     */
    private String publishModel;

    /**
     * jar包的名称
     */
    private String jarName;

    /**
     * jar包版本
     */
    private String jarVersion;


    /**
     * jenkins服务的key值
     */
    private String jenkinsServerKey;

    /**
     * 任务的url
     */
    private String jobUrl;

    /**
     * 任务参数
     */
    private String jobParams;

    /**
     * 操作人
     */
    private String operator;

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
