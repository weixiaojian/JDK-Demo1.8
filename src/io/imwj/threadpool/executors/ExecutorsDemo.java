package io.imwj.threadpool.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用Executors创建线程池
 * 三种方式：
 * Executors.newCachedThreadPool() 创建一个根据需要创建新线程的线程池，但在可用时将重新使用以前构造的线程。
 * Executors.newFixedThreadPool(int nThreads)创建一个线程池，该线程池重用固定数量的从共享无界队列中运行的线程。
 * Executors.newSingleThreadExecutor()创建一个使用从无界队列运行的单个工作线程的执行程序
 *
 * @author langao_q
 * @create 2020-04-20 15:32
 */
public class ExecutorsDemo {

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        ExecutorService threadPool1 = Executors.newFixedThreadPool(5);
        ExecutorService threadPool2 = Executors.newSingleThreadExecutor();
        //10个线程
        try {
            for (int i = 0; i < 10; i++) {
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t success!");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }
}
