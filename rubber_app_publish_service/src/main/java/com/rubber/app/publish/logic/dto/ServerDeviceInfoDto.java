package com.rubber.app.publish.logic.dto;

import com.rubber.app.publish.core.entity.ServerDeviceInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;

/**
 * @author luffyu
 * Created on 2021/9/10
 */
@Data
public class ServerDeviceInfoDto {

    /**
     * 自增id
     */
    private Integer id;

    /**
     * 服务设备的唯一key值
     */
    @NotNull
    private String serverKey;

    /**
     * 服务名称
     */
    @NotNull
    private String serverName;

    /**
     * 服务类型 jenkins表示打包服务器，app表示应用服务器 mysql表示db服务器 redis表示缓存服务器
     */
    @NotNull
    private String serverType;

    /**
     * 服务器ip
     */
    @NotNull
    private String serverIp;

    /**
     * 服务器端口
     */
    @NotNull
    private Integer serverShPort;

    /**
     * 服务器的用户
     */
    @NotNull
    private String serverShUser;

    /**
     * 服务器的密码
     */
    @NotNull
    private String serverShPsd;


    /**
     * 服务器的地址
     */
    @NotNull
    private String serverUrl;

    /**
     * 服务的登录账户名
     */
    @NotNull
    private String serverUser;

    /**
     * 服务的登录token
     */
    @NotNull
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



    public  ServerDeviceInfoDto(){

    }

    public ServerDeviceInfoDto(ServerDeviceInfo serverDeviceInfo){
        BeanUtils.copyProperties(serverDeviceInfo,this);
    }
}
