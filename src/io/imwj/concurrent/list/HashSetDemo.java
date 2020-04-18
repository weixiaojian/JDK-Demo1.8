package io.imwj.concurrent.list;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * HashSet线程不安全
 * @author langao_q
 * @create 2020-04-16 17:13
 */
public class HashSetDemo {

    public static void main(String[] args) {
        Set<String> set = new HashSet();
        Set<String> set1 = Collections.synchronizedSet(new HashSet<String>());
        Set<String> set2 = new CopyOnWriteArraySet<>();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(set);
            }, String.valueOf(i)).start();
        }
    }
}
