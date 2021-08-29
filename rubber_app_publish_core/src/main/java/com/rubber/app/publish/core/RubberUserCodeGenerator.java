package com.rubber.app.publish.core;

import com.rubber.base.components.mysql.utils.RubberSqlCodeGenerator;

/**
 * @author luffyu
 * Created on 2021/6/14
 */
public class RubberUserCodeGenerator {


    public static void main(String[] args) {

        RubberSqlCodeGenerator.CodeGeneratorConfigBean configBean = new RubberSqlCodeGenerator.CodeGeneratorConfigBean();
        configBean.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/rubber_oa_admin_db?useUnicode=true;characterEncoding=utf-8");
        configBean.setUserName("root");
        configBean.setPassword("root");
        configBean.setModelName("rubber_app_publish_core");
        configBean.setPackageParent("com.rubber.app.publish.core");
        configBean.setAuthor("luffyu");

        RubberSqlCodeGenerator rubberSqlCodeGenerator =  new RubberSqlCodeGenerator(configBean);
        rubberSqlCodeGenerator.create("t_application_publish_order","t_publish_task_info");
    }
}
