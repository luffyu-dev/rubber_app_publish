package com.rubber.app.publish.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.rubber.app.publish.core.constant.ErrCodeEnums;
import com.rubber.app.publish.core.constant.PushStatusEnums;
import com.rubber.app.publish.core.entity.ApplicationServerInfo;
import com.rubber.app.publish.core.exception.AppPublishParamException;
import com.rubber.app.publish.core.mapper.ApplicationServerInfoMapper;
import com.rubber.app.publish.core.service.IApplicationConfigInfoService;
import com.rubber.app.publish.core.service.IApplicationServerInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rubber.app.publish.core.service.IServerDeviceInfoService;
import com.rubber.common.utils.enums.EnvEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class ApplicationServerInfoServiceImpl extends ServiceImpl<ApplicationServerInfoMapper, ApplicationServerInfo> implements IApplicationServerInfoService {

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
        entity.setPushStatus(PushStatusEnums.WAIT_PACK.getCode());
        if (entity.getCreateTime() != null){
            entity.setCreateTime(LocalDateTime.now());
        }
        entity.setModifyTime(entity.getCreateTime());
        entity.setVersion(0);

    }


}
