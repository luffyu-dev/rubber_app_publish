package com.rubber.app.publish.core.service;

import com.rubber.app.publish.core.entity.PackInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rubber.base.components.mysql.plugins.admin.IBaseAdminService;

/**
 * <p>
 * 包配置表 服务类
 * </p>
 *
 * @author luffyu
 * @since 2021-10-17
 */
public interface IPackInfoService extends IBaseAdminService<PackInfo> {

    /**
     * 通过appname查询一个服务
     * @param packName 当前的app名称
     * @return 返回一个app名称信息
     */
    PackInfo getByPackName(String packName);
}
