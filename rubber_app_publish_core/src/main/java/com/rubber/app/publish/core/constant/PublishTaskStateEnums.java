package com.rubber.app.publish.core.constant;

/**
 * @author luffyu
 * Created on 2021/9/25
 */
public enum PublishTaskStateEnums {

    /**
     * 打包状态
     */

    WAIT_PACK(1,"待打包"),

    WAIT_PUSH(2,"待推送"),

    WAIT_PUBLISH(3,"待发布"),

    SUCCESS(4,"待发布"),

    ;
    private final Integer code;

    private final String label;

    PublishTaskStateEnums(Integer code, String label) {
        this.code = code;
        this.label = label;
    }

}
