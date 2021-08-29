package com.rubber.app.publish.logic.manager.pack.dto;

import lombok.Data;

/**
 * @author luffyu
 * Created on 2021/8/29
 */
@Data
public class AppPackResponse {

    /**
     * 是否启动成功
     */
    private boolean isSuccess = false;


    private String jenkinsServerKey;


    private String queueItem;

}
