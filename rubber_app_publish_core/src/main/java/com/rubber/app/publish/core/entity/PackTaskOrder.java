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
 * 打包配置表
 * </p>
 *
 * @author luffyu
 * @since 2021-10-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_pack_task_order")
public class PackTaskOrder extends BaseEntity {

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
    private String packName;

    /**
     * app的打包tag
     */
    private String appPackTag;

    /**
     * 发布状态 包含了任务的创建和打包
     */
    private Integer taskStatus;

    /**
     * jenkins的服务
     */
    private String jenkinsServerKey;

    /**
     * 当前jar的版本
     */
    private String jarName;

    /**
     * 当前jar包版本
     */
    private String jarVersion;

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
