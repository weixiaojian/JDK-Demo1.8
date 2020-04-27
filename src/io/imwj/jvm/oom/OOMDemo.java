package io.imwj.jvm.oom;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 常见的OOM(内存用完)异常
 *
 * @author langao_q
 * @create 2020-04-22 22:28
 */
public class OOMDemo {
    static String a;

    public static void main(String[] args) {
        int a = '2';
        System.out.println(a);
        System.out.println(a);
        //StackOverflowErro();
        //OOMJavaHeapSpace();
        //OOMGCoverHeadLimitExceeded();
        //OOMunableToCreateNewNativeThread();
        //OOMMetaspac();

    }

    //StackOverflowErro栈溢出
    public static void StackOverflowErro() {
        StackOverflowErro();//递归调用
    }

    //OutOfMemeoryErro：Java heap space堆溢出
    public static void OOMJavaHeapSpace() {
        byte[] bytes = new byte[20 * 1024 * 1024];
    }

    //OOM:GC over Head Limit Exceede//GC回收时间过长，GC回收做了无用功
    public static void OOMGCoverHeadLimitExceeded() {
        int i = 0;
        List<String> list = new ArrayList<>();
        try {
            while (true) {
                list.add(String.valueOf(i++).intern());
            }
        } catch (Exception e) {
            System.out.println("*************i：" + i);
            e.printStackTrace();
        }
    }


    //OOM:unable to create new native thread 单个线程中的进程数量超出最大数
    public static void OOMunableToCreateNewNativeThread() {
        for (int i = 0; ; i++) {
            System.out.println("************" + i);
            new Thread(() -> {
                try {TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);} catch (Exception e) {e.printStackTrace();}
            }, "" + i).start();
        }
    }


    static class OOMTest {
    }
    //OOM:Metaspac //元空间内存用完
    /*public static void OOMMetaspac(){
        int i=0;
        try {
            while (true){
                //cglib动态代理
                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(OOMTest.class);
                enhancer.setUseCache(false);
                enhancer.setCallback(new MethodInterceptor() {
                    @Override
                    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                        return methodProxy.invokeSuper(o, new String[]{});
                    }
                });
                enhancer.create();
            }
        }catch (Exception e){
            System.out.println(i + "次以后发生了异常");
            e.printStackTrace();
        }
    }*/
}
