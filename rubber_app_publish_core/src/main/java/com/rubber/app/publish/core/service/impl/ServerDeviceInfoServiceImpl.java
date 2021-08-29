package com.rubber.app.publish.core.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rubber.app.publish.core.constant.ErrCodeEnums;
import com.rubber.app.publish.core.constant.ServerDeviceTypeEnums;
import com.rubber.app.publish.core.constant.ServerStatusEnums;
import com.rubber.app.publish.core.entity.ServerDeviceInfo;
import com.rubber.app.publish.core.exception.AppPublishParamException;
import com.rubber.app.publish.core.mapper.ServerDeviceInfoMapper;
import com.rubber.app.publish.core.service.IServerDeviceInfoService;
import com.rubber.base.components.mysql.plugins.admin.BaseAdminService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务设备详情表 服务实现类
 * </p>
 *
 * @author luffyu
 * @since 2021-08-27
 */
@Service
public class ServerDeviceInfoServiceImpl extends BaseAdminService<ServerDeviceInfoMapper, ServerDeviceInfo> implements IServerDeviceInfoService {


    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    public boolean save(ServerDeviceInfo entity) {
        doPreSave(entity);
        return super.save(entity);
    }


    /**
     * 根据 UpdateWrapper 条件，更新记录 需要设置sqlset
     *
     * @param updateWrapper 实体对象封装操作类 {@link UpdateWrapper}
     */
    @Override
    public boolean update(Wrapper<ServerDeviceInfo> updateWrapper) {
        doPerUpdate(updateWrapper);
        return super.update(updateWrapper);
    }



    /**
     * 通过ip和端口查询
     * @param ip 当前的ip
     * @param port 当前的端口
     * @return 返回设备信息
     */
    @Override
    public ServerDeviceInfo getByIpPort(String ip,Integer port){
        QueryWrapper<ServerDeviceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("server_ip",ip);
        queryWrapper.eq("server_port",port);
        return getOne(queryWrapper);
    }


    /**
     * 通过serverKey来查询设备信息
     */
    @Override
    public ServerDeviceInfo getByServerKey(String serverKey){
        QueryWrapper<ServerDeviceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("server_key",serverKey);
        return getOne(queryWrapper);
    }

    /**
     * 通过服务类型查询服务设备信息
     *
     * @param deviceTypeEnums 当前的服务类型
     * @return 返回服务设备信息
     */
    @Override
    public List<ServerDeviceInfo> queryByServerType(ServerDeviceTypeEnums deviceTypeEnums) {
        if (deviceTypeEnums == null){
            return null;
        }
        QueryWrapper<ServerDeviceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("server_type",deviceTypeEnums.getKey());
        return list(queryWrapper);
    }


    /**
     * 保存之前的操作
     */
    private void doPreSave(ServerDeviceInfo entity){
        if (StrUtil.isEmpty(entity.getServerIp()) || entity.getServerShPort() == null){
            throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"设备的ip和端口不能为空");
        }
        if (getByIpPort(entity.getServerIp(),entity.getServerShPort()) != null){
            throw new AppPublishParamException(ErrCodeEnums.DEVICE_IS_EXIST,"设备的ip{}和端口{}已经存在为空",entity.getServerIp(),entity.getServerShPort());
        }
        if (StrUtil.isEmpty(entity.getServerKey())){
            //doCreat
            entity.setServerKey(IdUtil.simpleUUID());
        }
        if (entity.getServerStatus() == null){
            entity.setServerStatus(ServerStatusEnums.getDefault().getCode());
        }
        if (entity.getCreateTime() != null){
            entity.setCreateTime(LocalDateTime.now());
        }
        entity.setModifyTime(entity.getCreateTime());
        entity.setVersion(0);
    }



    private void doPerUpdate(Wrapper<ServerDeviceInfo> updateWrapper){

    }



}
