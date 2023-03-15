package com.phei.netty.timeServer.io;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimeServerHnadlerExecutePool {
    private ExecutorService executor;

    public TimeServerHnadlerExecutePool(int maxPoolSzie,int queueSize){
        executor=new ThreadPoolExecutor(Runtime.getRuntime()
                .availableProcessors(),maxPoolSzie,120L,
                TimeUnit.SECONDS,new ArrayBlockingQueue<java.lang.Runnable>(queueSize));

    }

    public void execute(Runnable task){
        executor.execute(task);
    }
}
