package com.rubber.app.publish.core.service;

import com.rubber.app.publish.core.entity.ServerDeviceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务设备详情表 服务类
 * </p>
 *
 * @author luffyu
 * @since 2021-08-27
 */
public interface IServerDeviceInfoService extends IService<ServerDeviceInfo> {

    /**
     * 通过ip和端口查询
     * @param ip 当前的ip
     * @param port 当前的端口
     * @return 返回设备信息
     */
    ServerDeviceInfo getByIpPort(String ip,Integer port);

    /**
     * 通过serverKey来查询设备信息
     * @param serverKey 服务的key
     * @return 返回设备信息
     */
    ServerDeviceInfo getByServerKey(String serverKey);

}
