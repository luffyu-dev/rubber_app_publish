package com.rubber.app.publish.logic.manager.push.dto;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * @author luffyu
 * Created on 2021/9/25
 */
@Data
public class PushPackShScriptDto {

    private static final String BASE_SCRIPT = "scp -P ${targetShPort} /var/jenkins_home/workspace/${appPackPath} ${targetShUser}@${targetShIp}:/home/application/jar";

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * jar包名称
     */
    private String jarName;

    /**
     * 推送的model
     */
    private String publishModel;

    /**
     * jenkins的推送机器
     */
    private String jenkinsPushPath;


    /**
     * 目标机器的sh用户和ip
     */
    private String targetShUser;
    private String targetShIp;
    private Integer targetShPort;


    public PushPackShScriptDto(AppPushDto appPushDto, String targetShUser, String targetShIp,int targetShPort) {
        this.jobName = appPushDto.getJobName();
        this.publishModel = appPushDto.getPublishModel();
        this.jarName = appPushDto.getJarName();

        this.targetShUser = targetShUser;
        this.targetShIp = targetShIp;
        this.targetShPort = targetShPort;
    }

    public String execScript(){
        String sb = BASE_SCRIPT;
        sb = sb.replace("${targetShPort}",String.valueOf(targetShPort));
        sb = sb.replace("${appPackPath}",initPackPath());
        sb = sb.replace("${targetShUser}",targetShUser);
        sb = sb.replace("${targetShIp}",targetShIp);
        return sb;
    }

    public String initPackPath(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.jobName).append("/");
        if (StrUtil.isNotEmpty(this.publishModel)){
            sb.append(this.publishModel).append("/");
        }
        sb.append("target/").append(this.jarName);
        return sb.toString();
    }

}
