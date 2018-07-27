package com.ray.server.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommandController extends  AbstractController{


    @RequestMapping(value="/doAction",method = RequestMethod.GET)
    public String doAction(){

        System.out.println(getSession().getId());
        return getSession().getId();
    }
}
