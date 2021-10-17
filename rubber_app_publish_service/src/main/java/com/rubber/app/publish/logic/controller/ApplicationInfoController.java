package com.rubber.app.publish.logic.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rubber.app.publish.core.constant.ErrCodeEnums;
import com.rubber.app.publish.core.entity.ApplicationConfigInfo;
import com.rubber.app.publish.core.exception.AppPublishParamException;
import com.rubber.app.publish.core.service.IApplicationConfigInfoService;
import com.rubber.app.publish.logic.dto.AppInfoDto;
import com.rubber.app.publish.logic.service.app.AppManagerService;
import com.rubber.base.components.mysql.plugins.admin.BaseAdminController;
import com.rubber.base.components.mysql.plugins.admin.page.PageModel;
import com.rubber.base.components.mysql.plugins.admin.page.SortType;
import com.rubber.common.utils.result.ResultMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author luffyu
 * Created on 2021/8/28
 */
@RestController
@RequestMapping("/publish/app")
public class ApplicationInfoController extends BaseAdminController {

    @Resource
    private IApplicationConfigInfoService iApplicationConfigInfoService;

    @Resource
    private AppManagerService appManagerService;


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
        return ResultMsg.success(iApplicationConfigInfoService.pageBySelect(pageModel, ApplicationConfigInfo.class, null));
    }



    /**
     * 查询全部的应用信息
     * @return 返回查询的信息
     */
    @GetMapping("/list-all")
    public ResultMsg listAll(){
        return ResultMsg.success(iApplicationConfigInfoService.list());
    }


    /**
     * 查询全部的应用信息
     * @return 返回查询的信息
     */
    @GetMapping("/server-env/{appName}")
    public ResultMsg serverEnv(@PathVariable("appName")String appName){
        return ResultMsg.success(appManagerService.queryAppServerEnv(appName));
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
            throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"应用不存在");
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
    @GetMapping("/info/{appName}")
    public ResultMsg infoAppName(@PathVariable("appName")String appName){
        if(StrUtil.isEmpty(appName)){
            throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"应用不存在");
        }
        return ResultMsg.success(appManagerService.getAppInfo(appName));
    }


    /**
     * 查询信息
     * @param appName app名称
     * @return 返回是否保存成功
     */
    @GetMapping("/server-list")
    public ResultMsg serverList(String appName,Integer checkEnv){
        if(StrUtil.isEmpty(appName)){
            throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"应用不存在");
        }
        return ResultMsg.success(appManagerService.queryAllActiveServer(appName,checkEnv));
    }


    /**
     * 服务扩容
     */
    @PostMapping("/capacity")
    public ResultMsg capacityApp(@RequestBody AppInfoDto appInfoDto){
        appManagerService.capacityApp(appInfoDto);
        return ResultMsg.success();
    }

    /**
     * 服务缩容
     */
    @PostMapping("/reduction/{applicationId}")
    public ResultMsg reductionApp(@PathVariable("applicationId")Integer applicationId){
        appManagerService.reduction(applicationId);
        return ResultMsg.success();
    }



}
