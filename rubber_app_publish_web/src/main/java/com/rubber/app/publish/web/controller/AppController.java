package com.rubber.app.publish.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luffyu
 * Created on 2021/8/22
 */
@RestController
@RequestMapping("/api")
public class AppController {

    @GetMapping("/info")
    public String getTest(){
        return "application s" ;
    }



}
