package com.rubber.app.publish.core.service;

import com.rubber.app.publish.core.entity.ApplicationServerInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rubber.base.components.mysql.plugins.admin.IBaseAdminService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 应用配置表 服务类
 * </p>
 *
 * @author luffyu
 * @since 2021-08-27
 */
public interface IApplicationServerInfoService extends IBaseAdminService<ApplicationServerInfo> {


    /**
     * 查询相关配置信息
     */
    List<ApplicationServerInfo> queryByAppNameAndEnv(String appName, Set<Integer> env);
}
