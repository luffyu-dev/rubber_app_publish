package com.rubber.app.publish.logic.controller;

import com.rubber.common.utils.result.ResultMsg;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luffyu
 * Created on 2021/9/25
 */
@RestController
@RequestMapping("/publish/handler")
public class PublishHandlerController {


    /**
     * 推送包
     * @return 返回包信息
     */
    @PostMapping("/pushPack")
    public ResultMsg pushPack(Integer publishId){





        return ResultMsg.success();
    }


}
