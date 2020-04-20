## java.util.concurretn（高并发）
### Volatile
* 保证可见性、不保证原子性、禁止指令重排（指令重排：编译为了优化代码执行进行的顺序重排）
* 如何保证原子性：使用java.util.concurrent.atomic包下的AtomicInteger替换我们原本用的int等基本类型
* 如果是对象一类的要保证原子性则使用AtomicReference<Object>原子引用 
* 多线程下实现单例模式
```
/**
 * volatile防止指令重排
 * synchronized：保证原子性
 */
class Singleton{
    public static volatile Singleton singleton = null;

    public static Singleton getSingleton(){
        //双端检锁机制
        if(singleton == null){
            synchronized (Singleton.class){
                if(singleton == null){
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
}
```

### CAS（比较并交换）
* 如果线程的期望值和内存的真实值一样就更新数据，若线程的期望值和内存不一致本次修改失败 并重新拷贝内存中的值
* 核心类：UnSafe是操作底层的后门，类中所有的方法都是native修饰的,也就是说UnSafe类中的方法都是直接调用操作底层资源执行响应的任务
* UnSafe根据内存偏移值来获取最新的准确数据，这样即保证了并发的效率又确保原子性
* 缺点：加大CPU开销、只能保证一个共享变量的原子性、ABA问题（一个线程把数据A变为了B，然后又重新变成了A。此时另外一个线程读取的时候，发现A没有变化，就误以为是原来的那个A）
* ABA解决：增加版本号机制AtomicStampedReference<T>
* 自旋：UnSafe类 + CAS思想


### Synchronized
* 同步锁 保证方法或代码块在多线程下同步执行
* 即保证可见性 又保证原子性，但太重（无法保证并发的效率）

## 线程六种状态
* 初始：创建了一个线程对象，但还没有调用start()方法。
* 运行：Java线程中将就绪（ready）和运行中（running）两种状态笼统的称为“运行”
* 阻塞：表示线程阻塞于锁。
* 等待：进入该状态的线程需要等待其他线程做出一些特定动作（通知或中断）。
* 超时等待：该状态不同于WAITING，它可以在指定的时间后自行返回。
* 终止：表示该线程已经执行完毕。

### 公平锁和非公平锁
* 公平锁：是指多个线程按照申请锁的顺序来获取锁类似排队 先来后到
* 非公平锁：是指在多线程获取锁的顺序并不是按照申请锁的顺序,有可能后申请的线程比先申请的线程优先获取到锁,在高并发的情况下,有可能造成优先级反转或者饥饿现象（如果获取失败会到公平锁的队列中）
* 并发包ReentrantLock的创建可以指定构造函数的boolean类型来得到公平锁或者非公平锁 默认是非公平锁

### 可重入锁（递归锁）
* 线程可以进入任何一个它已经拥有的锁所同步者的代码块（指的是同一线程外层函数获得最外层锁之后，内层递归函数仍然能获取该锁的代码；获取大门钥匙后可以进入房间）
* 最大作用就是避免死锁
* synchronized/lock都是典型可重入锁

### 自旋锁
* 是指尝试获取锁的线程不会立即阻塞，而是采用循环的方式去尝试获得锁，这样的好处是减少线程上下文切换 缺点循环会消耗CPU性能
* AtomicInteger
```
    public final int getAndAddInt(Object var1, long var2, int var4) {
        int var5;
        do {
            var5 = this.getIntVolatile(var1, var2);
        } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));
        return var5;
    }
```

### 独占锁(写)/共享锁(读)/互斥锁(读写)
* 独占锁：指该锁一次只能被一个线程拥有（ReentrantLock/Synchronized都是）
* 共享锁：指该锁可以被多个线程共享
* 互斥锁：是一种简单的加锁的方法来控制对共享资源的访问，互斥锁只有两种状态,即上锁( lock )和解锁( unlock )，读写即是互斥的

### CountDownLatch
* 让一些线程阻塞直到另外的一些操作完成后才被唤醒
* 两个方法：countDown()计数器减一，当计时器值为0调用await()方法会唤醒阻塞的线程
```
        CountDownLatch countDownLatch = new CountDownLatch(6);//阻塞
        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "\t" + "国,灭亡");
                countDownLatch.countDown();//自减
            }, String.valueOf(i)).start();
        }
        countDownLatch.await();//唤醒
        System.out.println("秦统一");
```

