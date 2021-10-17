package com.rubber.app.publish.logic.manager.pack.dto;

import lombok.Data;

/**
 * @author luffyu
 * Created on 2021/10/17
 */
@Data
public class MavenResolveDto {

    /**
     * github的应用地址
     */
    private String githubUrl;

    /**
     * 服务打包地址
     */
    private String mavenPath;

    /**
     * 推送的子模块
     */
    private String publishModel;

    /**
     * app的打包tag
     */
    private String appPackTag;



    /**
     * jar包的名称
     */
    private String jarName;

    /**
     * jar包版本
     */
    private String jarVersion;
}
