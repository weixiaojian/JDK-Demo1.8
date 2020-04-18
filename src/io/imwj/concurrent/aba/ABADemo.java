package io.imwj.concurrent.aba;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * ABA问题及解决
 * @author langao_q
 * @create 2020-04-16 14:21
 */
public class ABADemo {

    static AtomicReference atomic = new AtomicReference(100);
    static AtomicStampedReference<Integer> stamped = new AtomicStampedReference<>(100, 1);

    //ABA问题
    public static void ABA(){
        //System.out.println("=============ABA问题==============");
        /*new Thread(() -> {
            atomic.compareAndSet(100, 101);
            atomic.compareAndSet(101, 100);
        },"t1").start();

        new Thread(() -> {
            try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
            System.out.println(atomic.compareAndSet(100, 2020) + "-- value=" + atomic);
        },"t2").start();*/

        //System.out.println("=============ABA问题解决==============");
        new Thread(() -> {
            int stamp = stamped.getStamp();
            try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
            System.out.println(Thread.currentThread().getName() + " 第一次版本号：" + stamped.getStamp());
            stamped.compareAndSet(100, 101, stamped.getStamp(), stamped.getStamp()+1);
            System.out.println(Thread.currentThread().getName() + " 第一次版本号：" + stamped.getStamp());
            stamped.compareAndSet(101, 100, stamped.getStamp(), stamped.getStamp()+1);
            System.out.println(Thread.currentThread().getName() + " 第三次版本号：" + stamped.getStamp());
        },"t3").start();
        new Thread(() -> {
            int stamp = stamped.getStamp();
            try {Thread.sleep(3);} catch (InterruptedException e) {e.printStackTrace();}
            System.out.println(Thread.currentThread().getName() + " 第一次版本号：" + stamped.getStamp());
            System.out.println(stamped.compareAndSet(100, 2020, stamp, stamp+1) + "-- value=" + stamped.getReference());
        },"t4").start();
    }

    public static void main(String[] args) {
        ABA();
    }
}
