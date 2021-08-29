package com.rubber.app.publish.logic.manager.pack.config;

import com.offbytwo.jenkins.JenkinsServer;
import lombok.Data;

/**
 * @author luffyu
 * Created on 2021/8/29
 */
@Data
public class JenkinsBeanServer {

    private String serverKey;

    private JenkinsServer jenkinsServer;

    public JenkinsBeanServer() {
    }

    public JenkinsBeanServer(String serverKey, JenkinsServer jenkinsServer) {
        this.serverKey = serverKey;
        this.jenkinsServer = jenkinsServer;
    }
}
