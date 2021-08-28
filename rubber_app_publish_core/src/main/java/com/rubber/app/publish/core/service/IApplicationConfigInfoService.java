package com.rubber.app.publish.core.service;

import com.rubber.app.publish.core.entity.ApplicationConfigInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 应用配置表 服务类
 * </p>
 *
 * @author luffyu
 * @since 2021-08-27
 */
public interface IApplicationConfigInfoService extends IService<ApplicationConfigInfo> {


    /**
     * 通过appname查询一个服务
     * @param appName 当前的app名称
     * @return 返回一个app名称信息
     */
    ApplicationConfigInfo getByAppName(String appName);


    /**
     * 分配一个端口
     * @return 返回一个可用的端口信息
     */
    int assignAppPort();


}
