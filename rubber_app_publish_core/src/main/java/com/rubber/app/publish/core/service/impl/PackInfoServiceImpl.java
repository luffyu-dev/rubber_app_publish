package com.rubber.app.publish.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rubber.app.publish.core.constant.ApplicationGlobalConstant;
import com.rubber.app.publish.core.constant.ErrCodeEnums;
import com.rubber.app.publish.core.entity.PackInfo;
import com.rubber.app.publish.core.exception.AppPublishParamException;
import com.rubber.app.publish.core.mapper.PackInfoMapper;
import com.rubber.app.publish.core.service.IPackInfoService;
import com.rubber.base.components.mysql.plugins.admin.BaseAdminService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 包配置表 服务实现类
 * </p>
 *
 * @author luffyu
 * @since 2021-10-17
 */
@Service
public class PackInfoServiceImpl extends BaseAdminService<PackInfoMapper, PackInfo> implements IPackInfoService {


    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    public boolean save(PackInfo entity) {
        doPreSave(entity);
        return super.save(entity);
    }

    /**
     * 通过appname查询一个服务
     *
     * @param packName 当前的包名称
     * @return 返回一个app名称信息
     */
    @Override
    public PackInfo getByPackName(String packName) {
        QueryWrapper<PackInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pack_name",packName);
        return getOne(queryWrapper);
    }

    /**
     * 更新的前置操作
     * @param entity 实体信息
     */
    private void doPreSave(PackInfo entity){
        if (StrUtil.isEmpty(entity.getPackName())){
            throw new AppPublishParamException(ErrCodeEnums.PARAM_ERROR,"包名称不能为空");
        }
        if (getByPackName(entity.getPackName()) != null){
            throw new AppPublishParamException(ErrCodeEnums.DATA_IS_EXIST,"包名称{}已经存在",entity.getPackName());
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
