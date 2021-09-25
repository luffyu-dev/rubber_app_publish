package com.rubber.app.publish.logic.service.task;

import com.rubber.app.publish.core.entity.PublishTaskInfo;
import com.rubber.app.publish.logic.dto.AppPublishTaskDto;
import com.rubber.app.publish.logic.dto.AppTaskInfoDto;
import com.rubber.app.publish.logic.manager.pack.dto.AppPackResponse;

import java.util.List;

/**
 * @author luffyu
 * Created on 2021/8/29
 */
public interface AppPublishTaskService {

    /**
     * 添加一个任务
     */
    void addTask(PublishTaskInfo publishTaskInfo);


    /**
     * 任务id 查询任务详情
     */
    AppTaskInfoDto getTaskInfo(Integer taskId);


    /**
     * 按照任务id和环境查找信息信息
     * @param taskId
     * @param env
     * @return
     */
    List<AppPublishTaskDto> getPushTaskInfo(Integer taskId,Integer env);

    /**
     * 开始打包
     */
    AppPackResponse startPack(Integer taskId);

    /**
     * 查询打包状态
     * @param taskId 打包状态
     * @return
     */
    Integer getTaskStatus(Integer taskId);


    /**
     * 查询打包状态
     * @param taskId 打包状态
     * @return
     */
    String getPackTaskLog(Integer taskId);

}
