package com.rubber.app.publish.logic.dto;


import com.rubber.app.publish.core.entity.ApplicationPublishOrder;
import com.rubber.app.publish.core.entity.PublishTaskInfo;
import lombok.Data;

import java.util.Map;

/**
 * @author luffyu
 * Created on 2021/8/29
 */
@Data
public class AppPublishTaskDto {

    /**
     * 任务信息
     */
    private PublishTaskInfo publishTaskInfo;

    /**
     * 具体的发布信息
     */
    private Map<Integer, ApplicationPublishOrder> publishOrderMap;

}
