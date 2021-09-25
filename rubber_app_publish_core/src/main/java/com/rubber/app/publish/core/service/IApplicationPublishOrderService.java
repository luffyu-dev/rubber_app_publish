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



}
