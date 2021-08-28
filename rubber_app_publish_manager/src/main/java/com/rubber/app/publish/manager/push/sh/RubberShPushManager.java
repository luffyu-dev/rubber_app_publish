package com.rubber.app.publish.manager.push.sh;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.rubber.app.publish.manager.push.RubberPushManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
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
    public static Connection login(String ip, String userName, String password) throws IOException {
        Connection connection = new Connection(ip,22001);
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
    public static void exec(String ip, String userName, String password, String scripts, Charset charset) throws IOException {

        Connection connection = login(ip, userName, password);
        if (connection == null) {
            throw new RuntimeException("登录远程服务器出现异常,ip为:" + ip);
        }

        // Open a new {@link Session} on this connection
        Session session = connection.openSession();

        try (InputStream stdOut = new StreamGobbler(session.getStdout()); InputStream stdErr = new StreamGobbler(session.getStderr())) {
            session.execCommand(scripts);
            String outStr = processStream(stdOut, charset.name());
            String outErr = processStream(stdErr, charset.name());
            session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
            int exitStatus = session.getExitStatus();
            System.out.println(outStr + ">>>>" + outErr + ">>>>" +  exitStatus);
        }
    }


    /**
     * 执行脚本
     *
     * @param in      输入流
     * @param charset 字符编码
     * @return
     * @throws IOException
     */
    private static String processStream(InputStream in, String charset) throws IOException {
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
     * @return
     */
    @Override
    public String push() {
        return null;
    }


    public static void main(String[] args) throws IOException {
        //onnection connection = login("1.116.15.151","root","Ygf@337733");
        //System.out.println(connection);
        exec("1.116.15.151","root","Ygf@337733","scp -p 22 ${filen} ${user}@${ip}:${dir}", StandardCharsets.UTF_8);
    }


}
