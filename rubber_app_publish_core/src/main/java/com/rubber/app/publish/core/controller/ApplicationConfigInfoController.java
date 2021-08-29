package com.rubber.app.publish.core.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rubber.app.publish.core.constant.ErrCodeEnums;
import com.rubber.app.publish.core.entity.ApplicationConfigInfo;
import com.rubber.app.publish.core.exception.AppPublishParamException;
import com.rubber.app.publish.core.service.IApplicationConfigInfoService;
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
@RequestMapping("/publish/app-config")
public class ApplicationConfigInfoController extends BaseAdminController {

    @Resource
    private IApplicationConfigInfoService iApplicationConfigInfoService;



    /**
     * 分页查询
     * @param json 部门的查询信息的json字符串
     * @return 返回查询的信息
     */
    @GetMapping("/list")
    public ResultMsg list(String json){
        PageModel pageModel = decodeForJsonString(json);
        return ResultMsg.success(iApplicationConfigInfoService.pageBySelect(pageModel, ApplicationConfigInfo.class, null));
    }

    /**
     * 保存信息
     * @param applicationConfigInfo  保存的信息
     * @return 返回是否保存成功
     */
    @PostMapping("/add")
    public ResultMsg addAppConfig(@RequestBody ApplicationConfigInfo applicationConfigInfo){
        iApplicationConfigInfoService.save(applicationConfigInfo);
        return ResultMsg.success();
    }


    /**
     * 更新信息
     * @param applicationConfigInfo  保存的信息
     * @return 返回是否保存成功
     */
    @PostMapping("/update/{appName}")
    public ResultMsg updateAppConfig(@PathVariable("appName")String appName,@RequestBody ApplicationConfigInfo applicationConfigInfo){
        if(StrUtil.isEmpty(appName)){
            throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"部门id不存在");
        }
        QueryWrapper<ApplicationConfigInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_name",appName);
        iApplicationConfigInfoService.update(applicationConfigInfo,queryWrapper);
        return ResultMsg.success();
    }


    /**
     * 查询信息
     * @param appName app名称
     * @return 返回是否保存成功
     */
    @PostMapping("/info/{appName}")
    public ResultMsg infoAppName(@PathVariable("appName")String appName){
        if(StrUtil.isEmpty(appName)){
            throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"部门id不存在");
        }
        ApplicationConfigInfo applicationConfigInfo = iApplicationConfigInfoService.getByAppName(appName);
        return ResultMsg.success(applicationConfigInfo);
    }


}
