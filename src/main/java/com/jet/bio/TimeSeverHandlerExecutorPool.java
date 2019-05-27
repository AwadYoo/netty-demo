package com.jet.bio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author awad_yoo
 * @create 2019-05-27 11:04
 */
public class TimeSeverHandlerExecutorPool {

    private ExecutorService executor;

    public TimeSeverHandlerExecutorPool(int maxPoolSize, int queenSize) {

        executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), maxPoolSize
                , 120L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(queenSize));

    }

    public void execute(Runnable task) {
        executor.execute(task);
    }
}
