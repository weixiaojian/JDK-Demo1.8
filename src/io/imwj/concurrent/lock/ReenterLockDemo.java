package io.imwj.concurrent.lock;

/**
 * 可重入锁（递归锁）
 * @author langao_q
 * @create 2020-04-17 10:19
 */

class Phone{
    public synchronized void sendSms(){
        System.out.println(Thread.currentThread().getName() + "发短信");
        sendEmail();
    }

    public synchronized void sendEmail(){
        System.out.println(Thread.currentThread().getName() + "发邮件");
    }
}

public class ReenterLockDemo {

    public static void main(String[] args) {
        Phone p = new Phone();
        for (int i = 1; i < 3; i++) {
            new Thread(() -> {
                p.sendSms();
            }, String.valueOf(i)).start();
        }
    }
}