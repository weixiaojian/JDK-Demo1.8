package io.imwj.concurrent.list;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Array并发修改异常及解决
 * java.util.ConcurrentModificationException
 * 解决方案：1.使用Vector替代ArrayList，2.Collections.synchronizedList()，3.
 * @author langao_q
 * @create 2020-04-16 16:20
 */
public class ArrayListDemo {

    public static void main(String[] args) {
        List<String> list = new ArrayList();
        List<String> list1 = new Vector();
        List<String> list2 = Collections.synchronizedList(new ArrayList<>());
        CopyOnWriteArrayList list3 = new CopyOnWriteArrayList();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                list.add("123");
                System.out.println(list);
            }, String.valueOf(i)).start();
        }
    }
}
