package io.imwj.concurrent.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * BlockingQuery
 * 阻塞队列
 * @author langao_q
 * @create 2020-04-17 21:25
 */
public class BlockingQueryDemo {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> blockingQuery = new SynchronousQueue<>();
        new Thread(() -> {
            try {
                blockingQuery.put("aaa");
                System.out.println("put aaa success");
                blockingQuery.put("bbb");
                System.out.println("put bbb success");
                blockingQuery.put("ccc");
                System.out.println("put bbb success");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"rng").start();

        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + "取出：" + blockingQuery.take());
                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName() + "取出：" + blockingQuery.take());
                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName() + "取出：" + blockingQuery.take());
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"ig").start();
    }
}
