package com.rubber.app.publish.logic.manager.push.dto;

import com.rubber.app.publish.logic.dto.ServerDeviceInfoDto;
import lombok.Data;

/**
 * @author luffyu
 * Created on 2021/8/22
 */
@Data
public class AppPushDto {

    /**
     * 分布任务的名称
     */
    private String jobName;

    /**
     * 应用名称
     */
    private String jarName;


    /**
     * 发布的子模块
     */
    private String publishModel;

    /**
     * jenkins打包的服务设备信息
     */
    private ServerDeviceInfoDto jenkinsDevice;


    /**
     * 目标应用的服务机器
     */
    private ServerDeviceInfoDto tagPushDevice;

}
