package com.rubber.app.publish.logic.manager.push.dto;

import lombok.Data;

/**
 * @author luffyu
 * Created on 2021/9/29
 */
@Data
public class AppPushResult {

    /**
     * 推送是否成功
     */
    private boolean success;

    /**
     * 执行的状态
     */
    private Integer execStatus;

    /**
     * 推送结果
     */
    private String errMsg;

    /**
     * 成功的结果
     */
    private String successMsg;

}
