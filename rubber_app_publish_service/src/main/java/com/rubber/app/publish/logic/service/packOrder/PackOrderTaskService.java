package com.rubber.app.publish.logic.service.packOrder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rubber.app.publish.core.constant.ErrCodeEnums;
import com.rubber.app.publish.core.constant.PushStatusEnums;
import com.rubber.app.publish.core.entity.ApplicationConfigInfo;
import com.rubber.app.publish.core.entity.PackInfo;
import com.rubber.app.publish.core.entity.PackTaskOrder;
import com.rubber.app.publish.core.entity.PublishTaskInfo;
import com.rubber.app.publish.core.service.IPackInfoService;
import com.rubber.app.publish.core.service.IPackTaskOrderService;
import com.rubber.app.publish.logic.exception.AppPublishException;
import com.rubber.app.publish.logic.manager.pack.MavenResolveManager;
import com.rubber.app.publish.logic.manager.pack.dto.AppPackDto;
import com.rubber.app.publish.logic.manager.pack.dto.AppPackResponse;
import com.rubber.app.publish.logic.manager.pack.dto.AppPackStatusDto;
import com.rubber.app.publish.logic.manager.pack.dto.MavenResolveDto;
import com.rubber.app.publish.logic.manager.pack.jenkins.RubberJenkinsPackManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author luffyu
 * Created on 2021/10/17
 */
@Slf4j
@Service
public class PackOrderTaskService {

    @Resource
    private IPackTaskOrderService iPackTaskOrderService;

    @Resource
    private IPackInfoService iPackInfoService;

    @Resource
    private MavenResolveManager mavenResolveManager;



    @Resource
    private RubberJenkinsPackManager rubberJenkinsPackManager;



    public void addPackOrderTask(PackTaskOrder packTaskOrder){
        doPerAddTask(packTaskOrder);
        iPackTaskOrderService.save(packTaskOrder);
    }


    public void stopTask(Integer taskId){
        PackTaskOrder packTaskOrder = iPackTaskOrderService.getById(taskId);
        if (packTaskOrder == null){
            throw new AppPublishException(ErrCodeEnums.DATA_IS_NOT_EXIST);
        }
        if (packTaskOrder.getTaskStatus() >= PushStatusEnums.PACK_SUCCESS.getCode()){
            throw new AppPublishException(ErrCodeEnums.TASK_SUCCESS);
        }
        packTaskOrder.setTaskStatus(PushStatusEnums.STOP.getCode());
        iPackTaskOrderService.updateById(packTaskOrder);
    }


    /**
     * 开始打包
     *
     * @param taskId
     */
    public AppPackResponse startPack(Integer taskId) {
        PackTaskOrder packTaskOrder = iPackTaskOrderService.getById(taskId);
        if (packTaskOrder == null){
            throw new AppPublishException(ErrCodeEnums.DATA_IS_NOT_EXIST);
        }
        PackInfo packInfo = iPackInfoService.getByPackName(packTaskOrder.getPackName());
        if (packInfo == null){
            throw new AppPublishException(ErrCodeEnums.DATA_IS_NOT_EXIST,"包{}不存在",packInfo);
        }
        AppPackResponse appPackResponse =  rubberJenkinsPackManager.pack(createAppPackDto(packInfo,packTaskOrder));
        if (appPackResponse.isSuccess()){
            packTaskOrder.setTaskStatus(PushStatusEnums.START_PACK.getCode());
            packTaskOrder.setJenkinsServerKey(appPackResponse.getJenkinsServerKey());
            packTaskOrder.setJobUrl(appPackResponse.getQueueItem());
            iPackTaskOrderService.updateById(packTaskOrder);
        }
        return appPackResponse;
    }

