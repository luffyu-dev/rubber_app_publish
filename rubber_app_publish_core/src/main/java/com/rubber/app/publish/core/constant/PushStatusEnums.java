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
    PACKING(11,"打包中"),
    PACKED(12,"已打包"),

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
