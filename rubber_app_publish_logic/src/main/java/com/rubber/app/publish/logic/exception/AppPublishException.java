package com.rubber.app.publish.logic.exception;

import com.rubber.common.utils.result.IResultHandle;
import com.rubber.common.utils.result.code.ICodeHandle;
import com.rubber.common.utils.result.exception.BaseResultRunTimeException;

/**
 * @author luffyu
 * Created on 2021/8/29
 */
public class AppPublishException extends BaseResultRunTimeException {
    public AppPublishException(String msg) {
        super(msg);
    }

    public AppPublishException(IResultHandle handle) {
        super(handle);
    }

    public AppPublishException(String code, String msg, Object data) {
        super(code, msg, data);
    }

    public AppPublishException(ICodeHandle handle, Object data) {
        super(handle, data);
    }

    public AppPublishException(ICodeHandle handle) {
        super(handle);
    }

    public AppPublishException(ICodeHandle handle, String msg, Object... arguments) {
        super(handle, msg, arguments);
    }
}
