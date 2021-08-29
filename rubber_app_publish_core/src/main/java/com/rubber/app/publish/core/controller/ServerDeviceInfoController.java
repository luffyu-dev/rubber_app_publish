package com.rubber.app.publish.core.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rubber.app.publish.core.constant.ErrCodeEnums;
import com.rubber.app.publish.core.entity.ApplicationConfigInfo;
import com.rubber.app.publish.core.entity.ApplicationServerInfo;
import com.rubber.app.publish.core.entity.ServerDeviceInfo;
import com.rubber.app.publish.core.exception.AppPublishParamException;
import com.rubber.app.publish.core.service.IApplicationServerInfoService;
import com.rubber.app.publish.core.service.IServerDeviceInfoService;
import com.rubber.base.components.mysql.plugins.admin.BaseAdminController;
import com.rubber.base.components.mysql.plugins.admin.page.PageModel;
import com.rubber.common.utils.result.ResultMsg;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author luffyu
 * Created on 2021/8/28
 */
@RestController
@RequestMapping("/publish/server-device")
public class ServerDeviceInfoController extends BaseAdminController {

    @Resource
    private IServerDeviceInfoService iServerDeviceInfoService;



    /**
     * 分页查询
     * @param json 部门的查询信息的json字符串
     * @return 返回查询的信息
     */
    @GetMapping("/list")
    public ResultMsg list(String json){
        PageModel pageModel = decodeForJsonString(json);
        return ResultMsg.success(iServerDeviceInfoService.pageBySelect(pageModel, ServerDeviceInfo.class, null));
    }

    /**
     * 保存信息
     * @param serverDeviceInfo  保存的信息
     * @return 返回是否保存成功
     */
    @PostMapping("/add")
    public ResultMsg addAppConfig(@RequestBody ServerDeviceInfo serverDeviceInfo){
        iServerDeviceInfoService.save(serverDeviceInfo);
        return ResultMsg.success();
    }


    /**
     * 更新信息
     * @param serverDeviceInfo  保存的信息
     * @return 返回是否保存成功
     */
    @PostMapping("/update/{serverKey}")
    public ResultMsg updateAppConfig(@PathVariable("serverKey")String serverKey,@RequestBody ServerDeviceInfo serverDeviceInfo){
        if(StrUtil.isEmpty(serverKey)){
            throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"设备信息不存在");
        }
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


}
