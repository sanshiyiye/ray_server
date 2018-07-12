package com.ray.server.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShutDownService {

    private static final Logger logger = Logger.getLogger(ShutDownService.class);

    private final List<Hook> hooks;

    public ShutDownService() {
        logger.debug("Creating shutdown service");
        hooks = new ArrayList<Hook>();
        createShutdownHook();
    }

    /**
     * Protected for testing
     */
//    @VisibleForTesting
    protected void createShutdownHook() {
        ShutdownDaemonHook shutdownHook = new ShutdownDaemonHook();
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    public class ShutdownDaemonHook extends Thread {

        /**
         * 循环并使用hook关闭所有后台线程
         *
         * @see java.lang.Thread#run()
         */
        @Override
        public void run() {

            logger.info("Running shutdown sync");

            for (Hook hook : hooks) {
                hook.shutdown();
            }
        }
    }

    /**
     * 创建hook class的新实例
     */
    public Hook createHook(Thread thread) {

        thread.setDaemon(true);
        Hook retVal = new Hook(thread);
        hooks.add(retVal);
        return retVal;
    }

//    @VisibleForTesting
    List<Hook> getHooks() {
        return hooks;
    }
}
