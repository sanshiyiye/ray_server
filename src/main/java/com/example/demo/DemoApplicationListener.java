package com.example.demo;

import org.apache.log4j.Logger;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;


public class DemoApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    private Logger logger = Logger.getLogger(DemoApplicationListener.class);

//    private UserService userService = null;
    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent applicationEnvironmentPreparedEvent) {

    }
}

