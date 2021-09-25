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
    INIT(10,"初始化"),
    NORMAL(20,"正常"),
    STOP(30,"异常"),
    RELEASE(40,"释放中")


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
