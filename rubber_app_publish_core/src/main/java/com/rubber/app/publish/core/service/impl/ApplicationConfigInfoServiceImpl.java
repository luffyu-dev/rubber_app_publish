package com.rubber.app.publish.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rubber.app.publish.core.constant.ApplicationGlobalConstant;
import com.rubber.app.publish.core.constant.ErrCodeEnums;
import com.rubber.app.publish.core.entity.ApplicationConfigInfo;
import com.rubber.app.publish.core.exception.AppPublishParamException;
import com.rubber.app.publish.core.mapper.ApplicationConfigInfoMapper;
import com.rubber.app.publish.core.service.IApplicationConfigInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rubber.base.components.mysql.plugins.admin.BaseAdminService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 应用配置表 服务实现类
 * </p>
 *
 * @author luffyu
 * @since 2021-08-27
 */
@Service
public class ApplicationConfigInfoServiceImpl extends BaseAdminService<ApplicationConfigInfoMapper, ApplicationConfigInfo> implements IApplicationConfigInfoService {


    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    public boolean save(ApplicationConfigInfo entity) {
        doPreSave(entity);
        return super.save(entity);
    }



    /**
     * 通过appname查询一个服务
     *
     * @param appName 当前的app名称
     * @return 返回一个app名称信息
     */
    @Override
    public ApplicationConfigInfo getByAppName(String appName) {
        QueryWrapper<ApplicationConfigInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_name",appName);
        return getOne(queryWrapper);
    }


    /**
     * 分配一个端口
     * todo 后面需要优化
     * @return 返回一个可用的端口信息
     */
    @Override
    public int assignAppPort() {
        QueryWrapper<ApplicationConfigInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("app_port");
        queryWrapper.orderByDesc("app_port");
        queryWrapper.last(" limit 1");
        ApplicationConfigInfo applicationConfigInfo = getOne(queryWrapper);
        if (applicationConfigInfo == null || applicationConfigInfo.getAppPort() == null){
            return ApplicationGlobalConstant.DEFAULT_APP_PORT;
        }
        return applicationConfigInfo.getAppPort() + 1;
    }

    /**
     * 更新的前置操作
     * @param entity 实体信息
     */
    private void doPreSave(ApplicationConfigInfo entity){
        if (StrUtil.isEmpty(entity.getAppName())){
            throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"应用名称不能为空");
        }
        if (getByAppName(entity.getAppName()) != null){
            throw new AppPublishParamException(ErrCodeEnums.DATA_IS_EXIST,"应用名称{}已经存在",entity.getAppName());
        }
        if (entity.getAppPort() == null){
            entity.setAppPort(assignAppPort());
        }
        if (StrUtil.isEmpty(entity.getGithubUrl())){
            throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"github地址不能为空");
        }
        if (StrUtil.isEmpty(entity.getJdkVersion())){
            entity.setJdkVersion(ApplicationGlobalConstant.DEFAULT_JDK_VERSION);
        }
        if (StrUtil.isEmpty(entity.getMavenPath())){
            entity.setMavenPath(ApplicationGlobalConstant.DEFAULT_MAVEN_PART);
        }
        if (entity.getCreateTime() != null){
            entity.setCreateTime(LocalDateTime.now());
        }
        entity.setModifyTime(entity.getCreateTime());
        entity.setVersion(0);
    }



}
