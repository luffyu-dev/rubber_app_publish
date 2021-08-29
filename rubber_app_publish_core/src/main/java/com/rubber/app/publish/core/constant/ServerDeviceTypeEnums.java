package com.rubber.app.publish.core.constant;

import lombok.Getter;

/**
 * @author luffyu
 * Created on 2021/8/29
 */
@Getter
public enum  ServerDeviceTypeEnums {

    /**
     * 打包的服务设备
     */
    JENKINS("jenkins","打包"),

   APPLICATION("application","应用服务器")

    ;


    ServerDeviceTypeEnums(String key, String label) {
        this.key = key;
        this.label = label;
    }

    private String key;

    private String label;

}
