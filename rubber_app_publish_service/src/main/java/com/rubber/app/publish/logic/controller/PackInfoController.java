package com.rubber.app.publish.logic.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rubber.app.publish.core.constant.ErrCodeEnums;
import com.rubber.app.publish.core.entity.PackInfo;
import com.rubber.app.publish.core.exception.AppPublishParamException;
import com.rubber.app.publish.core.service.IPackInfoService;
import com.rubber.base.components.mysql.plugins.admin.BaseAdminController;
import com.rubber.base.components.mysql.plugins.admin.page.PageModel;
import com.rubber.base.components.mysql.plugins.admin.page.SortType;
import com.rubber.common.utils.result.ResultMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author luffyu
 * Created on 2021/10/17
 */
@RestController
@RequestMapping("/publish/pack-config")
public class PackInfoController extends BaseAdminController {

    @Autowired
    private IPackInfoService iPackInfoService;


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
        return ResultMsg.success(iPackInfoService.pageBySelect(pageModel, PackInfo.class, null));
    }



    /**
     * 查询全部的应用信息
     * @return 返回查询的信息
     */
    @GetMapping("/list-all")
    public ResultMsg listAll(){
        return ResultMsg.success(iPackInfoService.list());
    }




    /**
     * 保存信息
     * @param packInfo  保存的信息
     * @return 返回是否保存成功
     */
    @PostMapping("/add")
    public ResultMsg addAppConfig(@RequestBody PackInfo packInfo){
        iPackInfoService.save(packInfo);
        return ResultMsg.success();
    }



    /**
     * 更新信息
     * @param packInfo  保存的信息
     * @return 返回是否保存成功
     */
    @PostMapping("/update/{packName}")
    public ResultMsg updateAppConfig(@PathVariable("packName")String packName,@RequestBody PackInfo packInfo){
        if(StrUtil.isEmpty(packName)){
            throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"包不存在");
        }
        QueryWrapper<PackInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pack_name",packName);
        iPackInfoService.update(packInfo,queryWrapper);
        return ResultMsg.success();
    }

}
