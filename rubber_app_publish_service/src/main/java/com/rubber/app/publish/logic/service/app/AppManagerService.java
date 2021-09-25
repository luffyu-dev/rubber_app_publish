package com.rubber.app.publish.logic.service.app;

import com.rubber.app.publish.core.entity.ServerDeviceInfo;
import com.rubber.app.publish.logic.dto.AppConfigServiceDto;
import com.rubber.app.publish.logic.dto.AppInfoDto;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author luffyu
 * Created on 2021/9/22
 */
public interface AppManagerService {


    /**
     * 通过appName查找app详情
     * @param name 当前的appName
     * @return 返回appName
     */
    AppInfoDto getAppInfo(String name);


    /**
     * 查询应用的全部设备信息
     * @param name 当前的名称
     * @param env 环境信息
     * @return 返回设备的信息
     */
    List<AppConfigServiceDto> queryAllActiveServer(String name,Integer env);


    /**
     * 通过服务的key值查询信息
     */
    Map<String, ServerDeviceInfo> getAppServerByServerKeys(Set<String> serverKey);



    /**
     * 服务扩容
     * @param appInfoDto
     */
    void capacityApp(AppInfoDto appInfoDto);


    /**
     * 服务缩容
     */
    void reduction(Integer applicationId);


    /**
     * 通过app的名称查询当前配置的环境
     */
    Map<Integer,String> queryAppServerEnv(String appName);

}
