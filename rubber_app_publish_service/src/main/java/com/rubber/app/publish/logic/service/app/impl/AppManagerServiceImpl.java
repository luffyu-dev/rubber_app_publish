package com.rubber.app.publish.logic.service.app.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.rubber.app.publish.core.constant.ErrCodeEnums;
import com.rubber.app.publish.core.constant.ServerStatusEnums;
import com.rubber.app.publish.core.entity.ApplicationConfigInfo;
import com.rubber.app.publish.core.entity.ApplicationServerInfo;
import com.rubber.app.publish.core.entity.ServerDeviceInfo;
import com.rubber.app.publish.core.exception.AppPublishParamException;
import com.rubber.app.publish.core.service.IApplicationConfigInfoService;
import com.rubber.app.publish.core.service.IApplicationServerInfoService;
import com.rubber.app.publish.core.service.IServerDeviceInfoService;
import com.rubber.app.publish.logic.dto.AppConfigServiceDto;
import com.rubber.app.publish.logic.dto.AppInfoDto;
import com.rubber.app.publish.logic.service.app.AppManagerService;
import com.rubber.common.utils.enums.EnvEnum;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.util.ListHashMap;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author luffyu
 * Created on 2021/9/22
 */
@Slf4j
@Service("appManagerService")
public class AppManagerServiceImpl implements AppManagerService {

    @Resource
    private IApplicationConfigInfoService iApplicationConfigInfoService;

    @Resource
    private IApplicationServerInfoService iApplicationServerInfoService;

    @Resource
    private IServerDeviceInfoService iServerDeviceInfoService;

    /**
     * 通过appName查找app详情
     *
     * @param name 当前的appName
     * @return 返回appName
     */
    @Override
    public AppInfoDto getAppInfo(String name) {
        ApplicationConfigInfo applicationConfigInfo = iApplicationConfigInfoService.getByAppName(name);
        if (applicationConfigInfo == null){
            throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"应用不存在");
        }
        AppInfoDto appInfoDto = new AppInfoDto();
        BeanUtils.copyProperties(applicationConfigInfo,appInfoDto);
        List<AppConfigServiceDto> appConfigServiceDtos = queryAllActiveServer(name,null);
        Map<Integer, List<AppConfigServiceDto>> appServerList = appConfigServiceDtos.stream().collect(Collectors.groupingBy(AppConfigServiceDto::getAppEnv));
        appInfoDto.setAppServerList(appServerList);
        return appInfoDto;
    }

    /**
     * 查询应用的全部设备信息
     *
     * @param name 当前的名称
     * @return 返回设备的信息
     */
    @Override
    public List<AppConfigServiceDto> queryAllActiveServer(String name,Integer env) {
        Set<Integer> envSet = null;
        if (env != null){
            envSet = new HashSet<>();
            envSet.add(env);
        }
        List<AppConfigServiceDto> appConfigServiceDtos = new ArrayList<>();
        List<ApplicationServerInfo> appServerInfos = iApplicationServerInfoService.queryByAppNameAndEnv(name,envSet);
        if (appServerInfos != null){
            Set<String> serverKey = appServerInfos.stream().filter(i->StrUtil.isNotEmpty(i.getServerKey())).map(ApplicationServerInfo::getServerKey).collect(Collectors.toSet());
            Map<String,ServerDeviceInfo> deviceInfoMap = getByServerKeys(serverKey);

            for (ApplicationServerInfo appServerInfo:appServerInfos){
                AppConfigServiceDto appConfigServiceDto = new AppConfigServiceDto();
                BeanUtils.copyProperties(appServerInfo,appConfigServiceDto);
                ServerDeviceInfo serverDeviceInfo = deviceInfoMap.get(appServerInfo.getServerKey());
                if (serverDeviceInfo != null){
                    BeanUtils.copyProperties(serverDeviceInfo,appConfigServiceDto);
                }
                appConfigServiceDtos.add(appConfigServiceDto);
            }
        }
        return appConfigServiceDtos;
    }

    /**
     * 服务扩容
     *
     * @param appInfoDto
     */
    @Override
    public void capacityApp(AppInfoDto appInfoDto) {
        ApplicationConfigInfo applicationConfigInfo = iApplicationConfigInfoService.getByAppName(appInfoDto.getAppName());
        if (applicationConfigInfo == null){
            throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"应用不存在");
        }
        Map<Integer, List<AppConfigServiceDto>> appServerList = appInfoDto.getAppServerList();
        if (MapUtil.isEmpty(appServerList)){
            throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"至少选择一个扩容机器");
        }
        List<ApplicationServerInfo> applicationServerInfos = new ArrayList<>();
        for (Map.Entry<Integer,List<AppConfigServiceDto>> map:appServerList.entrySet()){
            Integer env = map.getKey();
            List<AppConfigServiceDto> capacityEnvList= map.getValue();
            if (CollUtil.isEmpty(capacityEnvList)) {
                throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"至少选择一个扩容机器");
            }
            capacityEnvList.forEach(i->applicationServerInfos.add(doCreateApplicationServerInfo(applicationConfigInfo,i,env)));
        }
        //写入成功之后
        iApplicationServerInfoService.saveBatch(applicationServerInfos);
        //执行初始化
    }

    /**
     * 服务缩容
     *
     * @param applicationId
     */
    @Override
    public void reduction(Integer applicationId) {
        ApplicationServerInfo applicationServerInfo = iApplicationServerInfoService.getById(applicationId);
        if (applicationServerInfo == null){
            throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"应用服务不存在");
        }
        applicationServerInfo.setAppServerStatus(ServerStatusEnums.RELEASE.getCode());
        applicationServerInfo.setModifyTime(LocalDateTime.now());
        iApplicationServerInfoService.updateById(applicationServerInfo);
        //执行释放逻辑
    }


    private ApplicationServerInfo doCreateApplicationServerInfo(ApplicationConfigInfo applicationConfigInfo,AppConfigServiceDto dto,Integer env){
        ApplicationServerInfo serverInfo = new ApplicationServerInfo();
        if (dto.getApplicationId() != null){
            throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"不支持修改applicationId:{}",dto.getApplicationId());
        }
        serverInfo.setAppName(applicationConfigInfo.getAppName());
        serverInfo.setServerKey(dto.getServerKey());
        serverInfo.setAppEnv(env);
        serverInfo.setAppServerStatus(ServerStatusEnums.INIT.getCode());
        serverInfo.setVersion(1);
        serverInfo.setCreateTime(LocalDateTime.now());
        serverInfo.setModifyTime(LocalDateTime.now());
        return serverInfo;
    }



    /**
     * 查询服务器的设备的相关信息
     */
    private Map<String,ServerDeviceInfo> getByServerKeys( Set<String> serverKey){
        Map<String,ServerDeviceInfo> map = new HashMap<>();
        if (CollUtil.isNotEmpty(serverKey)){
            String[] keys = new String[serverKey.size()];
            List<ServerDeviceInfo>  serverDeviceInfoList = iServerDeviceInfoService.queryByServerKeys(CollUtil.toList(serverKey.toArray(keys)));
            if (CollUtil.isNotEmpty(serverDeviceInfoList)){
                map = serverDeviceInfoList.stream().collect(Collectors.toMap(ServerDeviceInfo::getServerKey, i->i));
            }
        }
        return map;
    }
}
