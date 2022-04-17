package com.rubber.app.publish.logic.manager.pack.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.offbytwo.jenkins.JenkinsServer;
import com.rubber.app.publish.core.constant.ServerDeviceTypeEnums;
import com.rubber.app.publish.core.entity.ApplicationConfigInfo;
import com.rubber.app.publish.core.entity.ApplicationServerInfo;
import com.rubber.app.publish.core.entity.ServerDeviceInfo;
import com.rubber.app.publish.core.service.IServerDeviceInfoService;
import com.rubber.app.publish.logic.manager.pack.dto.AppPackDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luffyu
 * Created on 2021/8/29
 */
@Slf4j
@Component
public class JenkinsServerProvider implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    Map<String, JenkinsBeanServer> jenkinsServerProvider = new HashMap<>();

    private static final String LOCK_KEY = "lock_key";

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        JenkinsServerProvider.applicationContext = applicationContext;
        initJenkinsService();
    }

    /**
     * 返回一个jenkins服务器
     */
    public JenkinsBeanServer getJenkinsServer(){
        return getJenkinsServer(null);
    }
    public JenkinsBeanServer getJenkinsServer(String key){
        if (CollUtil.isEmpty(jenkinsServerProvider)){
            synchronized (LOCK_KEY){
                if (CollUtil.isEmpty(jenkinsServerProvider)){
                    initJenkinsService();
                }
            }
        }
        if (CollUtil.isEmpty(jenkinsServerProvider)){
            return null;
        }
        if (StrUtil.isEmpty(key)){
            return getRandomOne();
        }else {
            return jenkinsServerProvider.get(key);
        }
    }


    /**
     * 初始化jenkins服务
     */
    public synchronized void initJenkinsService(){
        jenkinsServerProvider.clear();
        IServerDeviceInfoService iServerDeviceInfoService = applicationContext.getBean(IServerDeviceInfoService.class);
        List<ServerDeviceInfo> serverDeviceInfoList = iServerDeviceInfoService.queryByServerType(ServerDeviceTypeEnums.JENKINS);
        if (CollUtil.isEmpty(serverDeviceInfoList)){
            log.error("没有配置jenkins服务器");
            return;
        }
        for (ServerDeviceInfo serverDeviceInfo:serverDeviceInfoList){
            try {
                JenkinsServer jenkinsServer = new JenkinsServer(new URI(serverDeviceInfo.getServerUrl()), serverDeviceInfo.getServerUser(), serverDeviceInfo.getServerToken());
                jenkinsServerProvider.put(serverDeviceInfo.getServerKey(),new JenkinsBeanServer(serverDeviceInfo.getServerKey(),jenkinsServer));
            }catch (Exception e){
                log.error("初始化Jenkins的服务异常-serverKey={},serverUrl = {}",serverDeviceInfo.getServerName(),serverDeviceInfo.getServerUrl());
            }
        }
    }


    /**
     * 随机返回一个服务
     */
    private JenkinsBeanServer getRandomOne(){
        return jenkinsServerProvider.values().iterator().next();
    }

}
