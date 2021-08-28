package com.rubber.app.publish.core.constant;

import com.rubber.common.utils.result.code.ICodeHandle;

/**
 * @author luffyu
 * Created on 2021/8/8
 */
public enum ErrCodeEnums implements ICodeHandle {

    /**
     *     SUCCESS("1000000", "成功"),
     *     SYSTEM_ERROR("2000000", "系统错误"),
     *     PARAM_ERROR("3000000", "参数错误");
     *     1开头表示业务正常
     *     2开头表示系统异常
     *     3开头表示用户入参异常
     *     4开头表示逻辑上异常
     *
     *     第5位表示是业务code，用户服务业务是2
     *     第3位表示是具体的业务类型 其中 1表示打包  2表示推送  3表示发布
     *
     */
    PARAM_ERROR("3020000", "必要参数不能为空"),
    DEVICE_IS_EXIST("3020000", "设备已经存在"),
    DATA_IS_EXIST("3020000", "数据已经存在"),
    DATA_IS_NOT_EXIST("3020000", "数据不存在"),




    ;

    ErrCodeEnums(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String code;

    private String msg;


    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
