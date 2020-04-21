package io.imwj.jvm.reference;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * 四大引用（强、软、弱、虚）
 * 强引用：无论是GC还是OOM都不会清理掉强引用
 * 软引用：内存充足时保留，内存不够时进行回收
 * 弱引用：只要触发GC弱引用对象就会被回收掉
 * 虚引用：
 * @author langao_q
 * @create 2020-04-21 16:23
 */
public class ReferenceDemo {

    public static void main(String[] args) {
        reference();
        System.out.println("================");
        softReference();
        System.out.println("================");
        weakReference();
    }

    //强引用
    public static void reference(){
        Object obj = new Object();
        Object obj1 = obj;

        obj = null;
        System.gc();

        System.out.println(obj1);
    }

    //软引用
    public static void softReference(){
        Object obj = new Object();
        SoftReference obj1 = new SoftReference(obj);

        obj = null;
        System.gc();

        System.out.println(obj1.get());
    }

    //弱引用
    public static void weakReference(){
        Object obj = new Object();
        WeakReference obj1 = new WeakReference(obj);

        obj = null;
        System.gc();

        System.out.println(obj1.get());
    }
}
