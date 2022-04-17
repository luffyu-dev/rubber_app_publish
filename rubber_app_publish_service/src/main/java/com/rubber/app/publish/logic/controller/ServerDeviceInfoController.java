package com.rubber.app.publish.logic.controller;

import ch.ethz.ssh2.Connection;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rubber.app.publish.core.constant.ErrCodeEnums;
import com.rubber.app.publish.core.constant.ServerStatusEnums;
import com.rubber.app.publish.core.entity.ServerDeviceInfo;
import com.rubber.app.publish.core.exception.AppPublishParamException;
import com.rubber.app.publish.logic.dto.ServerDeviceInfoDto;
import com.rubber.app.publish.core.service.IServerDeviceInfoService;
import com.rubber.app.publish.logic.manager.push.sh.RubberShPushManager;
import com.rubber.base.components.mysql.plugins.admin.BaseAdminController;
import com.rubber.base.components.mysql.plugins.admin.page.PageModel;
import com.rubber.base.components.mysql.plugins.admin.page.SortType;
import com.rubber.base.components.util.result.ResultMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author luffyu
 * Created on 2021/8/28
 */
@Slf4j
@RestController
@RequestMapping("/publish/server-device")
public class ServerDeviceInfoController extends BaseAdminController {

    @Resource
    private IServerDeviceInfoService iServerDeviceInfoService;

    @Resource
    private RubberShPushManager rubberShPushManager;


    /**
     * 分页查询
     * @param json 部门的查询信息的json字符串
     * @return 返回查询的信息
     */
    @GetMapping("/list")
    public ResultMsg list(String json){
        PageModel pageModel = decodeForJsonString(json);
        pageModel.setSort(new String[]{"id"});
        pageModel.setOrder(SortType.desc);
        return ResultMsg.success(iServerDeviceInfoService.pageBySelect(pageModel, ServerDeviceInfo.class, null));
    }

    /**
     * 分页查询
     * @param json 部门的查询信息的json字符串
     * @return 返回查询的信息
     */
    @GetMapping("/list-app-all")
    public ResultMsg listAllApp(String json){
        QueryWrapper<ServerDeviceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("server_type","app");
        return ResultMsg.success(iServerDeviceInfoService.list(queryWrapper));
    }


    /**
     * 保存信息
     * @param serverDeviceInfoDto  保存的信息
     * @return 返回是否保存成功
     */
    @PostMapping("/add")
    public ResultMsg addAppConfig(@RequestBody ServerDeviceInfoDto serverDeviceInfoDto){
        ServerDeviceInfo serverDeviceInfo = new ServerDeviceInfo();
        BeanUtils.copyProperties(serverDeviceInfoDto,serverDeviceInfo);
        iServerDeviceInfoService.save(serverDeviceInfo);
        return ResultMsg.success();
    }


    /**
     * 更新信息
     * @param serverKey  保存的信息
     * @return 返回是否保存成功
     */
    @PostMapping("/update/{serverKey}")
    public ResultMsg updateAppConfig(@PathVariable("serverKey")String serverKey,@RequestBody ServerDeviceInfoDto serverDeviceInfoDto){
        if(StrUtil.isEmpty(serverKey)){
            throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"设备信息不存在");
        }
        ServerDeviceInfo serverDeviceInfo = new ServerDeviceInfo();
        BeanUtils.copyProperties(serverDeviceInfoDto,serverDeviceInfo);
        QueryWrapper<ServerDeviceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("server_key",serverKey);
        iServerDeviceInfoService.update(serverDeviceInfo,queryWrapper);
        return ResultMsg.success();
    }


    /**
     * 查询信息
     * @param serverKey 服务的唯一key值
     * @return 返回是否保存成功
     */
    @PostMapping("/info/{serverKey}")
    public ResultMsg infoAppName(@PathVariable("serverKey")String serverKey){
        if(StrUtil.isEmpty(serverKey)){
            throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"设备信息不存在");
        }
        ServerDeviceInfo serverDeviceInfo = iServerDeviceInfoService.getByServerKey(serverKey);
        return ResultMsg.success(serverDeviceInfo);
    }



    /**
     * 查询信息
     * @param serverKey
     * @return 返回是否保存成功
     */
    @PostMapping("/test-status/{serverKey}")
    public ResultMsg testServiceStatus(@PathVariable("serverKey")String serverKey){
        if(StrUtil.isEmpty(serverKey)){
            throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"设备信息不存在");
        }
        ServerDeviceInfo serverDeviceInfo = iServerDeviceInfoService.getByServerKey(serverKey);
        ServerStatusEnums serverStatusEnums = ServerStatusEnums.STOP;
        try {
            Connection connection = rubberShPushManager.login(serverDeviceInfo.getServerIp(), serverDeviceInfo.getServerShPort(),serverDeviceInfo.getServerShUser(), serverDeviceInfo.getServerShPsd());
            if(connection !=null && connection.isAuthenticationComplete()){
                serverStatusEnums = ServerStatusEnums.NORMAL;
            }
        }catch (Exception e){
            log.error("检测状态出现异常,e={}",e.getMessage());
        }
        if (!serverStatusEnums.getCode().equals(serverDeviceInfo.getServerStatus())){
            serverDeviceInfo.setServerStatus(serverStatusEnums.getCode());
            iServerDeviceInfoService.updateById(serverDeviceInfo);
        }
        return ResultMsg.success(serverStatusEnums.getCode());
    }
}
