package com.rubber.app.publish.manager.pack.demo;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author luffyu
 * Created on 2021/8/22
 */
public class JenkinsConnect {

    // 连接 Jenkins 需要设置的信息
    static final String JENKINS_URL = "http://127.0.0.1:38080";
    static final String JENKINS_USERNAME = "admin";
    static final String JENKINS_PASSWORD = "11cbebe5da3a035294954afbc4d907d173";


    /**
     *
     * @return
     */
    public static JenkinsHttpClient getClient(){
        JenkinsHttpClient jenkinsHttpClient = null;
        try {
            jenkinsHttpClient = new JenkinsHttpClient(new URI(JENKINS_URL), JENKINS_USERNAME, JENKINS_PASSWORD);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return jenkinsHttpClient;
    }

    /**
     * 连接 Jenkins
     */
    public static JenkinsServer connection() {
        JenkinsServer jenkinsServer = null;
        try {
            jenkinsServer = new JenkinsServer(new URI(JENKINS_URL), JENKINS_USERNAME, JENKINS_PASSWORD);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return jenkinsServer;
    }

}
