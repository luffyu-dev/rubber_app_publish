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
 * 服务设备详情表
 * </p>
 *
 * @author luffyu
 * @since 2021-08-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_server_device_info")
public class ServerDeviceInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 服务设备的唯一key值
     */
    private String serverKey;

    /**
     * 服务名称
     */
    private String serverName;

    /**
     * 服务类型 jenkins表示打包服务器，app表示应用服务器 mysql表示db服务器 redis表示缓存服务器
     */
    private String serverType;

    /**
     * 服务器ip
     */
    private String serverIp;

    /**
     * 服务器端口
     */
    private Integer serverShPort;

    /**
     * 服务器的用户
     */
    private String serverShUser;

    /**
     * 服务器的密码
     */
    private String serverShPsd;


    /**
     * 服务器的地址
     */
    private String serverUrl;

    /**
     * 服务的登录账户名
     */
    private String serverUser;

    /**
     * 服务的登录token
     */
    private String serverToken;

    /**
     * 机器所在组的key
     */
    private String serverGroupKey;

    /**
     * 机器所在的地址
     */
    private String serverAddress;

    /**
     * 机器的状态 10表示正常 20表示停用 30表示负载
     */
    private Integer serverStatus;

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
