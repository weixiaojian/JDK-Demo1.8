package io.imwj.concurrent.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多线程之间按顺序调用，实现A > B > C三个线程启动
 * AA 打印五次，BB打印10次， CC打印15次
 * 循环10轮
 * @author langao_q
 * @create 2020-04-18 21:02
 */
class source{
    private int number = 1;
    Lock lock = new ReentrantLock();
    Condition c1 = lock.newCondition();
    Condition c2 = lock.newCondition();
    Condition c3 = lock.newCondition();

    public void print5() {
        lock.lock();
        try {
            while (number != 1){
                c1.await();
            }
            for(int i=1; i<=5; i++){
                System.out.println(Thread.currentThread().getName() + "\t" + i);
            }
            number = 2;
            c2.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public void print10() {
        lock.lock();
        try {
            while (number != 2){
                c2.await();
            }
            for(int i=1; i<=10; i++){
                System.out.println(Thread.currentThread().getName() + "\t" + i);
            }
            number = 3;
            c3.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public void print15() {
        lock.lock();
        try {
            while (number != 3){
                c3.await();
            }
            for(int i=1; i<=15; i++){
                System.out.println(Thread.currentThread().getName() + "\t" + i);
            }
            number = 1;
            c1.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }


}

public class ConditionDemo {

    public static void main(String[] args) {
        source s = new source();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                s.print5();
            }
        },"AA").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                s.print10();
            }
        },"BB").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                s.print15();
            }
        },"CC").start();
    }
}
