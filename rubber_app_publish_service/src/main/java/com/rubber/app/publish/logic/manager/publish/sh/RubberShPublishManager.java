package com.rubber.app.publish.logic.manager.publish.sh;

import com.rubber.app.publish.logic.manager.publish.RubberPublishManager;
import com.rubber.app.publish.logic.manager.publish.dto.PublishJarDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author luffyu
 * Created on 2021/10/11
 */
@Slf4j
@Component
public class RubberShPublishManager implements RubberPublishManager {
    /**
     * 启动jar
     *
     * @param publishJarDto
     */
    @Override
    public void startJar(PublishJarDto publishJarDto) {

    }
}
