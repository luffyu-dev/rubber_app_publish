package com.rubber.app.publish.logic.config;

import com.rubber.common.utils.result.IResultHandle;
import com.rubber.common.utils.result.ResultMsg;
import com.rubber.common.utils.result.exception.BaseResultException;
import com.rubber.common.utils.result.exception.BaseResultRunTimeException;
import com.rubber.common.utils.result.exception.IResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author luffyu
 * Created on 2019-10-31
 */
@ControllerAdvice
@Slf4j
public class ExceptionHandelConfig {


    @ExceptionHandler(value = {BaseResultRunTimeException.class, BaseResultException.class})
    @ResponseBody
    public ResultMsg handel(Exception e) throws Exception {
        if(e instanceof IResultException){
            IResultException re = (IResultException)e;
            IResultHandle resultHandle = re.getResult();
            if( resultHandle instanceof ResultMsg){
                return (ResultMsg) resultHandle;
            }else {
                return ResultMsg.error();
            }
        }else {
            throw e;
        }
    }

}
