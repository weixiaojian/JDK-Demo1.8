package io.imwj.concurrent.juc;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Semaphore：资源复用
 * @author langao_q
 * @create 2020-04-17 17:36
 */
public class SemaphoreDemo {

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);//3个车位
        for (int i = 0; i < 6; i++) {//六辆车
            new Thread(() -> {
                try {
                    semaphore.acquire();//锁定资源
                    System.out.println(Thread.currentThread().getName() + "抢到车位！");
                    try {TimeUnit.SECONDS.sleep(3);} catch (InterruptedException e) {e.printStackTrace();}
                    System.out.println(Thread.currentThread().getName() + "停车3秒后 还回车位！");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    semaphore.release();//还回资源
                }
            }, String.valueOf(i)).start();
        }

    }
}
