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

class Kitchen0{
    private Integer food = 0;

    public synchronized void increase() {
        try {
            while (food != 0){
                this.wait();//线程等待
            }
            food ++;
            System.out.println(Thread.currentThread().getName() + "\t老版本生产了一个食物，当前：" + food);
            this.notifyAll();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public synchronized void dncrease(){
        try {
            while (food == 0){
                this.wait();//线程等待
            }
            food --;
            System.out.println(Thread.currentThread().getName() + "\t老版本消费了一个食物，当前：" + food);
            this.notifyAll();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}


public class zero {

    public static void main(String[] args) {
        Kitchen0 kitchen = new Kitchen0();
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
