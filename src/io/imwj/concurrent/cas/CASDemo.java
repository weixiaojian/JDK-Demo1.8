package io.imwj.concurrent.cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS：比较并交换
 * @author langao_q
 * @create 2020-04-16 11:04
 */
public class CASDemo {

    public static void main(String[] args) {
        AtomicInteger i = new AtomicInteger(5);
        System.out.println(i.compareAndSet(5, 2019));
        System.out.println(i.compareAndSet(5, 2020));

        i.getAndIncrement();
        System.out.println(i);
    }

}
