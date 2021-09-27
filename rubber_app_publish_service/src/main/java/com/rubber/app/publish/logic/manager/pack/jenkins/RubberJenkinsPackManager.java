package com.rubber.app.publish.logic.manager.pack.jenkins;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.*;
import com.rubber.app.publish.core.constant.PushStatusEnums;
import com.rubber.app.publish.logic.manager.pack.RubberPackManager;
import com.rubber.app.publish.logic.manager.pack.config.JenkinsBeanServer;
import com.rubber.app.publish.logic.manager.pack.config.JenkinsServerProvider;
import com.rubber.app.publish.logic.manager.pack.dto.AppPackDto;
import com.rubber.app.publish.logic.manager.pack.dto.AppPackResponse;
import com.rubber.app.publish.logic.manager.pack.dto.AppPackStatusDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author luffyu
 * Created on 2021/8/22
 */
@Component
@Slf4j
public class RubberJenkinsPackManager implements RubberPackManager {


    @Resource
    private JenkinsServerProvider jenkinsServerProvider;


    /**
     * 打包的接口防范
     *
     * @param appPackDto 当前打包的基础信息
     * @return 返回打包信息
     */
    @Override
    public AppPackResponse pack(AppPackDto appPackDto) {
        AppPackResponse appPackResponse = new AppPackResponse();
        JenkinsBeanServer jenkinsBeanServer  =  jenkinsServerProvider.getJenkinsServer();
        JenkinsServer jenkinsServer = jenkinsBeanServer.getJenkinsServer();
        try {
            log.info("开始执行打包逻辑,packName={}",appPackDto.getAppName());
            //如果当前打包的app不存在，则创建一个打包的任务
            if (!isExist(jenkinsServer,appPackDto)) {
                log.info("开始执行的打包任务不存在，则创建一个任务,packName={}",appPackDto.getAppName());
                doCreateJob(jenkinsServer,appPackDto);
            }
            QueueReference queueReference = doBuildJob(jenkinsServer,appPackDto);
            if (queueReference != null){
                appPackResponse.setSuccess(true);
                appPackResponse.setJenkinsServerKey(jenkinsBeanServer.getServerKey());
                appPackResponse.setQueueItem(queueReference.getQueueItemUrlPart());
            }
        }catch (Exception e){
            //打包有异常的操作
            log.error("jenkins 打包异常");
        }
        return appPackResponse;
    }

    @Override
    public AppPackStatusDto getPackStatus(AppPackStatusDto appPackStatusDto) {
        AppPackStatusDto response = new AppPackStatusDto();
        JenkinsBeanServer jenkinsBeanServer  =  jenkinsServerProvider.getJenkinsServer(appPackStatusDto.getJenkinsServerKey());
        JenkinsServer jenkinsServer = jenkinsBeanServer.getJenkinsServer();
        if (PushStatusEnums.START_PACK.equals(appPackStatusDto.getPreStatus())){
            getStatusForStartPack(jenkinsServer,appPackStatusDto);
        }
        if (PushStatusEnums.PACKING.equals(appPackStatusDto.getPreStatus())){
            //打包状态，查看打包中的数据
            getPackResult(jenkinsServer,appPackStatusDto);
        }
        return response;
    }


    @Override
    public String getPackLog(AppPackStatusDto appPackStatusDto) {
        JenkinsBeanServer jenkinsBeanServer  =  jenkinsServerProvider.getJenkinsServer(appPackStatusDto.getJenkinsServerKey());
        JenkinsServer jenkinsServer = jenkinsBeanServer.getJenkinsServer();
        try {
            QueueItem queueItem = new QueueItem();
            Executable executable = new Executable();
            executable.setUrl(appPackStatusDto.getExecUrl());
            queueItem.setExecutable(executable);
            Build build = jenkinsServer.getBuild(queueItem);
            if (build != null) {
                BuildWithDetails buildWithDetails = build.details();
                return buildWithDetails.getConsoleOutputText();
            }
            return "";
        }catch (Exception e){
            return "";
        }
    }