### CyclicBarrier 
* 让一组线程到达屏障时被阻塞，直到最后一个线程到达屏障时才会开门，这时所有线程才会被唤醒继续工作
* countDownLatch(火箭倒计时自减)，CyclicBarrier(计数器 等大家到齐)
```
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7, ()->{ System.out.println("召唤神龙！"); });

        for (int i = 0; i < 7; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "---龙珠");
                try {cyclicBarrier.await();} catch (InterruptedException e) {e.printStackTrace();} catch (BrokenBarrierException e) {e.printStackTrace();}
            }, String.valueOf(i)).start();
        }
```

### Semaphore
* 是一个计数信号量，必须由获取它的线程释放，常用于限制可以访问某些资源的线程数量
* 可伸缩的控制并发资源数（初始化、减少、增加）
```
        Semaphore semaphore = new Semaphore(3);//3个车位
        for (int i = 0; i < 6; i++) {//六辆车
            new Thread(() -> {
                try {
                    semaphore.acquire();//锁定资源
                    System.out.println(Thread.currentThread().getName() + "抢到车位！");
                    try {TimeUnit.SECONDS.sleep(3);} catch (InterruptedException e) {e.printStackTrace();}
                    System.out.println(Thread.currentThread().getName() + "停车3秒后 还回车位！");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    semaphore.release();//还回资源
                }
            }, String.valueOf(i)).start();
        }
```

## 阻塞队列 (BlockingQueue)
* 当阻塞队列是空的时候，从队列中`获取`元素会被阻塞；当阻塞队列是满的时候，从队列中`添加`元素会被阻塞
### ArrayBlockingQueue 
* 由数组构成的有界阻塞队列

### LinkedBlockingQueue
* 由链表结构组成的有界阻塞队列（但大小是默认值Integer.MAX_VALUE）


### SynchroousQueue
* 不储存元素的阻塞队列，也是单个的元素队列（SynchronousQueue没有容量）
* 每个put操作必须要等待一个take操作,否则不能继续添加元素,反之亦然
```
        BlockingQueue<String> blockingQuery = new SynchronousQueue<>();
        new Thread(() -> {
            try {
                blockingQuery.put("aaa");
                System.out.println("put aaa success");
                blockingQuery.put("bbb");
                System.out.println("put bbb success");
                blockingQuery.put("ccc");
                System.out.println("put bbb success");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"rng").start();

        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + "取出：" + blockingQuery.take());
                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName() + "取出：" + blockingQuery.take());
                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName() + "取出：" + blockingQuery.take());
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"ig").start();
```
## 线程代码编写口诀
* 线程  操作  资源类
* 判断  干活  通知
* 防止虚假唤醒机制

### 阻塞队列one
* synchronized：线程阻塞
* wait：线程等待
* notify：唤醒线程
* 传统方式实现线程通信之生产者消费者：一个初始值为0的变量 两个线程交替操作 一个加1 一个减1重复五次
```
class Kitchen0{
    private Integer food = 0;

    public synchronized void increase() {
        try {
            while (food != 0){
                this.wait();//线程等待
            }
            food ++;
            System.out.println(Thread.currentThread().getName() + "\t老版本生产了一个食物，当前：" + food);
            this.notifyAll();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public synchronized void dncrease(){
        try {
            while (food == 0){
                this.wait();//线程等待
            }
            food --;
            System.out.println(Thread.currentThread().getName() + "\t老版本消费了一个食物，当前：" + food);
            this.notifyAll();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}


public class zero {

    public static void main(String[] args) {
        Kitchen0 kitchen = new Kitchen0();
        new Thread(() -> {
            for (int i = 0; i <=5; i++) {
                kitchen.increase();
            }
        },"rng").start();

        new Thread(() -> {
            for (int i = 0; i <=5; i++) {
                kitchen.dncrease();
            }
        },"ig").start();
    }
}
```

### 阻塞队列two（）
* Lock：线程阻塞
* Condition.await：线程等待
* Condition.signal：唤醒线程
* 传统方式实现线程通信之生产者消费者：一个初始值为0的变量 两个线程交替操作 一个加1 一个减1重复五次
```
class Kitchen{
    private Integer food = 0;
    Lock lock = new ReentrantLock();//初始化锁
    Condition condition  = lock.newCondition();

    public void increase() {
        lock.lock();//上锁
        try {
            while (food != 0){
                condition.await();//线程等待
            }
            food ++;
            System.out.println(Thread.currentThread().getName() + "\t生产了一个食物，当前：" + food);
            condition.signalAll();//唤醒所有线程
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();//解锁
        }
    }

    public void dncrease(){
        lock.lock();
        try {
            while (food == 0){
                condition.await();//线程等待
            }
            food --;
            System.out.println(Thread.currentThread().getName() + "\t消费了一个食物，当前：" + food);
            condition.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}


public class one {

    public static void main(String[] args) {
        Kitchen kitchen = new Kitchen();
        new Thread(() -> {
            for (int i = 0; i <=5; i++) {
                kitchen.increase();
            }
        },"rng").start();

        new Thread(() -> {
            for (int i = 0; i <=5; i++) {
                kitchen.dncrease();
            }
        },"ig").start();
    }
}
```

