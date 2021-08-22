package com.rubber.app.publish.manager.pack.demo;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.QueueReference;

import java.io.IOException;

/**
 * @author luffyu
 * Created on 2021/8/22
 */
public class JenkinsJobService {

    public JenkinsServer jenkinsServer;

    public JenkinsJobService() {
        jenkinsServer = JenkinsConnect.connection();
    }

    public static void main(String[] args) throws IOException {
        JenkinsJobService jenkinsJobService = new JenkinsJobService();
//        MavenJobWithDetails mavenJobWithDetails = jenkinsJobService.jenkinsServer.getMavenJob("demo");
//
//
//        String demoxml = jenkinsJobService.jenkinsServer.getJobXml("demo");
//        System.out.println(demoxml);

        //jenkinsJobService.jenkinsServer.createJob("demo4",demoxml);


        JobWithDetails jobWithDetails = jenkinsJobService.jenkinsServer.getJob("demo4");
        QueueReference queueReference = jobWithDetails.build();
        queueReference.getQueueItemUrlPart();

        System.out.println(jobWithDetails);
    }



}
