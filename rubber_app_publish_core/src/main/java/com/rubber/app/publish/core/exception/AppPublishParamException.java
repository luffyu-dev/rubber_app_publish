package com.rubber.app.publish.core.exception;

import com.rubber.common.utils.result.IResultHandle;
import com.rubber.common.utils.result.code.ICodeHandle;
import com.rubber.common.utils.result.exception.BaseResultRunTimeException;

/**
 * @author luffyu
 * Created on 2021/8/27
 */
public class AppPublishParamException extends BaseResultRunTimeException {



    public AppPublishParamException(String msg) {
        super(msg);
    }

    public AppPublishParamException(ICodeHandle handle) {
        super(handle);
    }


    public AppPublishParamException(ICodeHandle handle, Object data) {
        super(handle, data);
    }

    public AppPublishParamException(ICodeHandle handle, String msg, Object... arguments) {
        super(handle, msg, arguments);
    }
}
