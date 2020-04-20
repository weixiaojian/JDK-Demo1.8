package io.imwj.threadpool.deadlock;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 死锁：指的是两个或两个以上的线程在执行过程中，因争夺资源而造成一种**互相等待的现象**
 * 系统资源不足、进程推进的顺序不合适、资源分配不当
 * 如何解决：1.jps命令定位进程编号 2.jstack找到死锁查看
 * @author langao_q
 * @create 2020-04-20 16:57
 */
class Deadloc implements Runnable{
    private String lockAa,lockBb;

    public Deadloc(String lockAa, String lockBb){
        this.lockAa = lockAa;
        this.lockBb = lockBb;
    }

    @Override
    public void run() {
        synchronized (lockAa){
            System.out.println(Thread.currentThread().getName() + "\t 拿到锁："+lockAa);
            try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) { e.printStackTrace();}
            synchronized (lockBb){
                System.out.println(Thread.currentThread().getName() + "\t 拿到锁："+lockBb);
            }
        }
    }
}

public class DeadlockDemo {
    public static void main(String[] args) {
        Deadloc deadloc = new Deadloc("lockAa", "lockBb");
        Deadloc deadloc1 = new Deadloc("lockBb", "lockAa");
        new Thread(deadloc, "ThreadAAA").start();
        new Thread(deadloc1, "ThreadBBB").start();
    }
}
