package io.imwj.concurrent.lock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 自旋锁：CAS（比较并获取）
 * @author langao_q
 * @create 2020-04-17 14:43
 */
public class SpinLockDemo {

    AtomicReference<Thread> atomic = new AtomicReference();

    public void mylock(){
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + "上锁开始!");
        while (!atomic.compareAndSet(null, thread)){
            System.out.println(thread.getName() + "抢锁运行中......");
        }
    }

    public void unmylock(){
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + "解锁开始!");
        atomic.compareAndSet(thread, null);
        System.out.println("解锁完成......");
    }


    public static void main(String[] args) {
        SpinLockDemo demo = new SpinLockDemo();
        new Thread(() -> {
            demo.mylock();
            try {Thread.sleep(3); } catch (InterruptedException e) {e.printStackTrace();}
            demo.unmylock();
        },"t1").start();

        new Thread(() -> {
            demo.mylock();
            try {Thread.sleep(1); } catch (InterruptedException e) {e.printStackTrace();}
            demo.unmylock();
        },"t2").start();
    }

}
