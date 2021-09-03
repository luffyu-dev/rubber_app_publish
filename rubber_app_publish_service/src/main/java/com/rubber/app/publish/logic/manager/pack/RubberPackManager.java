package com.rubber.app.publish.logic.manager.pack;

import com.rubber.app.publish.logic.manager.pack.dto.AppPackDto;
import com.rubber.app.publish.logic.manager.pack.dto.AppPackResponse;
import com.rubber.app.publish.logic.manager.pack.dto.AppPackStatusDto;

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
    AppPackResponse pack(AppPackDto appPackDto);



    AppPackStatusDto getPackStatus(AppPackStatusDto appPackStatusDto);


    String getPackLog(AppPackStatusDto appPackStatusDto);
}
