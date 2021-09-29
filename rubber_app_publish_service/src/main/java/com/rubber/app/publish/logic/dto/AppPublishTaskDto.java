package com.rubber.app.publish.logic.dto;


import lombok.Data;


/**
 * @author luffyu
 * Created on 2021/8/29
 */
@Data
public class AppPublishTaskDto {

    /**
     * 任务的id
     */
    private Integer taskId;

    /**
     * 发布单id
     */
    private Integer publishId;

    /**
     * 需要发布的应用id
     */
    private Integer applicationId;

    /**
     * 发布状态
     */
    private Integer publishStatus;

    /**
     * 发布的参数
     */
    private String publishParams;



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
