package com.rubber.app.publish.logic.service.task.impl;

import cn.hutool.core.collection.CollUtil;
import com.rubber.app.publish.core.constant.ErrCodeEnums;
import com.rubber.app.publish.core.constant.PushStatusEnums;
import com.rubber.app.publish.core.entity.*;
import com.rubber.app.publish.core.service.IApplicationConfigInfoService;
import com.rubber.app.publish.core.service.IApplicationPublishOrderService;
import com.rubber.app.publish.core.service.IApplicationServerInfoService;
import com.rubber.app.publish.core.service.IPublishTaskInfoService;
import com.rubber.app.publish.logic.dto.AppPublishTaskDto;
import com.rubber.app.publish.logic.dto.AppTaskInfoDto;
import com.rubber.app.publish.logic.exception.AppPublishException;
import com.rubber.app.publish.logic.manager.pack.dto.AppPackDto;
import com.rubber.app.publish.logic.manager.pack.dto.AppPackResponse;
import com.rubber.app.publish.logic.manager.pack.dto.AppPackStatusDto;
import com.rubber.app.publish.logic.manager.pack.jenkins.RubberJenkinsPackManager;
import com.rubber.app.publish.logic.service.app.AppManagerService;
import com.rubber.app.publish.logic.service.task.AppPublishTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.management.Query;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author luffyu
 * Created on 2021/8/29
 */
@Service
@Slf4j
public class AppPublishTaskServiceImpl implements AppPublishTaskService {


    @Resource
    private IPublishTaskInfoService iPublishTaskInfoService;

    @Resource
    private IApplicationServerInfoService iApplicationServerInfoService;

    @Resource
    private IApplicationPublishOrderService iApplicationPublishOrderService;

    @Resource
    private IApplicationConfigInfoService iApplicationConfigInfoService;

    @Resource
    private RubberJenkinsPackManager rubberJenkinsPackManager;

    @Autowired
    private AppManagerService appManagerService;

    /**
     * 添加一个任务
     *
     * @param publishTaskInfo
     */
    @Override
    @Transactional(
            rollbackFor = Exception.class
    )
    public void addTask(PublishTaskInfo publishTaskInfo) {
        //添加任务之前的操作
        doPerAddTask(publishTaskInfo);
        String[] envStr = publishTaskInfo.getAppEnv().split(",");
        Set<Integer> envs = new HashSet<>();
        for (String s:envStr){
            envs.add(Integer.valueOf(s));
        }
        List<ApplicationServerInfo>  applicationServerInfos = iApplicationServerInfoService.queryByAppNameAndEnv(publishTaskInfo.getAppName(),envs);
        if (CollUtil.isEmpty(applicationServerInfos)){
            throw new AppPublishException(ErrCodeEnums.DATA_IS_NOT_EXIST);
        }
        if(iPublishTaskInfoService.save(publishTaskInfo)){
            List<ApplicationPublishOrder> collect = applicationServerInfos.stream().map(i -> creatPublishOrder(i, publishTaskInfo)).collect(Collectors.toList());
            iApplicationPublishOrderService.saveBatch(collect);
        }
    }

    /**
     * 任务id 查询任务详情
     *
     * @param taskId
     */
    @Override
    public AppTaskInfoDto getTaskInfo(Integer taskId) {
        PublishTaskInfo publishTaskInfo = iPublishTaskInfoService.getById(taskId);
        if (publishTaskInfo == null){
            throw new AppPublishException(ErrCodeEnums.DATA_IS_NOT_EXIST);
        }
        AppTaskInfoDto appTaskInfoDto = new AppTaskInfoDto();
        BeanUtils.copyProperties(publishTaskInfo,appTaskInfoDto);
        //null表示查询所有环境的
        List<AppPublishTaskDto> pushTaskInfoList = getPushTaskInfo(publishTaskInfo, null);
        Map<Integer, List<AppPublishTaskDto>> collect = pushTaskInfoList.stream().collect(Collectors.groupingBy(AppPublishTaskDto::getAppEnv));
        appTaskInfoDto.setPublishOrderList(collect);
        return appTaskInfoDto;
    }

    /**
     * 按照任务id和环境查找信息信息
     *
     * @param taskId
     * @param env
     * @return
     */
    @Override
    public List<AppPublishTaskDto> getPushTaskInfo(Integer taskId, Integer env) {
        PublishTaskInfo publishTaskInfo = new PublishTaskInfo();
        publishTaskInfo.setTaskId(taskId);
        return getPushTaskInfo(taskId,env);
    }


    private List<AppPublishTaskDto> getPushTaskInfo(PublishTaskInfo publishTaskInfo, Integer env) {
        List<AppPublishTaskDto> appPublishTaskDtos = new ArrayList<>();
        List<ApplicationPublishOrder> applicationPublishOrders = iApplicationPublishOrderService.queryByTaskId(publishTaskInfo.getTaskId(),env);
        if (CollUtil.isNotEmpty(applicationPublishOrders)){
            Set<String> serverKeys = applicationPublishOrders.stream().map(ApplicationPublishOrder::getServerKey).collect(Collectors.toSet());
            Map<String, ServerDeviceInfo> appServerByServerKeys = appManagerService.getAppServerByServerKeys(serverKeys);

            for (ApplicationPublishOrder applicationPublishOrder:applicationPublishOrders){
                AppPublishTaskDto appPublishTaskDto = new AppPublishTaskDto();
                BeanUtils.copyProperties(applicationPublishOrder,appPublishTaskDto);
                ServerDeviceInfo serverDeviceInfo = appServerByServerKeys.get(applicationPublishOrder.getServerKey());
                if (serverDeviceInfo != null){
                    BeanUtils.copyProperties(serverDeviceInfo,appPublishTaskDto);
                }
                appPublishTaskDto.setAppName(publishTaskInfo.getAppName());
                appPublishTaskDtos.add(appPublishTaskDto);
            }
        }
        return appPublishTaskDtos;
    }

