package com.rubber.app.publish.logic.manager.publish;

import com.rubber.app.publish.logic.manager.publish.dto.PublishJarDto;

/**
 * @author luffyu
 * Created on 2021/10/11
 */
public interface RubberPublishManager {

    /**
     * 启动jar
     */
    void startJar(PublishJarDto publishJarDto);
}
