package com.rubber.app.publish.logic.manager.pack.dto;

import lombok.Data;

/**
 * @author luffyu
 * Created on 2021/8/22
 */
@Data
public class AppPackDto {

    /**
     * job的名称
     */
    private String jobName;

    /**
     * 当前的app名称
     */
    private String appName;

    /**
     * github的地址
     */
    private String gitHubUrl;

    /**
     * git的打包tag
     */
    private String gitTag;

    /**
     * 打包的分支
     */
    private String gitBranch;

    /**
     * 打包的maven地址
     */
    private String packMavenPath = "pom.xml";


    /**
     * jenkins的服务
     */
    private String jenkinsServerKey;



}
