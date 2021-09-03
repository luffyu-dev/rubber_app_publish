package com.rubber.app.publish.logic.controller;

import com.rubber.app.publish.core.entity.PublishTaskInfo;
import com.rubber.app.publish.core.entity.ServerDeviceInfo;
import com.rubber.app.publish.core.service.IPublishTaskInfoService;
import com.rubber.app.publish.logic.service.task.AppPublishTaskService;
import com.rubber.base.components.mysql.plugins.admin.BaseAdminController;
import com.rubber.base.components.mysql.plugins.admin.page.PageModel;
import com.rubber.common.utils.result.ResultMsg;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author luffyu
 * Created on 2021/8/29
 */
@RestController
@RequestMapping("/publish/task")
public class PublishTaskController extends BaseAdminController {

    @Resource
    private AppPublishTaskService appPublishTaskService;

    @Resource
    private IPublishTaskInfoService iPublishTaskInfoService;


    /**
     * 分页查询
     * @param json 部门的查询信息的json字符串
     * @return 返回查询的信息
     */
    @GetMapping("/list")
    public ResultMsg list(String json){
        PageModel pageModel = decodeForJsonString(json);
        return ResultMsg.success(iPublishTaskInfoService.pageBySelect(pageModel, PublishTaskInfo.class, null));
    }


    @PostMapping("/add")
    public ResultMsg addTask(@RequestBody PublishTaskInfo publishTaskInfo){
        appPublishTaskService.addTask(publishTaskInfo);
        return ResultMsg.success();
    }

    @GetMapping("/info/{taskId}")
    public ResultMsg infoTask(@PathVariable("taskId")Integer taskId){
        return ResultMsg.success(appPublishTaskService.getTaskInfo(taskId));
    }


    @PostMapping("/pack/{taskId}")
    public ResultMsg startPack(@PathVariable("taskId")Integer taskId){
        return ResultMsg.success(appPublishTaskService.startPack(taskId));
    }


    @PostMapping("/status/{taskId}")
    public ResultMsg taskStatus(@PathVariable("taskId")Integer taskId){
        return ResultMsg.success(appPublishTaskService.getTaskStatus(taskId));
    }


    @PostMapping("/log/{taskId}")
    public ResultMsg taskLog(@PathVariable("taskId")Integer taskId){
        return ResultMsg.success(appPublishTaskService.getPackTaskLog(taskId));
    }

}