### 阻塞队列three
* 消息队列底层原理
```
/**
 * 3.0版本的阻塞队列
 * Volatile/CAS/AtomicInteger/AtomicReference/BlockingQueue
 * 实现MQ底层
 * @author langao_q
 * @create 2020-04-18 21:45
 */
class Source{
    private volatile boolean FLAG = true;
    private AtomicInteger atomicInteger = new AtomicInteger();//初始值为0

    private BlockingQueue<String> blockingQueue = null;
    public Source(BlockingQueue blockingQueue){
        System.out.println("传进来的BlockingQueue是：" + blockingQueue.getClass().getName());
        this.blockingQueue = blockingQueue;
    }

    //生产方法
    public void prod() throws InterruptedException {
        String data = null;
        while (FLAG){
            data = atomicInteger.incrementAndGet() + "";
            boolean rest = blockingQueue.offer(data, 2L, TimeUnit.SECONDS);//把生产的数据放入队列
            if(rest)
                System.out.println(Thread.currentThread().getName() + "\t生产了数据：" + data);
            else
                System.out.println(Thread.currentThread().getName() + "\t生产数据失败！：" + data);
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println(Thread.currentThread().getName() + "\t停止生产数据");
    }

    //消费方法
    public void consum() throws InterruptedException {
        String data = null;
        while (FLAG){
            data = blockingQueue.poll(2L, TimeUnit.SECONDS);//从队列中取出生产数据
            if(data == null || data.equalsIgnoreCase("")){
                FLAG = false;
                System.out.println(Thread.currentThread().getName() + "\t超时退出！");
                return;
            }
            System.out.println(Thread.currentThread().getName() + "\t消费了数据：" + data);
        }
    }

    //暂停方法
    public void stop(){
        FLAG = false;
        System.out.println("停止生产和消费数据！");
    }
}
public class three {
    public static void main(String[] args) throws InterruptedException {
        Source s = new Source(new ArrayBlockingQueue(10));
        new Thread(() -> {//生产线程
            try {s.prod();} catch (InterruptedException e) {e.printStackTrace();}
        },"rng").start();
        new Thread(() -> {//消费线程
            try {s.consum();} catch (InterruptedException e) {e.printStackTrace();}
        },"ig").start();

        TimeUnit.SECONDS.sleep(10);//main线程休息10s 让前面两个线程操作
        s.stop();//停止生产和消费
    }
}
```

### Callable（第三种实现多线程方式）
* 区别：相对与Runnable有返回值、能够抛出异常
* 启动方式：通过FutureTask
* 注意：尽量最后get()获取返回值（如果计算返回值需要时间 main线程会阻塞等待 直到计算完成）
* 
```
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
        System.out.println("Callable实现多线程的返回值：" + value);
    }
}
```

