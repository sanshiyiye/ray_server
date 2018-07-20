package com.ray.server.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class CommandController extends  AbstractController{
    //    @Value("${spring.redis.host}")
    @Value("${spring.redis.host}")
    private String host;
    @RequestMapping(value="/doAction",method = RequestMethod.GET)
    public String doAction(){

        System.out.println(host);
        return getSession().getId();
    }
}
