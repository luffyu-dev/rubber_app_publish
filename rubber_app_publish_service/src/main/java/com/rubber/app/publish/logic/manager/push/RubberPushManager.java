package com.rubber.app.publish.logic.manager.push;

import com.rubber.app.publish.logic.manager.push.dto.AppPushDto;
import com.rubber.app.publish.logic.manager.push.dto.AppPushResult;

import java.io.IOException;

/**
 * @author luffyu
 * Created on 2021/8/22
 */
public interface RubberPushManager {

    /**
     * 推送接口
     * @return
     */
    AppPushResult pushPackage(AppPushDto appPushDto) throws Exception;

}
