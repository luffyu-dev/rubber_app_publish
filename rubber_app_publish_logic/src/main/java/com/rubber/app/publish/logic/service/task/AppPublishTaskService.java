package com.rubber.app.publish.logic.service.task;

import com.alibaba.fastjson.JSON;
import com.rubber.app.publish.core.entity.PublishTaskInfo;
import com.rubber.app.publish.logic.dto.AppPublishTaskDto;
import com.rubber.app.publish.logic.manager.pack.dto.AppPackResponse;

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
    AppPublishTaskDto getTaskInfo(Integer taskId);


    /**
     * 开始打包
     */
    AppPackResponse startPack(Integer taskId);

    /**
     * 查询打包状态
     * @param taskId 打包状态
     * @return
     */
    Integer getPackStatus(Integer taskId);

}
