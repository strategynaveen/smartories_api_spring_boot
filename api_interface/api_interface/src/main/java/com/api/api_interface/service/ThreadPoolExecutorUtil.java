package com.api.api_interface.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
public class ThreadPoolExecutorUtil
{
    private Logger logger= LoggerFactory.getLogger(ThreadPoolExecutorUtil.class);

    private ThreadPoolExecutor threadPoolExecutor;

    public ThreadPoolExecutorUtil()
    {
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue(100000);
        threadPoolExecutor = new ThreadPoolExecutor(2, 10, 20, TimeUnit.SECONDS, blockingQueue);
        threadPoolExecutor.setRejectedExecutionHandler((r, executor) ->
        {
            try
            {
                Thread.sleep(1000);
                logger.error("Exception occurred while adding task, Waiting for some time");
            }
            catch (InterruptedException e)
            {
                logger.error("Thread interrupted:  ()",e.getCause());
                Thread.currentThread().interrupt();
            }
            threadPoolExecutor.execute(r);
        });
    }

    void executeTask(MachineDataThread taskThread)
    {
        Future<?> future=threadPoolExecutor.submit(taskThread);
        // System.out.println("Queue Size: "+threadPoolExecutor.getQueue().size());
        // System.out.println("Number of Active Threads: "+threadPoolExecutor.getActiveCount());
    }
}

