package com.rubber.app.publish.logic.manager.push.sh;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.rubber.app.publish.logic.dto.ServerDeviceInfoDto;
import com.rubber.app.publish.logic.exception.AppPublishException;
import com.rubber.app.publish.logic.manager.push.RubberPushManager;
import com.rubber.app.publish.logic.manager.push.dto.AppPushDto;
import com.rubber.app.publish.logic.manager.push.dto.PushPackShScriptDto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author luffyu
 * Created on 2021/8/22
 */
public class RubberShPushManager implements RubberPushManager {

    /** 超时时间 */
    private static final int TIME_OUT = 1000 * 5 * 60;


    /**
     * 登录远端服务器
     *
     * @param ip       主机地址
     * @param userName 用户名
     * @param password 密码
     * @return 当前的连接
     * @throws IOException
     */
    public static Connection login(String ip, Integer port,String userName, String password) throws IOException {
        Connection connection = new Connection(ip,port);
        connection.connect();
        return connection.authenticateWithPassword(userName, password) ? connection : null;
    }

    /**
     * 执行一个命令
     *
     * @param ip       主机ip
     * @param userName 用户名
     * @param password 密码
     * @param scripts  需要执行的脚本
     * @param charset  字符编码
     * @return ShellResult类
     * @throws Exception
     */
//    public static void exec(String ip, String userName, String password, String scripts, Charset charset) throws IOException {
//
//        Connection connection = login(ip, userName, password);
//        if (connection == null) {
//            throw new RuntimeException("登录远程服务器出现异常,ip为:" + ip);
//        }
//
//        // Open a new {@link Session} on this connection
//        Session session = connection.openSession();
//
//        try (InputStream stdOut = new StreamGobbler(session.getStdout()); InputStream stdErr = new StreamGobbler(session.getStderr())) {
//            session.execCommand(scripts);
//            String outStr = processStream(stdOut, charset.name());
//            String outErr = processStream(stdErr, charset.name());
//            session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
//            int exitStatus = session.getExitStatus();
//            System.out.println(outStr + ">>>>" + outErr + ">>>>" +  exitStatus);
//        }
//    }


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
    public String pushPackage(AppPushDto appPushDto) throws Exception {
        ServerDeviceInfoDto jenkinsDevice = appPushDto.getJenkinsDevice();
        //登录jenkins服务
        Connection connection = login(jenkinsDevice.getServerIp(),jenkinsDevice.getServerShPort(), jenkinsDevice.getServerShUser(), jenkinsDevice.getServerShPsd());
        if (connection == null) {
            throw new AppPublishException("登录远程服务器出现异常,ip为:" + jenkinsDevice.getServerAddress());
        }
        Session session = connection.openSession();
        InputStream stdOut = new StreamGobbler(session.getStdout());
        InputStream stdErr = new StreamGobbler(session.getStderr());

        ServerDeviceInfoDto tagPushDevice = appPushDto.getTagPushDevice();

        PushPackShScriptDto pushPackShScriptDto = new PushPackShScriptDto(appPushDto.getAppName(),tagPushDevice.getServerShUser(),tagPushDevice.getServerIp(),tagPushDevice.getServerShPort());
        System.out.println(pushPackShScriptDto.execScript());
        session.execCommand(pushPackShScriptDto.execScript());
        //session.execCommand("echo 1");
        String outStr = processStream(stdOut, StandardCharsets.UTF_8.name());
        String outErr = processStream(stdErr, StandardCharsets.UTF_8.name());

        System.out.println(outStr);
        System.out.println(outErr);
        return outStr;
    }


    public static void main(String[] args) throws Exception {

        AppPushDto appPushDto = new AppPushDto();
        appPushDto.setAppName("rubber_common_utils/common_utils/target/common_utils-1.0-SNAPSHOT.jar");

        ServerDeviceInfoDto serverDeviceInfoDto = new ServerDeviceInfoDto();
        serverDeviceInfoDto.setServerIp("1.116.15.151");
        serverDeviceInfoDto.setServerShPort(22001);
        serverDeviceInfoDto.setServerShUser("root");
        serverDeviceInfoDto.setServerShPsd("Ygf@337733");
        appPushDto.setJenkinsDevice(serverDeviceInfoDto);

        ServerDeviceInfoDto target = new ServerDeviceInfoDto();
        target.setServerIp("1.116.15.151");
        target.setServerShUser("root");
        target.setServerShPort(22);
        appPushDto.setTagPushDevice(target);

        RubberShPushManager rubberShPushManager = new RubberShPushManager();
        rubberShPushManager.pushPackage(appPushDto);

    }


}
