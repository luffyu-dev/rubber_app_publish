package com.rubber.app.publish.logic.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author luffyu
 * Created on 2021/9/22
 */
@Data
public class AppInfoDto {

    /**
     * 应用服务名称
     */
    private String appName;

    /**
     * 应用的端口
     */
    private Integer appPort;

    /**
     * 应用类型 io内存型 cpu型
     */
    private String appType;

    /**
     * 应用服务的业务线
     */
    private String appBusinessLine;

    /**
     * github的应用地址
     */
    private String githubUrl;

    /**
     * 服务打包地址
     */
    private String mavenPath;

    /**
     * jdk版本
     */
    private String jdkVersion;

    /**
     * 部署路径
     */
    private String deployPath;

    /**
     * 扩展参数信息
     */
    private String extendParams;

    /**
     * 设备服务详情
     */
    private Map<Integer, List<AppConfigServiceDto>> appServerList;

}
