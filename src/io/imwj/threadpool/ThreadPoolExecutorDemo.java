package io.imwj.threadpool;

import java.util.concurrent.*;

/**
 * 构造自己的线程池 参照ThreadPoolExecutor源码
 * 可运行的最大线程数8=maximumPoolSize(5) + BlockingDeque<>(3)
 * 四大拒绝策略：AbortPolicy、CallerRunPolicy、DiscardoldestPolicy、DiscardPolicy
 * 线程池参数的合理配置：CPU密集型（CPU核数+1）、io密集型（）
 * @author langao_q
 * @create 2020-04-20 16:05
 */
public class ThreadPoolExecutorDemo {

    public static void main(String[] args) {

        //jdk提供方式(不用)：ExecutorService threadPool = Executors.newFixedThreadPool(5);
        ExecutorService threadPool = new ThreadPoolExecutor(2,
                                                            5,
                                                            5,
                                                            TimeUnit.SECONDS,
                                                            new LinkedBlockingDeque<>(3),
                                                            Executors.defaultThreadFactory(),
                                                            new ThreadPoolExecutor.AbortPolicy());
        try {
            for (int i = 0; i < 10; i++) {
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t SUCCESS!");
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            threadPool.shutdown();
        }
    }
}
