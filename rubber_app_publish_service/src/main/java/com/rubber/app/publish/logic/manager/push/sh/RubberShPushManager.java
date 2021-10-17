package com.rubber.app.publish.logic.manager.push.sh;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.rubber.app.publish.core.constant.ErrCodeEnums;
import com.rubber.app.publish.logic.dto.ServerDeviceInfoDto;
import com.rubber.app.publish.logic.exception.AppPublishException;
import com.rubber.app.publish.logic.manager.push.RubberPushManager;
import com.rubber.app.publish.logic.manager.push.dto.AppPushDto;
import com.rubber.app.publish.logic.manager.push.dto.AppPushResult;
import com.rubber.app.publish.logic.manager.push.dto.PushPackShScriptDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author luffyu
 * Created on 2021/8/22
 */
@Slf4j
@Component
public class RubberShPushManager implements RubberPushManager {


    /**
     * 登录远端服务器
     *
     * @param ip       主机地址
     * @param userName 用户名
     * @param password 密码
     * @return 当前的连接
     * @throws IOException
     */
    public Connection login(String ip, Integer port,String userName, String password) throws IOException {
        Connection connection = new Connection(ip,port);
        connection.connect();
        return connection.authenticateWithPassword(userName, password) ? connection : null;
    }

    public Connection login(ServerDeviceInfoDto deviceInfoDto) throws IOException {
        Connection connection = new Connection(deviceInfoDto.getServerIp(),deviceInfoDto.getServerShPort());
        connection.connect();
        return connection.authenticateWithPassword(deviceInfoDto.getServerShUser(), deviceInfoDto.getServerShPsd()) ? connection : null;
    }


    /**
     * 执行脚本
     *
     * @param in      输入流
     * @param charset 字符编码
     * @return
     * @throws IOException
     */
    private String processStream(InputStream in, String charset) throws IOException {
        byte[] buf = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while (in.read(buf) != -1) {
            sb.append(new String(buf, charset));
        }
        return sb.toString();
    }


    /**
     * 推送接口
     *
     * @param appPushDto
     * @return
     */
    @Override
    public AppPushResult pushPackage(AppPushDto appPushDto) throws Exception {
        ServerDeviceInfoDto jenkinsDevice = appPushDto.getJenkinsDevice();
        ServerDeviceInfoDto tagPushDevice = appPushDto.getTagPushDevice();
        PushPackShScriptDto pushPackShScriptDto = new PushPackShScriptDto(appPushDto,tagPushDevice.getServerShUser(),tagPushDevice.getServerIp(),tagPushDevice.getServerShPort());
        //创建目录
        doMkdirTargetContents(pushPackShScriptDto,tagPushDevice);
        //上传jar包
        return doPushPack(pushPackShScriptDto,jenkinsDevice);
    }


    /**
     * 创建目标服务器的目录
     */
    private void doMkdirTargetContents(PushPackShScriptDto pushPackShScriptDto, ServerDeviceInfoDto tagPushDevice) throws IOException {
        InputStream stdOut = null;
        InputStream stdErr = null;
        Session targetSession = null;
        try {
            //登录幕布服务器 创建目录
            Connection targetConnection = login(tagPushDevice);
            if (targetConnection == null) {
                throw new AppPublishException("登录远程服务器出现异常,ip为:" + tagPushDevice.getServerIp());
            }
            targetSession = targetConnection.openSession();
            stdOut = new StreamGobbler(targetSession.getStdout());
            stdErr = new StreamGobbler(targetSession.getStderr());
            log.info("推送执行的脚本为:{}",pushPackShScriptDto.execCreatTargetContentScript());
            targetSession.execCommand(pushPackShScriptDto.execCreatTargetContentScript());
            log.info("脚本提交成功...");
            String outStr = processStream(stdOut, StandardCharsets.UTF_8.name());
            String outErr = processStream(stdErr, StandardCharsets.UTF_8.name());
            log.info("获取执行结果...succ={},err={}",outStr,outErr);
            if (targetSession.getExitStatus() == null || targetSession.getExitStatus() != 0){
                throw new AppPublishException(ErrCodeEnums.TASK_PUSH_ERROR,"在服务器{}创建文件目录失败",tagPushDevice.getServerKey());
            }
        }finally {
            if (stdErr != null){
                stdErr.close();
            }
            if (stdOut != null){
                stdOut.close();
            }
            if (targetSession != null){
                targetSession.close();
            }
        }
    }


    /**
     * 创建jar包
     * @param pushPackShScriptDto
     * @param jenkinsDevice
     * @return
     * @throws IOException
     */
    private AppPushResult doPushPack(PushPackShScriptDto pushPackShScriptDto, ServerDeviceInfoDto jenkinsDevice) throws IOException {
        InputStream stdOut = null;
        InputStream stdErr = null;
        Session session = null;
        AppPushResult appPushResult = new AppPushResult();
        try {
            //登录jenkins服务 上传服务
            Connection connection = login(jenkinsDevice);
            if (connection == null) {
                throw new AppPublishException("登录远程服务器出现异常,ip为:" + jenkinsDevice.getServerAddress());
            }
            session = connection.openSession();
            stdOut = new StreamGobbler(session.getStdout());
            stdErr = new StreamGobbler(session.getStderr());
            for (String s:pushPackShScriptDto.execScript()){
                log.info("推送执行的脚本为:{}", s);
                session.execCommand(s);
                log.info("执行脚本成功...");
                String outStr = processStream(stdOut, StandardCharsets.UTF_8.name());
                String outErr = processStream(stdErr, StandardCharsets.UTF_8.name());
                log.info("获取执行结果...succ={},err={}",outStr,outErr);
                if (session.getExitStatus() != null && session.getExitStatus() == 0){
                    appPushResult.setSuccess(true);
                    appPushResult.setPushTargetPath(pushPackShScriptDto.initTargetPath());
                    appPushResult.setPushJarName(pushPackShScriptDto.getJarName());
                }else {
                    appPushResult.setSuccess(false);
                }
            }
            appPushResult.setExecStatus(session.getExitStatus());
            return appPushResult;
        }finally {
            if (stdErr != null){
                stdErr.close();
            }
            if (stdOut != null){
                stdOut.close();
            }
            if (session != null){
                session.close();
            }
        }
    }



}
