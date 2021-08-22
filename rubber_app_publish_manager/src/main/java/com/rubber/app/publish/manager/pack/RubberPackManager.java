package com.rubber.app.publish.manager.pack;

import com.rubber.app.publish.manager.dto.AppPackDto;

/**
 * @author luffyu
 * Created on 2021/8/22
 */
public interface RubberPackManager {


    /**
     * 打包的接口防范
     * @param appPackDto 当前打包的基础信息
     * @return 返回打包信息
     */
    String pack(AppPackDto appPackDto);

}
