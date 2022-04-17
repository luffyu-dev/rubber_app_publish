package com.rubber.app.publish.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rubber.app.publish.core.constant.ErrCodeEnums;
import com.rubber.app.publish.core.entity.ApplicationServerInfo;
import com.rubber.app.publish.core.exception.AppPublishParamException;
import com.rubber.app.publish.core.mapper.ApplicationServerInfoMapper;
import com.rubber.app.publish.core.service.IApplicationConfigInfoService;
import com.rubber.app.publish.core.service.IApplicationServerInfoService;
import com.rubber.app.publish.core.service.IServerDeviceInfoService;
import com.rubber.base.components.mysql.plugins.admin.BaseAdminService;
import com.rubber.base.components.util.enums.EnvEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 应用配置表 服务实现类
 * </p>
 *
 * @author luffyu
 * @since 2021-08-27
 */
@Service
public class ApplicationServerInfoServiceImpl extends BaseAdminService<ApplicationServerInfoMapper, ApplicationServerInfo> implements IApplicationServerInfoService {

    @Resource
    private IServerDeviceInfoService iServerDeviceInfoService;

    @Resource
    private IApplicationConfigInfoService iApplicationConfigInfoService;

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    public boolean save(ApplicationServerInfo entity) {
        doPreSave(entity);
        return super.save(entity);
    }


    /**
     * 保存的前置处理
     * @param entity
     */
    private void doPreSave(ApplicationServerInfo entity){
        if (StrUtil.isEmpty(entity.getAppName())){
            throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"应用名称不能为空");
        }
        if (StrUtil.isEmpty(entity.getServerKey())){
            throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"服务器key不能为空");
        }
        if (EnvEnum.getByValue(entity.getAppEnv()) == null){
            throw new AppPublishParamException(ErrCodeEnums.DATA_IS_NOT_EXIST,"环境key值不存在");
        }
        if (StrUtil.isEmpty(entity.getAppPackTag())){
            throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"打包的tag不能为空");
        }
        if (iApplicationConfigInfoService.getByAppName(entity.getAppName()) == null){
            throw new AppPublishParamException(ErrCodeEnums.DATA_IS_NOT_EXIST,"应用{}不存在",entity.getAppName());
        }
        if (iServerDeviceInfoService.getByServerKey(entity.getServerKey()) == null){
            throw new AppPublishParamException(ErrCodeEnums.DATA_IS_NOT_EXIST,"服务资源{}不存在",entity.getAppName());
        }
        //entity.setPushStatus(PushStatusEnums.WAIT_PACK.getCode());
        if (entity.getCreateTime() != null){
            entity.setCreateTime(LocalDateTime.now());
        }
        entity.setModifyTime(entity.getCreateTime());
        entity.setVersion(0);

    }


    /**
     * 查询相关配置信息
     *
     * @param appName
     * @param env
     */
    @Override
    public List<ApplicationServerInfo> queryByAppNameAndEnv(String appName, Set<Integer> env) {
        QueryWrapper<ApplicationServerInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_name",appName);
        if (CollUtil.isNotEmpty(env)){
            queryWrapper.in("app_env",env);
        }
        return list(queryWrapper);
    }

    /**
     * 查询相关配置信息
     *
     * @param ids
     */
    @Override
    public List<ApplicationServerInfo> queryByIds(Set<Integer> ids) {
        QueryWrapper<ApplicationServerInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("application_id",ids);
        return list(queryWrapper);
    }
}
