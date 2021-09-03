package com.rubber.app.publish.logic.manager.pack.dto;

import com.rubber.app.publish.core.constant.PushStatusEnums;
import lombok.Data;

/**
 * @author luffyu
 * Created on 2021/8/29
 */
@Data
public class AppPackStatusDto {

    private String jenkinsServerKey;

    /**
     * 前置的打包状态
     */
    private PushStatusEnums preStatus;

    /**
     * 当前的打包状态
     */
    private PushStatusEnums nowStatus;


    private String execUrl;

    private Long execNum;
}
