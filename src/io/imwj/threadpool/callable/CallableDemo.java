package io.imwj.threadpool.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * Callable接口：实现多线程的第三种方式
 * 区别：相对与Runnable有返回值、能够抛出异常
 * 启动方式：通过FutureTask
 * 注意：尽量最后get()获取返回值（如果计算返回值需要时间 main线程会阻塞等待 直到计算完成）
 * @author langao_q
 * @create 2020-04-19 11:39
 */
class mythread implements Callable<Integer>{
    @Override
    public Integer call() throws Exception {
        System.out.println(Thread.currentThread().getName() + "\t 开启一个单独的线程");
        TimeUnit.SECONDS.sleep(3);
        return 1024;
    }
}

public class CallableDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask<>(new mythread());//1.构造线程
        new Thread(futureTask, "IG").start();//2.启动线程
        Integer value = futureTask.get();//3.获取返回值（尽量往后放）
        //FutureTask<Integer> futureTask1 = new FutureTask<>(new mythread());//必须再构造一次
        //new Thread(futureTask1, "RNG").start();//即使再启动一个新的线程也不会生效
        System.out.println("Callable实现多线程的返回值：" + value);
    }
}
