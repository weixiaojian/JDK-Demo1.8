package io.imwj.concurrent.queue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 传统方式实现线程通信之生产者消费者
 * 一个初始值为0的变量 两个线程交替操作 一个加1 一个减1
 * @author langao_q
 * @create 2020-04-17 21:44
 */

class Kitchen{
    private Integer food = 0;
    Lock lock = new ReentrantLock();//初始化锁
    Condition condition  = lock.newCondition();

    public void increase() {
        lock.lock();//上锁
        try {
            while (food != 0){
                condition.await();//线程等待
            }
            food ++;
            System.out.println(Thread.currentThread().getName() + "\t生产了一个食物，当前：" + food);
            condition.signalAll();//唤醒所有线程
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();//解锁
        }
    }

    public void dncrease(){
        lock.lock();
        try {
            while (food == 0){
                condition.await();//线程等待
            }
            food --;
            System.out.println(Thread.currentThread().getName() + "\t消费了一个食物，当前：" + food);
            condition.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}


public class one {

    public static void main(String[] args) {
        Kitchen kitchen = new Kitchen();
        new Thread(() -> {
            for (int i = 0; i <=5; i++) {
                kitchen.increase();
            }
        },"rng").start();

        new Thread(() -> {
            for (int i = 0; i <=5; i++) {
                kitchen.dncrease();
            }
        },"ig").start();
    }
}
