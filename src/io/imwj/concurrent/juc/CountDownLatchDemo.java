package io.imwj.concurrent.juc;

import io.imwj.concurrent.enums.CountryEnum;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch
 * @author langao_q
 * @create 2020-04-17 16:17
 */
public class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(6);
        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "\t" + "国,灭亡");
                countDownLatch.countDown();
            }, CountryEnum.forEach(i).getName()).start();
        }
        countDownLatch.await();
        System.out.println("秦统一");

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {

            }, String.valueOf(i)).start();
        }
    }
}
