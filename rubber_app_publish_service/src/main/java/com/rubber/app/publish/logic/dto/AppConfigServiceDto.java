package com.rubber.app.publish.logic.dto;

import lombok.Data;

/**
 * @author luffyu
 * Created on 2021/9/22
 */
@Data
public class AppConfigServiceDto {

    /**
     * 当前一台应用的服务id
     */
    private Integer applicationId;

    /**
     * 应用名称
     */
    private String appName;


    /**
     * 服务环境 100表示项目环境 200表示测试环境 30表示预发布环境
     */
    private Integer appEnv;

    /**
     * app的打包tag
     */
    private String appPackTag;

    /**
     * 当前服务状态 10表示正常 20表示停用
     */
    private Integer appServerStatus;


    /**
     * 服务设备的唯一key值
     */
    private String serverKey;

    /**
     * 服务名称
     */
    private String serverName;


    /**
     * 服务器ip
     */
    private String serverIp;

    /**
     * 服务器端口
     */
    private Integer serverShPort;


}
