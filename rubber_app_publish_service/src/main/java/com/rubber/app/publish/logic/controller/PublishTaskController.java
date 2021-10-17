package com.rubber.app.publish.logic.controller;

import com.rubber.app.publish.core.entity.PublishTaskInfo;
import com.rubber.app.publish.core.service.IPublishTaskInfoService;
import com.rubber.app.publish.logic.service.task.AppPublishTaskService;
import com.rubber.base.components.mysql.plugins.admin.BaseAdminController;
import com.rubber.base.components.mysql.plugins.admin.page.PageModel;
import com.rubber.base.components.mysql.plugins.admin.page.SortType;
import com.rubber.common.utils.result.ResultMsg;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author luffyu
 * Created on 2021/8/29
 */
@RestController
@RequestMapping("/publish/push-task")
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
        pageModel.setSort(new String[]{"task_id"});
        pageModel.setOrder(SortType.desc);
        return ResultMsg.success(iPublishTaskInfoService.pageBySelect(pageModel, PublishTaskInfo.class, null));
    }


    /**
     *  新增任务
     */
    @PostMapping("/add")
    public ResultMsg addTask(@RequestBody PublishTaskInfo publishTaskInfo){
        appPublishTaskService.addTask(publishTaskInfo);
        return ResultMsg.success();
    }

    /**
     * 任务信息
     */
    @GetMapping("/info/{taskId}")
    public ResultMsg infoTask(@PathVariable("taskId")Integer taskId){
        return ResultMsg.success(appPublishTaskService.getTaskInfo(taskId));
    }

    /**
     * 任务信息
     */
    @PostMapping("/stop/{taskId}")
    public ResultMsg stopTask(@PathVariable("taskId")Integer taskId){
        appPublishTaskService.stopTask(taskId);
        return ResultMsg.success();
    }

    /**
     * 环境信息
     */
    @GetMapping("/env-info")
    public ResultMsg infoEnvTask(Integer taskId,Integer env){
        return ResultMsg.success(appPublishTaskService.getPushTaskInfo(taskId,env));
    }


    /**
     * 打包接口
     */
    @PostMapping("/pack/{taskId}")
    public ResultMsg startPack(@PathVariable("taskId")Integer taskId){
        return ResultMsg.success(appPublishTaskService.startPack(taskId));
    }


    /**
     * 查询任务状态
     */
    @PostMapping("/status/{taskId}")
    public ResultMsg taskStatus(@PathVariable("taskId")Integer taskId){
        return ResultMsg.success(appPublishTaskService.getTaskStatus(taskId));
    }

    /**
     * 打包日志
     */
    @GetMapping("/log/{taskId}")
    public ResultMsg taskLog(@PathVariable("taskId")Integer taskId){
        return ResultMsg.success(appPublishTaskService.getPackTaskLog(taskId));
    }


    /**
     * 推送jar包到服务器
     */
    @PostMapping("/push/{publishId}")
    public ResultMsg push(@PathVariable("publishId")Integer publishId){
        appPublishTaskService.pushPack(publishId);
        return ResultMsg.success();
    }

}