    /**
     * 开始打包
     *
     * @param taskId
     */
    @Override
    public AppPackResponse startPack(Integer taskId) {
        PublishTaskInfo publishTaskInfo = iPublishTaskInfoService.getById(taskId);
        if (publishTaskInfo == null){
            throw new AppPublishException(ErrCodeEnums.DATA_IS_NOT_EXIST);
        }
        ApplicationConfigInfo applicationConfigInfo = iApplicationConfigInfoService.getByAppName(publishTaskInfo.getAppName());
        if (applicationConfigInfo == null){
            throw new AppPublishException(ErrCodeEnums.DATA_IS_NOT_EXIST,"应用{}不存在",publishTaskInfo.getAppName());
        }
        AppPackResponse appPackResponse =  rubberJenkinsPackManager.pack(createAppPackDto(applicationConfigInfo,publishTaskInfo));
        if (appPackResponse.isSuccess()){
            publishTaskInfo.setTaskStatus(PushStatusEnums.START_PACK.getCode());
            publishTaskInfo.setJenkinsServerKey(appPackResponse.getJenkinsServerKey());
            publishTaskInfo.setJobUrl(appPackResponse.getQueueItem());
            iPublishTaskInfoService.updateById(publishTaskInfo);
            iApplicationPublishOrderService.updateStateByTaskId(taskId,PushStatusEnums.START_PACK.getCode());

        }
        return appPackResponse;
    }

    /**
     * 查询打包状态
     *
     * @param taskId 打包状态
     * @return
     */
    @Override
    public Integer getTaskStatus(Integer taskId) {
        PublishTaskInfo publishTaskInfo = iPublishTaskInfoService.getById(taskId);
        if (publishTaskInfo == null){
            throw new AppPublishException(ErrCodeEnums.DATA_IS_NOT_EXIST);
        }
        PushStatusEnums statusEnums = PushStatusEnums.getByCode(publishTaskInfo.getTaskStatus());
        if (statusEnums == null){
            return 0;
        }
        if (statusEnums.getCode() <  PushStatusEnums.WAIT_PUSH.getCode()){
            AppPackStatusDto appPackStatusDto = new AppPackStatusDto();
            appPackStatusDto.setPreStatus(statusEnums);
            appPackStatusDto.setJenkinsServerKey(publishTaskInfo.getJenkinsServerKey());
            appPackStatusDto.setExecUrl(publishTaskInfo.getJobUrl());
            rubberJenkinsPackManager.getPackStatus(appPackStatusDto);
            if (appPackStatusDto.getNowStatus() != null && appPackStatusDto.getNowStatus() != statusEnums){
                publishTaskInfo.setJobUrl(appPackStatusDto.getExecUrl());
                publishTaskInfo.setTaskStatus(appPackStatusDto.getNowStatus().getCode());
                iPublishTaskInfoService.updateById(publishTaskInfo);
                iApplicationPublishOrderService.updateStateByTaskId(taskId,appPackStatusDto.getNowStatus().getCode());
            }
        }
        return publishTaskInfo.getTaskStatus();
    }

    /**
     * 查询打包状态
     *
     * @param taskId 打包状态
     * @return
     */
    @Override
    public String getPackTaskLog(Integer taskId) {
        PublishTaskInfo publishTaskInfo = iPublishTaskInfoService.getById(taskId);
        if (publishTaskInfo == null){
            throw new AppPublishException(ErrCodeEnums.DATA_IS_NOT_EXIST);
        }
        AppPackStatusDto appPackStatusDto = new AppPackStatusDto();
        appPackStatusDto.setJenkinsServerKey(publishTaskInfo.getJenkinsServerKey());
        appPackStatusDto.setExecUrl(publishTaskInfo.getJobUrl());
        return rubberJenkinsPackManager.getPackLog(appPackStatusDto);
    }


    /**
     *  前置校验层
     */
    private void doPerAddTask(PublishTaskInfo publishTaskInfo){

    }


    private ApplicationPublishOrder creatPublishOrder(ApplicationServerInfo applicationServerInfo,PublishTaskInfo publishTaskInfo){
        ApplicationPublishOrder applicationPublishOrder = new ApplicationPublishOrder();
        applicationPublishOrder.setTaskId(publishTaskInfo.getTaskId());
        applicationPublishOrder.setApplicationId(applicationServerInfo.getApplicationId());
        applicationPublishOrder.setAppEnv(applicationServerInfo.getAppEnv());
        applicationPublishOrder.setServerKey(applicationServerInfo.getServerKey());
        applicationPublishOrder.setPublishStatus(PushStatusEnums.WAIT_PACK.getCode());
        LocalDateTime now = LocalDateTime.now();
        applicationPublishOrder.setCreateTime(now);
        applicationPublishOrder.setModifyTime(now);
        applicationPublishOrder.setVersion(1);
        return applicationPublishOrder;
    }

    private AppPackDto createAppPackDto(ApplicationConfigInfo applicationConfigInfo,PublishTaskInfo publishTaskInfo){
        AppPackDto appPackDto = new AppPackDto();
        appPackDto.setAppName(applicationConfigInfo.getAppName());
        appPackDto.setGitHubUrl(applicationConfigInfo.getGithubUrl());
        appPackDto.setPackMavenPath(applicationConfigInfo.getMavenPath());
        appPackDto.setGitTag(publishTaskInfo.getAppPackTag());
        return appPackDto;

    }
}
