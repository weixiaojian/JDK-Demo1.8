package io.imwj.concurrent.volatiles;

/**
 * Volatile代码分析
 * 轻量级：保证可见性，不保证原子性，禁止指令重排
 * 可见性：
 * 原子性：
 * @author langao_q
 * @create 2020-04-15 15:16
 */
class MyData{
    volatile Integer number = 0;



    public void addTo60(){
        this.number = 60;
    }

    public void addPulsPuls(){
        this.number ++;
    }
}

public class VolatileDemo {

    /**
     * 验证-不保证原子性
     * @param args
     */
    public static void main(String[] args) {
        MyData myData = new MyData();
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    myData.addPulsPuls();
                }
            }, String.valueOf(i)).start();
        }
        while (Thread.activeCount() > 2){
            Thread.yield();
        }
        System.out.println(Thread.currentThread().getName() + "：" + myData.number);

    }

    /**
     * 验证-保证可见性
     * number不使用volatile修饰 此处会一直循环
     * number使用volatile修饰 线程修改数值之后会立马结束循环
     */
    public static void seeOKByVolatile(){
        MyData myData = new MyData();
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "：init!");
            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myData.addTo60();;
            System.out.println(Thread.currentThread().getName() + "：over!");
        },"aaa").start();

        while (myData.number == 0){
        }
        System.out.println(Thread.currentThread().getName() + "结束！" + myData.number);
    }
}
