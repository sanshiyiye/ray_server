package com.ray.server.service;

import org.apache.log4j.Logger;

public class Hook {
    private static final Logger logger = Logger.getLogger(Hook.class);

    private boolean keepRunning = true;

    private final Thread thread;

    Hook(Thread thread) {
        this.thread = thread;
    }

    /**
     * @return True 如果后台线程继续运行
     */
    public boolean keepRunning() {
        return keepRunning;
    }

    /**
     * 告诉客户端后台线程关闭并等待友好地关闭
     */
    public void shutdown() {
        keepRunning = false;
        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            logger.error("Error shutting down thread with hook", e);
        }
    }
}
