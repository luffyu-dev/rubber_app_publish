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
    STOP(0,"关闭",1),

    WAIT_PACK(10,"待打包",1),
    START_PACK(11,"开始打包",1),
    PACKING(12,"打包中",1),
    PACK_ERROR(13,"打包失败",1),
    PACK_SUCCESS(14,"已打包",1),

    WAIT_PUSH(20,"待推送",2),
    PUSHING(21,"推送中",2),
    PUSHING_ERROR(22,"推送失败",2),
    PUSH_SUCCESS(23,"已推送",2),

    WAIT_PUBLISH(30,"待发布",3),
    PUBLISHING(31,"发布中",3),
    PUBLISHED(32,"已发布",3),

    FINISH(40,"完成",3),

    ;
    private final Integer code;

    private final String label;

    private final Integer type;



    PushStatusEnums(Integer code, String label,Integer type) {
        this.code = code;
        this.label = label;
        this.type = type;
    }


    public static PushStatusEnums getByCode(Integer code){
        if (code == null){
            return null;
        }
        for (PushStatusEnums pushStatusEnums:PushStatusEnums.values()){
            if (pushStatusEnums.getCode().equals(code) ){
                return pushStatusEnums;
            }
        }
        return null;
    }
}
