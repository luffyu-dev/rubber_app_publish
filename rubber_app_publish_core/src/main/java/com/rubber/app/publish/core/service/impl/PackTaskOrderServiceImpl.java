package com.rubber.app.publish.core.service.impl;

import com.rubber.app.publish.core.entity.PackTaskOrder;
import com.rubber.app.publish.core.mapper.PackTaskOrderMapper;
import com.rubber.app.publish.core.service.IPackTaskOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rubber.base.components.mysql.plugins.admin.BaseAdminService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 打包配置表 服务实现类
 * </p>
 *
 * @author luffyu
 * @since 2021-10-17
 */
@Service
public class PackTaskOrderServiceImpl extends BaseAdminService<PackTaskOrderMapper, PackTaskOrder> implements IPackTaskOrderService {

}
