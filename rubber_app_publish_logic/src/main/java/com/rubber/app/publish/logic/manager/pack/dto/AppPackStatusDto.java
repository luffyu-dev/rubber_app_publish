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

    private PushStatusEnums nowStatus;

    private PushStatusEnums nextStatus;

    private String execUrl;

    private Long execNum;
}
