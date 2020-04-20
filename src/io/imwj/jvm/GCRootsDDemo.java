package io.imwj.jvm;

/**
 * GC Roots示例
 * 1.虚拟机栈中引用的对象
 * 2.方法区中的类静态属性引用对象
 * 3.方法区常量引用对象
 * 4.本地方法栈中Natvie引用的对象
 * @author langao_q
 * @create 2020-04-20 21:21
 */
public class GCRootsDDemo {

    //private static GCRootsDDemo1 gc1;//2.方法区中的类静态属性引用对象
    //private static final GCRootsDDemo2 gc2 = new GCRootsDDemo();//3.方法区常量引用对象

    public static void m1(){
        GCRootsDDemo gc = new GCRootsDDemo();
        System.gc();
        System.out.println("第一次GC success！");
    }

    public static void main(String[] args) {
        m1();//1.虚拟机栈中引用的对象
    }

}
