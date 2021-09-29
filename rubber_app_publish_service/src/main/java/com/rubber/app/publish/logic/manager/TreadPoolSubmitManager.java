package com.rubber.app.publish.logic.manager;

import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author luffyu
 * Created on 2021/9/29
 */
@Component
public class TreadPoolSubmitManager {


    /**
     * 任务现场池
     */
    private ThreadPoolExecutor executorPool = new ThreadPoolExecutor(2,4,1, TimeUnit.SECONDS,new ArrayBlockingQueue<>(200));



    public void submit(Runnable command){
        executorPool.submit(command);
    }

}
