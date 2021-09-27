package com.rubber.app.publish.logic.manager.push.dto;

import lombok.Data;

/**
 * @author luffyu
 * Created on 2021/9/25
 */
@Data
public class PushPackShScriptDto {

    private static final String BASE_SCRIPT = "scp -P ${targetShPort} /var/jenkins_home/workspace/${appPackPath} ${targetShUser}@${targetShIp}:/home/application/jar";

    private String appPackPath;

    private String targetShUser;

    private String targetShIp;

    private Integer targetShPort;


    public PushPackShScriptDto(String appPackPath, String targetShUser, String targetShIp,int targetShPort) {
        this.appPackPath = appPackPath;
        this.targetShUser = targetShUser;
        this.targetShIp = targetShIp;
        this.targetShPort = targetShPort;
    }

    public String execScript(){
        String sb = BASE_SCRIPT;
        sb = sb.replace("${targetShPort}",String.valueOf(targetShPort));
        sb = sb.replace("${appPackPath}",appPackPath);
        sb = sb.replace("${targetShUser}",targetShUser);
        sb = sb.replace("${targetShIp}",targetShIp);
        return sb;
    }

}
