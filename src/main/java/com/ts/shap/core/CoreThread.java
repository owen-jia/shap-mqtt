package com.ts.shap.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: Owen Jia
 * @time: 2019/4/18 15:17
 */
public abstract class CoreThread extends Thread{

    protected final static Logger log = LoggerFactory.getLogger(CoreThread.class);
    protected final static String flag = "shap-core-thread";

    /**
     * 线程是否停止
     */
    protected boolean stoped = false;

    /**
     * 线程关闭
     */
    public void close(){
        this.stoped = true;

        if(!this.isInterrupted())
            this.interrupt();

        log.info("<"+this.getClass().getName() + ">被动关闭");
    }
}