    /**
     * 查询打包状态
     *
     * @param taskId 打包状态
     * @return
     */
    public Integer getTaskStatus(Integer taskId) {
        PackTaskOrder packTaskOrder = iPackTaskOrderService.getById(taskId);
        if (packTaskOrder == null){
            throw new AppPublishException(ErrCodeEnums.DATA_IS_NOT_EXIST);
        }
        PushStatusEnums statusEnums = PushStatusEnums.getByCode(packTaskOrder.getTaskStatus());
        if (statusEnums == null){
            return 0;
        }
        if (statusEnums.getCode() <  PushStatusEnums.WAIT_PUSH.getCode()){
            AppPackStatusDto appPackStatusDto = new AppPackStatusDto();
            appPackStatusDto.setPreStatus(statusEnums);
            appPackStatusDto.setJenkinsServerKey(packTaskOrder.getJenkinsServerKey());
            appPackStatusDto.setExecUrl(packTaskOrder.getJobUrl());
            rubberJenkinsPackManager.getPackStatus(appPackStatusDto);
            if (appPackStatusDto.getNowStatus() != null && appPackStatusDto.getNowStatus() != statusEnums){
                packTaskOrder.setJobUrl(appPackStatusDto.getExecUrl());
                packTaskOrder.setTaskStatus(appPackStatusDto.getNowStatus().getCode());
                iPackTaskOrderService.updateById(packTaskOrder);
            }
        }
        return packTaskOrder.getTaskStatus();
    }

    /**
     * 查询打包状态
     *
     * @param taskId 打包状态
     * @return
     */
    public String getPackTaskLog(Integer taskId) {
        PackTaskOrder packTaskOrder = iPackTaskOrderService.getById(taskId);
        if (packTaskOrder == null){
            throw new AppPublishException(ErrCodeEnums.DATA_IS_NOT_EXIST);
        }
        AppPackStatusDto appPackStatusDto = new AppPackStatusDto();
        appPackStatusDto.setJenkinsServerKey(packTaskOrder.getJenkinsServerKey());
        appPackStatusDto.setExecUrl(packTaskOrder.getJobUrl());
        return rubberJenkinsPackManager.getPackLog(appPackStatusDto);
    }





    /**
     *  前置校验层
     */
    private void doPerAddTask(PackTaskOrder packTaskOrder){
        PackInfo oldOrder = iPackInfoService.getByPackName(packTaskOrder.getPackName());
        if (oldOrder == null){
            throw new AppPublishException(ErrCodeEnums.DATA_IS_NOT_EXIST);
        }
        packTaskOrder.setTaskName(packTaskOrder.getPackName() + "_" + packTaskOrder.getAppPackTag());

        MavenResolveDto mavenResolveDto = new MavenResolveDto();
        BeanUtils.copyProperties(packTaskOrder,mavenResolveDto);
        BeanUtils.copyProperties(oldOrder,mavenResolveDto);
        mavenResolveManager.resolveVersion(mavenResolveDto);
        packTaskOrder.setJarName(mavenResolveDto.getJarName());
        packTaskOrder.setJarVersion(mavenResolveDto.getJarVersion());
        JSONObject jb = new JSONObject();
        jb.put("modules",mavenResolveDto.getModules());
        packTaskOrder.setJobParams(jb.toJSONString());
        packTaskOrder.setTaskStatus(PushStatusEnums.WAIT_PACK.getCode());
    }


    private AppPackDto createAppPackDto(PackInfo packInfo, PackTaskOrder packTaskOrder){
        AppPackDto appPackDto = new AppPackDto();
        appPackDto.setJobName(packTaskOrder.getTaskName());
        appPackDto.setAppName(packInfo.getPackName());
        appPackDto.setGitHubUrl(packInfo.getGithubUrl());
        appPackDto.setPackMavenPath(packInfo.getMavenPath());
        appPackDto.setGitTag(packTaskOrder.getAppPackTag());
        appPackDto.setJenkinsServerKey(packTaskOrder.getJenkinsServerKey());
        return appPackDto;

    }
}
