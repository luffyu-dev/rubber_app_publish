package com.rubber.app.publish.logic.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author luffyu
 * Created on 2021/9/25
 */
@Data
public class AppTaskInfoDto {


    /**
     * 发布任务的id
     */
    private Integer taskId;

    /**
     * 分布任务的名称
     */
    private String taskName;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 服务环境 多个环境用引文逗号隔开
     */
    private String appEnv;

    /**
     * app的打包tag
     */
    private String appPackTag;

    /**
     * 发布状态 包含了任务的创建和打包
     */
    private Integer taskStatus;

    /**
     * jar包的名称
     */
    private String jarName;

    /**
     * jar包版本
     */
    private String jarVersion;


    /**
     * jenkins服务的key值
     */
    private String jenkinsServerKey;

    /**
     * 任务的url
     */
    private String jobUrl;

    /**
     * 任务参数
     */
    private String jobParams;


    /**
     * 推送的详情
     */
    public Map<Integer, List<AppPublishTaskDto>> publishOrderList;
}
