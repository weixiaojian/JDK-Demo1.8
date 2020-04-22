package io.imwj.jvm.oom;

/**
 * 常见的OOM(内存用完)异常
 * StackOverflowErro：栈溢出
 * OutOfMemeoryErro：Java heap space
 * @author langao_q
 * @create 2020-04-22 22:28
 */
public class OOMDemo {

    public static void main(String[] args) {
        //StackOverflowErro();
        OOMJavaHeapSpace();
    }

    //StackOverflowErro
    public static void StackOverflowErro(){
        StackOverflowErro();//递归调用
    }

    //OutOfMemeoryErro：Java heap space
    public static void OOMJavaHeapSpace(){
        byte[] bytes = new byte[20 * 1024 * 1024];
    }
}
