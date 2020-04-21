package io.imwj.jvm.param;

import java.lang.ref.Reference;
import java.util.concurrent.TimeUnit;

/**
 * JVM调优/参数配置
 * 标配参数、X参数、XX参数（主要）
 * XX参数：Boolean类型、K-V类型、jinfo
 * @author langao_q
 * @create 2020-04-21 09:49
 */
public class  HelloGC{

    public static void main(String[] args) throws InterruptedException {
        System.out.println("GC ing....");
        //byte[] bytes = new byte[50 * 1024 * 1024];
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

}
