package com.rubber.app.publish.core.constant;

import lombok.Getter;

/**
 * @author luffyu
 * Created on 2021/8/27
 */
@Getter
public enum ServerStatusEnums {

    /**
     * 状态
     */
    NORMAL(10,"正常"),
    STOP(30,"停用"),
    LOADING(40,"负载")


    ;
    private final Integer code;

    private final String label;


    ServerStatusEnums(Integer code, String label) {
        this.code = code;
        this.label = label;
    }

    public static ServerStatusEnums getDefault(){
        return NORMAL;
    }



}
