package com.rubber.app.publish.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.rubber.*")
@MapperScan("com.rubber.app.publish.core.mapper")
public class RubberAppPublishWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(RubberAppPublishWebApplication.class, args);
    }

}
