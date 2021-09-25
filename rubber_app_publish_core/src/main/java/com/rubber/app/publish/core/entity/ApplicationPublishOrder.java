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
@TableName("t_application_publish_order")
public class ApplicationPublishOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 自增发布id
     */
    @TableId(value = "publish_id", type = IdType.AUTO)
    private Integer publishId;

    /**
     * 任务的id
     */
    private Integer taskId;

    /**
     * 需要发布的应用id
     */
    private Integer applicationId;

    /**
     * 发布环境
     */
    private Integer appEnv;


    /**
     * 关联的服务key值
     */
    private String serverKey;


    /**
     * 发布状态
     */
    private Integer publishStatus;

    /**
     * 发布的参数
     */
    private String publishParams;

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
