package com.rubber.app.publish.manager.pack.jenkins;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.QueueReference;
import com.rubber.app.publish.manager.dto.AppPackDto;
import com.rubber.app.publish.manager.pack.RubberBasePackManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Struct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author luffyu
 * Created on 2021/8/22
 */
@Slf4j
public class RubberJenkinsPackManager extends RubberBasePackManager {


    public JenkinsServer jenkinsServer;


    public RubberJenkinsPackManager(JenkinsServer jenkinsServer){
        this.jenkinsServer = jenkinsServer;
    }


    /**
     * 任务是否存在
     *
     * @param appPackDto
     */
    @Override
    public boolean isExist(AppPackDto appPackDto) {
        try {
            JobWithDetails jobWithDetails = jenkinsServer.getJob(appPackDto.getAppName());
            return jobWithDetails != null;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建一个任务
     *
     * @param appPackDto
     */
    @Override
    public String doCreateJob(AppPackDto appPackDto) throws IOException {
        Document document = XmlUtil.readXML(new File("rubber_app_publish_manager/src/main/resources/jenkinsTemplate.xml"));
        String xml = XmlUtil.toStr(document);
        xml = StringUtils.replace(xml,"${git_hub_url}",appPackDto.getGitHubUrl());
        jenkinsServer.createJob(appPackDto.getAppName(),xml);
        return "success";
    }

    /**
     * 构建任务
     *
     * @param appPackDto
     */
    @Override
    public String doBuildJob(AppPackDto appPackDto) throws IOException {
        JobWithDetails jobWithDetails = jenkinsServer.getJob(appPackDto.getAppName());
        Map<String,String> buildParam = new HashMap<>(2);
        if (StrUtil.isNotEmpty(appPackDto.getGitTag()) || StrUtil.isNotEmpty(appPackDto.getGitBranch())){
            buildParam.put("tag",StrUtil.isNotEmpty(appPackDto.getGitTag()) ? appPackDto.getGitTag() : appPackDto.getGitBranch());
        }
        if (StrUtil.isNotEmpty(appPackDto.getPackMavenPath())){
            buildParam.put("pomPath",appPackDto.getPackMavenPath());
        }
        QueueReference queueReference;
        if (MapUtil.isNotEmpty(buildParam)){
            queueReference = jobWithDetails.build(buildParam);
        }else {
            queueReference = jobWithDetails.build();
        }
        return queueReference == null ? null : queueReference.getQueueItemUrlPart();
    }


    public static void main(String[] args) throws URISyntaxException {
        JenkinsServer jenkinsServer = new JenkinsServer(new URI("http://oa.jenkins.luffyu.cn"), "admin", "11d6d17b03e25d89254cf4e136c683901b");
        RubberJenkinsPackManager rubberJenkinsPackManager = new RubberJenkinsPackManager(jenkinsServer);
        AppPackDto appPackDto = new AppPackDto();
        appPackDto.setAppName("rubber_common_utils");
        appPackDto.setGitHubUrl("https://github.com/luffyu-dev/rubber_common_utils.git");
        rubberJenkinsPackManager.pack(appPackDto);
    }


}
