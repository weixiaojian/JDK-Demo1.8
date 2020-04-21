package io.imwj.jvm.reference;

import java.util.HashMap;
import java.util.WeakHashMap;

/**
 * WeakHashMapDemo
 * @author langao_q
 * @create 2020-04-21 17:11
 */
public class WeakHashMapDemo {

    public static void main(String[] args) {
        hashMap();
        System.out.println("================");
        myWeakHashMap();
    }

    //普通map
    public static void hashMap(){
        HashMap<Integer,Object> map = new HashMap<>();
        Integer key = new Integer(1);
        String value = "HashMap";
        map.put(key, value);

        key = null; //此处并不会影响map中的内容！
        System.out.println(map);

        System.gc();
        System.out.println(map);
    }

    //WeakHashMap
    public static void myWeakHashMap(){
        WeakHashMap<Integer,String> map = new WeakHashMap<>();
        Integer key = new Integer(2);
        String value = "WeakHashMap";
        map.put(key, value);

        key = null; //此处并不会影响map中的内容！
        System.out.println(map);

        System.gc();
        System.out.println(map);
    }
}
