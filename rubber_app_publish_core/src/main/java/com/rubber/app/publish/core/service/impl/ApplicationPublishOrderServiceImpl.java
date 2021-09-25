package com.rubber.app.publish.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rubber.app.publish.core.entity.ApplicationPublishOrder;
import com.rubber.app.publish.core.mapper.ApplicationPublishOrderMapper;
import com.rubber.app.publish.core.service.IApplicationPublishOrderService;
import com.rubber.base.components.mysql.plugins.admin.BaseAdminService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 应用配置表 服务实现类
 * </p>
 *
 * @author luffyu
 * @since 2021-08-29
 */
@Service
public class ApplicationPublishOrderServiceImpl extends BaseAdminService<ApplicationPublishOrderMapper, ApplicationPublishOrder> implements IApplicationPublishOrderService {

    /**
     * 通过任务id查询
     */
    @Override
    public List<ApplicationPublishOrder> queryByTaskId(Integer taskId,Integer env) {
        QueryWrapper<ApplicationPublishOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id",taskId);
        if (env != null){
            queryWrapper.eq("app_env",env);
        }
        return list(queryWrapper);
    }

    /**
     * 更具任务id更新全部的任务信息
     *
     * @param taskId     当前taskId
     * @param taskStatus 当前的任务状态
     */
    @Override
    public void updateStateByTaskId(Integer taskId, Integer taskStatus) {
        QueryWrapper<ApplicationPublishOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id",taskId);
        ApplicationPublishOrder applicationPublishOrder = new ApplicationPublishOrder();
        applicationPublishOrder.setModifyTime(LocalDateTime.now());
        applicationPublishOrder.setPublishStatus(taskStatus);
        update(applicationPublishOrder,queryWrapper);
    }
}
