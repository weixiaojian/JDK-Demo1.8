package io.imwj.concurrent.list;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * HashMap线程不安全
 * @author langao_q
 * @create 2020-04-16 17:27
 */
public class HashMapDemo {

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap();
        Map<Object, Object> map1 = Collections.synchronizedMap(new HashMap<>());
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                map1.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0,5));
                System.out.println(map1);
            }, String.valueOf(i)).start();
        }
    }
}
