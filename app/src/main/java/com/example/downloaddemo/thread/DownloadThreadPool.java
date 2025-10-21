package com.example.downloaddemo.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


//全局线程池
public class DownloadThreadPool {
    // 默认线程数计算
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = Math.max(2, CPU_COUNT);
    private static final int MAX_POOL_SIZE = Math.min(CPU_COUNT * 2, 8);
    private static final long KEEP_ALIVE = 60L;

    private final ThreadPoolExecutor executor;

    private static class Holder{
        private static final DownloadThreadPool INSTANCE = new DownloadThreadPool();
    }

    public static DownloadThreadPool getInstance() {
        return Holder.INSTANCE;
    }

    private DownloadThreadPool() {
        executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                //改名字，让线程更改名字，便于调试(用于指定为线程池创建新线程的方式)
                new ThreadFactory() {
                    private final AtomicInteger count = new AtomicInteger(1);
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "DownloadThread-" + count.getAndIncrement());
                    }
                },
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    //提交任务
    public void execute(Runnable task) {
        executor.execute(task);
    }

    // 获取 ThreadPoolExecutor 对象，可动态修改线程数
    public ThreadPoolExecutor getExecutor() {
        return executor;
    }

    // 动态调整线程池大小
    public void adjustPoolSize(int newCore, int newMax) {
        executor.setCorePoolSize(newCore);
        executor.setMaximumPoolSize(newMax);
    }

    public void adjustMaxPool(int newMax) {
        executor.setMaximumPoolSize(newMax);
    }

    // 关闭线程池
    public void shutdown() {
        executor.shutdown();
    }

}
