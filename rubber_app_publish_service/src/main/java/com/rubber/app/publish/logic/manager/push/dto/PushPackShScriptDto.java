package com.rubber.app.publish.logic.manager.push.dto;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author luffyu
 * Created on 2021/9/25
 */
@Data
public class PushPackShScriptDto {

    //推送jar的版本
    private static final String BASE_PUSH_JAR_SCRIPT = "scp -P ${targetShPort} /var/jenkins_home/workspace/${appPackPath} ${targetShUser}@${targetShIp}:${target_path}";
    //推送脚本
    private static final String BASE_PUSH_STARTSH_SCRIPT = "scp -P ${targetShPort} /var/jenkins_home/workspace/${appPackPath}/script/bin.sh ${targetShUser}@${targetShIp}:${target_path}/script";

    /**
     * 目标父目录
     */
    private static final String TARGET_FATHER_PATH = "/home/application";

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * jar包名称
     */
    private String jarName;

    /**
     * app名称
     */
    private String appName;

    /**
     * 推送的版本
     */
    private String pushTag;

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
        this.appName = appPushDto.getAppName();
        this.pushTag = appPushDto.getPushTag();

        this.targetShUser = targetShUser;
        this.targetShIp = targetShIp;
        this.targetShPort = targetShPort;
    }

    public Set<String> execScript(){
        Set<String> sh = new HashSet<>();
        sh.add(getPushJarScript());
        return sh;
    }


    public String getPushJarScript(){
        String sb = BASE_PUSH_JAR_SCRIPT;
        sb = sb.replace("${targetShPort}",String.valueOf(targetShPort));
        sb = sb.replace("${appPackPath}",initPackPath() + this.jarName);
        sb = sb.replace("${targetShUser}",targetShUser);
        sb = sb.replace("${targetShIp}",targetShIp);
        sb = sb.replace("${target_path}",initTargetPath());
        return sb;
    }

    public String getPushJarStartShScript(){
        String sb = BASE_PUSH_STARTSH_SCRIPT;
        sb = sb.replace("${targetShPort}",String.valueOf(targetShPort));
        sb = sb.replace("${appPackPath}",initPackPath());
        sb = sb.replace("${targetShUser}",targetShUser);
        sb = sb.replace("${targetShIp}",targetShIp);
        sb = sb.replace("${target_path}",initTargetPath());
        return sb;
    }





    /**
     * 创建目标文件脚本
     */
    public String execCreatTargetContentScript(){
        return "mkdir -p " + initTargetPath() + "/script";
    }


    public String initTargetPath(){
        //return TARGET_FATHER_PATH;
        return TARGET_FATHER_PATH + "/" + this.appName +"/"+ this.pushTag ;
    }

    public String initPackPath(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.jobName).append("/");
        if (StrUtil.isNotEmpty(this.publishModel)){
            sb.append(this.publishModel).append("/");
        }
        sb.append("target/");
        return sb.toString();
    }

}
