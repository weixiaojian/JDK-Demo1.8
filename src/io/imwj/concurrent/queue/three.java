package io.imwj.concurrent.queue;

import com.sun.org.apache.bcel.internal.generic.FADD;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 3.0版本的阻塞队列
 * Volatile/CAS/AtomicInteger/AtomicReference/BlockingQueue
 * 实现MQ底层
 * @author langao_q
 * @create 2020-04-18 21:45
 */
class Source{
    private volatile boolean FLAG = true;
    private AtomicInteger atomicInteger = new AtomicInteger();//初始值为0

    private BlockingQueue<String> blockingQueue = null;
    public Source(BlockingQueue blockingQueue){
        System.out.println("传进来的BlockingQueue是：" + blockingQueue.getClass().getName());
        this.blockingQueue = blockingQueue;
    }

    //生产方法
    public void prod() throws InterruptedException {
        String data = null;
        while (FLAG){
            data = atomicInteger.incrementAndGet() + "";
            boolean rest = blockingQueue.offer(data, 2L, TimeUnit.SECONDS);//把生产的数据放入队列
            if(rest)
                System.out.println(Thread.currentThread().getName() + "\t生产了数据：" + data);
            else
                System.out.println(Thread.currentThread().getName() + "\t生产数据失败！：" + data);
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println(Thread.currentThread().getName() + "\t停止生产数据");
    }

    //消费方法
    public void consum() throws InterruptedException {
        String data = null;
        while (FLAG){
            data = blockingQueue.poll(2L, TimeUnit.SECONDS);//从队列中取出生产数据
            if(data == null || data.equalsIgnoreCase("")){
                FLAG = false;
                System.out.println(Thread.currentThread().getName() + "\t超时退出！");
                return;
            }
            System.out.println(Thread.currentThread().getName() + "\t消费了数据：" + data);
        }
    }

    //暂停方法
    public void stop(){
        FLAG = false;
        System.out.println("停止生产和消费数据！");
    }
}
public class three {
    public static void main(String[] args) throws InterruptedException {
        Source s = new Source(new ArrayBlockingQueue(10));
        new Thread(() -> {//生产线程
            try {s.prod();} catch (InterruptedException e) {e.printStackTrace();}
        },"rng").start();
        new Thread(() -> {//消费线程
            try {s.consum();} catch (InterruptedException e) {e.printStackTrace();}
        },"ig").start();

        TimeUnit.SECONDS.sleep(10);//main线程休息10s 让前面两个线程操作
        s.stop();//停止生产和消费
    }
}
