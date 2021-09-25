package com.rubber.app.publish.core.service;

import com.rubber.app.publish.core.entity.ApplicationPublishOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rubber.base.components.mysql.plugins.admin.IBaseAdminService;

import java.util.List;

/**
 * <p>
 * 应用配置表 服务类
 * </p>
 *
 * @author luffyu
 * @since 2021-08-29
 */
public interface IApplicationPublishOrderService extends IBaseAdminService<ApplicationPublishOrder> {

    /**
     * 通过任务id查询
     */
    List<ApplicationPublishOrder> queryByTaskId(Integer taskId,Integer env);


    /**
     * 更具任务id更新全部的任务信息
     * @param taskId 当前taskId
     * @param taskStatus 当前的任务状态
     */
    void updateStateByTaskId(Integer taskId,Integer taskStatus);

}
