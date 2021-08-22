package com.rubber.app.publish.manager.pack;

import com.rubber.app.publish.manager.dto.AppPackDto;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author luffyu
 * Created on 2021/8/22
 */
@Slf4j
public abstract class RubberBasePackManager implements RubberPackManager {

    /**
     * 任务是否存在
     */
    public abstract boolean isExist(AppPackDto appPackDto);

    /**
     * 创建一个任务
     */
    public abstract String doCreateJob(AppPackDto appPackDto) throws Exception;

    /**
     * 构建任务
     */
    public abstract String doBuildJob(AppPackDto appPackDto) throws IOException;


    /**
     * 打包的接口防范
     *
     * @param appPackDto 当前打包的基础信息
     * @return 返回打包信息
     */
    @Override
    public String pack(AppPackDto appPackDto) {
        //前置操作
        doPrePack(appPackDto);
        try {
            log.info("开始执行打包逻辑,packName={}",appPackDto.getAppName());
            //如果当前打包的app不存在，则创建一个打包的任务
            if (!isExist(appPackDto)) {
                log.info("开始执行的打包任务不存在，则创建一个任务,packName={}",appPackDto.getAppName());
                doCreateJob(appPackDto);
            }
            //开始打包
            if(doBuildJob(appPackDto) != null){
                log.info("开始执行的打包任务任务启动成功,packName={}",appPackDto.getAppName());
                //打包成功之后的操作
                doAfterPackSuccess(appPackDto);
            }else {
                //打包失败之后的操作
                doAfterPackFail(appPackDto);
            }
        }catch (Exception e){
            //打包有异常的操作
            doAfterPackError(appPackDto);
        }
        return null;
    }


    /**
     * 打包成功的操作
     * @param appPackDto
     */
    public void doPrePack(AppPackDto appPackDto){

    }

    /**
     * 打包成功的操作
     */
    public void doAfterPackSuccess(AppPackDto appPackDto){

    }

    /**
     * 打包失败的操作
     */
    public void doAfterPackFail(AppPackDto appPackDto){

    }

    /**
     * 打包异常的操作
     */
    public void doAfterPackError(AppPackDto appPackDto){

    }

}