    /**
     * 打包状态是否从队列中进入开始打包状态
     */
    private void getStatusForStartPack(JenkinsServer jenkinsServer,AppPackStatusDto appPackStatusDto){
        try {
            QueueItem queueItem = jenkinsServer.getQueueItem(new QueueReference(appPackStatusDto.getExecUrl()));
            if (queueItem.getExecutable() != null){
                log.info("当前jenkins状态又队列状转换成了打包,jenkinsServer={},url={}",appPackStatusDto.getJenkinsServerKey(),appPackStatusDto.getExecUrl());
                appPackStatusDto.setPreStatus(PushStatusEnums.PACKING);
                appPackStatusDto.setNowStatus(PushStatusEnums.PACKING);
                appPackStatusDto.setExecUrl(queueItem.getExecutable().getUrl());
                appPackStatusDto.setExecNum(queueItem.getExecutable().getNumber());
                log.info("用户已经进入到打包流程");
            }
        }catch (Exception e){
            log.error("查询状态失败");
        }
    }

    /**
     * 打包状态是否从队列中出来
     */
    private void getPackResult(JenkinsServer jenkinsServer,AppPackStatusDto appPackStatusDto){
        try {
            QueueItem queueItem = new QueueItem();
            Executable executable = new Executable();
            executable.setUrl(appPackStatusDto.getExecUrl());
            queueItem.setExecutable(executable);
            Build build = jenkinsServer.getBuild(queueItem);
            if (build != null){
                BuildWithDetails buildWithDetails = build.details();
                if (buildWithDetails != null){
                    BuildResult buildResult = buildWithDetails.getResult();
                    if (BuildResult.SUCCESS.equals(buildResult)){
                        //打包成功
                        appPackStatusDto.setPreStatus(PushStatusEnums.PACK_SUCCESS);
                        appPackStatusDto.setNowStatus(PushStatusEnums.PACK_SUCCESS);
                    }else if (BuildResult.FAILURE.equals(buildResult)){
                        appPackStatusDto.setPreStatus(PushStatusEnums.PACK_ERROR);
                        appPackStatusDto.setNowStatus(PushStatusEnums.PACK_ERROR);
                    }
                }
            }
        }catch (Exception e){
            log.error("查询状态失败");
        }

    }



    /**
     * 任务是否存在
     *
     * @param appPackDto
     */
    public boolean isExist(JenkinsServer jenkinsServer,AppPackDto appPackDto) {
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
    public void doCreateJob(JenkinsServer jenkinsServer,AppPackDto appPackDto) throws IOException {
        Document document = XmlUtil.readXML(new File("rubber_app_publish_service/src/main/resources/jenkinsTemplate.xml"));
        String xml = XmlUtil.toStr(document);
        xml = StringUtils.replace(xml,"${git_hub_url}",appPackDto.getGitHubUrl());
        jenkinsServer.createJob(appPackDto.getAppName(),xml);
    }

    /**
     * 构建任务
     *
     * @param appPackDto
     */
    public QueueReference doBuildJob(JenkinsServer jenkinsServer,AppPackDto appPackDto) throws IOException {
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
        return queueReference;
    }


    public static void main(String[] args) throws Exception {
        JenkinsServer jenkinsServer = new JenkinsServer(new URI("http://127.0.0.1:38080"), "admin", "11cbebe5da3a035294954afbc4d907d173");
        QueueItem queueItem = jenkinsServer.getQueueItem(new QueueReference("http://127.0.0.1:38080/queue/item/41"));

        Build build = jenkinsServer.getBuild(queueItem);
        JobWithDetails jobWithDetails = jenkinsServer.getJob("hotel_data_dock_project");
        Build build1 = jobWithDetails.getBuildByNumber(12);

        System.out.println(queueItem);
        String tt = "http://127.0.0.1:38080/job/hotel_data_dock_project/9/";
        int number = 9;


    }


}
