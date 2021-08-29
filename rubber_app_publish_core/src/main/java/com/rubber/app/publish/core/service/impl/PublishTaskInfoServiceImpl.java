package com.rubber.app.publish.core.service.impl;

import com.rubber.app.publish.core.entity.PublishTaskInfo;
import com.rubber.app.publish.core.mapper.PublishTaskInfoMapper;
import com.rubber.app.publish.core.service.IPublishTaskInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rubber.base.components.mysql.plugins.admin.BaseAdminService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 应用配置表 服务实现类
 * </p>
 *
 * @author luffyu
 * @since 2021-08-29
 */
@Service
public class PublishTaskInfoServiceImpl extends BaseAdminService<PublishTaskInfoMapper, PublishTaskInfo> implements IPublishTaskInfoService {

}