## 线程池
* 主要用来控制运行的线程的数量，处理过程中将线程放入队列，如果线程数量超出了队列数量 则需要排队等候等其他线程执行完毕再取出线程来执行
* 优点：线程复用；控制最大并发数；管理线程（底层是阻塞队列）
![image](https://blog.imwj.club//upload/2020/04/69f2ni6sv0j9dob4gsrs3q71f0.png)

### ThreadPoolExecutor（第四种实现多线程）
* Executors.newCachedThreadPool()（底层队列SynchronousQueue）  
创建一个根据需要创建新线程的线程池，但在可用时将重新使用以前构造的线程  
* Executors.newFixedThreadPool(int nThreads)（底层队列LinkedBlockingQueue）  
创建一个线程池，该线程池重用固定数量的从共享无界队列中运行的线程  
* Executors.newSingleThreadExecutor()（底层队列LinkedBlockingQueue）  
创建一个使用从无界队列运行的单个工作线程的执行程序  
* 总结：以上三种方式正式开发中并不会使用（这三个允许请求的长度都是Integer.MAX_VALUE 可能会堆积大量请求导致OOM[内存用完]），一般都是要自己手写线程池(ThreadPoolExecutor)
* 代码实现
```
        //jdk提供方式(不用)：ExecutorService threadPool = Executors.newFixedThreadPool(5);
        ExecutorService threadPool = new ThreadPoolExecutor(2,
                                                            5,
                                                            5,
                                                            TimeUnit.SECONDS,
                                                            new LinkedBlockingDeque<>(3),
                                                            Executors.defaultThreadFactory(),
                                                            new ThreadPoolExecutor.AbortPolicy());
        try {
            for (int i = 0; i < 10; i++) {//开启十个线程去访问
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t SUCCESS!");
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {//用完线程一定要关闭
            threadPool.shutdown();
        }
```

### 线程池七大参数
```
public ThreadPoolExecutor(int corePoolSize,//线程池钟常驻核心线程数
                              int maximumPoolSize,//线程池能够容纳同时执行的最大线程数（大于1）
                              long keepAliveTime,//多余线程存活时间
                              TimeUnit unit,//时间单位
                              BlockingQueue<Runnable> workQueue,//任务队列 被提交但尚未被执行的任务
                              ThreadFactory threadFactory,//线程工厂 创建新的线程（默认即可）
                              RejectedExecutionHandler handler)//拒绝策略，当线程队列满了且工作线程大于maximumPoolSize时如何拒绝
```

### 线程池底层工作原理
1.在创建线程池后，等待提交过来的任务请求  
2.当调用execute()方法添加一个任务请求时，线程池会马上创建线程运行这个任务  
    2.1如果正在运行的线程数量小于corePoolSize，那么马上创建线程运行这个任务  
    2.2如果正在运行的线程数量大于或等于corePoolSize，那么将这个任务`放入队列`  
    2.3如果队列满了且正在运行的线程数量小于maximumPoolSize，那么会创建非核心线程运行这个任务  
    2.4如果队列满了且正在运行的线程数量大于或等于maximumPoolSize，那么线程池会启动`饱和和拒绝策略`  
3.当一个线程完成任务时，它会从队列中取下一个任务来执行  
4.当一个线程闲置超过`keepAliveTime`的时间，线程池会判断  
    4.1如果当前线程数大于corePoolSize，那么这个线程会被停掉  
    4.2线程池的所有任务完成后它最终会收缩到corePoolSize的大小  
    
    
### 线程池的四大拒绝策略
* AbortPolicy(默认)：直接抛出RejectedException异常阻止系统正常运行
* CallerRunPolicy：即不抛异常 也不丢弃任务，回退给调用者
* DiscardoldestPolicy：抛弃队列中等待最久的任务
* DiscardPolicy：直接丢弃任务

### 线程池参数的合理配置
* CPU密集型：CPU核数+1
* IO密集型：CPU核数/(1-阻塞系数)，阻塞系数（0.8或0.9）


## 死锁
* 指的是两个或两个以上的线程在执行过程中，因争夺资源而造成一种**互相等待的现象**
### 产生原因
* 系统资源不足、进程推进的顺序不合适、资源分配不当
```
class Deadloc implements Runnable{
    private String lockAa,lockBb;
    public Deadloc(String lockAa, String lockBb){
        this.lockAa = lockAa;
        this.lockBb = lockBb;
    }
    @Override
    public void run() {
        synchronized (lockAa){
            System.out.println(Thread.currentThread().getName() + "\t 拿到锁："+lockAa);
            try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) { e.printStackTrace();}
            synchronized (lockBb){
                System.out.println(Thread.currentThread().getName() + "\t 拿到锁："+lockBb);
            }
        }
    }
}
public class DeadlockDemo {
    public static void main(String[] args) {
        Deadloc deadloc = new Deadloc("lockAa", "lockBb");
        Deadloc deadloc1 = new Deadloc("lockBb", "lockAa");
        new Thread(deadloc, "ThreadAAA").start();
        new Thread(deadloc1, "ThreadBBB").start();
    }
}
```

### 如何解决
* jps命令定位进程编号，jstack找到死锁查看
```
jps -l

jstack 进程编号
```

### Synchronized和Lock的区别
* Sync是关键字属于JVM层面，Lock是具体类属于Api层面
* Sync不需要手动释放锁，Lock需要手动释放锁 否则产生死锁
* Sync不可中断，Lock可以中断 相对Sync更加灵活
* Sync是非公平锁，Lock两者都可以 
* Lock有锁绑定多个条件Condition Sync没有（sync只能是唤醒所有或者唤醒一个 Lock可以精确唤醒）


### ThreadLocal的了解，实现原理。
每个线程都保存一份threadlocalmap的引用,以ThreadLocal和 ThreadLocal对象声明的变量类型作为参数