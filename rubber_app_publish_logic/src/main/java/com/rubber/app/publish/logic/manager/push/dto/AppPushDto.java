package com.rubber.app.publish.logic.manager.push.dto;

import lombok.Data;

/**
 * @author luffyu
 * Created on 2021/8/22
 */
@Data
public class AppPushDto {
    /**
     * app的版本
     */
    private String appName;

    /**
     * 打包的tag
     */
    private String gitTag;

    /**
     * 目标ip
     */
    private String targetIp;

    /**
     * 目标服务器的端口
     */
    private int targetPort = 20;

    /**
     * 目标服务器的用户
     */
    private String targetShUser;

    /**
     * 目标服务器的密码
     */
    private String targetShPassword;

    /**
     * 目标地址
     */
    private String targetPath;
}
