package com.rubber.app.publish.logic.controller;

import com.rubber.app.publish.core.constant.ErrCodeEnums;
import com.rubber.app.publish.core.entity.PackTaskOrder;
import com.rubber.app.publish.core.exception.AppPublishParamException;
import com.rubber.app.publish.core.service.IPackTaskOrderService;
import com.rubber.app.publish.logic.service.packOrder.PackOrderTaskService;
import com.rubber.base.components.mysql.plugins.admin.BaseAdminController;
import com.rubber.base.components.mysql.plugins.admin.page.PageModel;
import com.rubber.base.components.mysql.plugins.admin.page.SortType;
import com.rubber.common.utils.result.ResultMsg;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author luffyu
 * Created on 2021/10/17
 */
@RestController
@RequestMapping("/publish/pack-order")
public class PackOrderController extends BaseAdminController {


    @Resource
    private IPackTaskOrderService iPackTaskOrderService;

    @Resource
    private PackOrderTaskService packOrderTaskService;

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
        return ResultMsg.success(iPackTaskOrderService.pageBySelect(pageModel, PackTaskOrder.class, null));
    }



    /**
     * 保存信息
     * @param packInfo  保存的信息
     * @return 返回是否保存成功
     */
    @PostMapping("/add")
    public ResultMsg save(@RequestBody PackTaskOrder packInfo){
        packOrderTaskService.addPackOrderTask(packInfo);
        return ResultMsg.success();
    }


    /**
     * 更新信息
     * @param packInfo  保存的信息
     * @return 返回是否保存成功
     */
    @PostMapping("/update/{taskId}")
    public ResultMsg updatePackOrder(@PathVariable("taskId")Integer taskId,@RequestBody PackTaskOrder packInfo){
        if(taskId == null || !taskId.equals(packInfo.getTaskId())){
            throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"任务不存在");
        }
        iPackTaskOrderService.updateById(packInfo);
        return ResultMsg.success();
    }


    /**
     * 更新信息
     * @param taskId  保存的信息
     * @return 返回是否保存成功
     */
    @GetMapping("/info/{taskId}")
    public ResultMsg getInfo(@PathVariable("taskId")Integer taskId){
        if(taskId == null){
            throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"任务不存在");
        }
        return ResultMsg.success(iPackTaskOrderService.getById(taskId));
    }



    /**
     * 更新信息
     * @param taskId  保存的信息
     * @return 返回是否保存成功
     */
    @PostMapping("/stop/{taskId}")
    public ResultMsg stopTask(@PathVariable("taskId")Integer taskId){
        if(taskId == null){
            throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"任务不存在");
        }
        packOrderTaskService.stopTask(taskId);
        return ResultMsg.success();
    }




    /**
     * 打包接口
     */
    @PostMapping("/pack/{taskId}")
    public ResultMsg startPack(@PathVariable("taskId")Integer taskId){
        return ResultMsg.success(packOrderTaskService.startPack(taskId));
    }


    /**
     * 查询任务状态
     */
    @PostMapping("/status/{taskId}")
    public ResultMsg taskStatus(@PathVariable("taskId")Integer taskId){
        return ResultMsg.success(packOrderTaskService.getTaskStatus(taskId));
    }

    /**
     * 打包日志
     */
    @GetMapping("/log/{taskId}")
    public ResultMsg taskLog(@PathVariable("taskId")Integer taskId){
        return ResultMsg.success(packOrderTaskService.getPackTaskLog(taskId));
    }



}
