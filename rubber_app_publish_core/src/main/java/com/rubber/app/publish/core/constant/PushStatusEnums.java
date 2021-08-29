package com.rubber.app.publish.core.constant;

import lombok.Getter;

/**
 * @author luffyu
 * Created on 2021/8/28
 */
@Getter
public enum  PushStatusEnums {
    /**
     * 发布状态
     */
    WAIT_PACK(10,"待打包"),
    START_PACK(11,"开始打包"),
    PACKING(12,"打包中"),
    PACK_SUCCESS(13,"已打包"),
    PACK_ERROR(14,"打包失败"),

    WAIT_PUSH(20,"待推送"),
    PUSHING(21,"推送中"),
    PUSHED(22,"已推送"),

    WAIT_PUBLISH(30,"待发布"),
    PUBLISHING(31,"发布中"),
    PUBLISHED(32,"已发布"),

    ;
    private final Integer code;

    private final String label;



    PushStatusEnums(Integer code, String label) {
        this.code = code;
        this.label = label;
    }

}
