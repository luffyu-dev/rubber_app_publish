package com.rubber.app.publish.core.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rubber.app.publish.core.constant.ErrCodeEnums;
import com.rubber.app.publish.core.entity.ApplicationConfigInfo;
import com.rubber.app.publish.core.entity.ApplicationServerInfo;
import com.rubber.app.publish.core.exception.AppPublishParamException;
import com.rubber.app.publish.core.service.IApplicationConfigInfoService;
import com.rubber.app.publish.core.service.IApplicationServerInfoService;
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
@RequestMapping("/publish/app-server")
public class ApplicationServerInfoController extends BaseAdminController {

    @Resource
    private IApplicationServerInfoService iApplicationServerInfoService;



    /**
     * 分页查询
     * @param json 部门的查询信息的json字符串
     * @return 返回查询的信息
     */
    @GetMapping("/list")
    public ResultMsg list(String json){
        PageModel pageModel = decodeForJsonString(json);
        return ResultMsg.success(iApplicationServerInfoService.pageBySelect(pageModel, ApplicationServerInfo.class, null));
    }

    /**
     * 保存信息
     * @param applicationServerInfo  保存的信息
     * @return 返回是否保存成功
     */
    @PostMapping("/add")
    public ResultMsg addAppConfig(@RequestBody ApplicationServerInfo applicationServerInfo){
        iApplicationServerInfoService.save(applicationServerInfo);
        return ResultMsg.success();
    }


    /**
     * 更新信息
     * @param applicationServerInfo  保存的信息
     * @return 返回是否保存成功
     */
    @PostMapping("/update/{appName}")
    public ResultMsg updateAppConfig(@PathVariable("appName")String appName,@RequestBody ApplicationServerInfo applicationServerInfo){
        if(StrUtil.isEmpty(appName)){
            throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"部门id不存在");
        }
        QueryWrapper<ApplicationServerInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_name",appName);
        iApplicationServerInfoService.update(applicationServerInfo,queryWrapper);
        return ResultMsg.success();
    }





}
