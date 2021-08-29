package com.rubber.app.publish.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rubber.app.publish.core.entity.ApplicationPublishOrder;
import com.rubber.app.publish.core.mapper.ApplicationPublishOrderMapper;
import com.rubber.app.publish.core.service.IApplicationPublishOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rubber.base.components.mysql.plugins.admin.BaseAdminService;
import org.springframework.stereotype.Service;

import javax.management.Query;
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
    public List<ApplicationPublishOrder> queryByTaskId(Integer taskId) {
        QueryWrapper<ApplicationPublishOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id",taskId);
        return list(queryWrapper);
    }
}
