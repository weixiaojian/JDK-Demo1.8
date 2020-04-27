## JVM
### 双亲委派模式
[Bootstrap ClassLoader]  
[Extension ClassLoader]   
[App ClassLoader]  
[自定义类加载器]  
[自定义类加载器]  

*  某个特定的类加载器在接到加载类的请求时，首先将加载任务委托给父类加载器，依次递归，如果父类加载器可以完成类加载任务，就成功返回；只有父类加载器无法完成此加载任务时，才自己去加载。
* 避免重复加载 + 避免核心类篡改

#### JVM内存结构
* 堆：存放所有new出来的对象（管运行）
* 虚拟机栈：存放基本数据类型、局部变量、对象的引用（管储存）
* 本地方法栈：
* 程序计数器：
* 方法区：
![image](https://blog.imwj.club//upload/2020/04/7h1k1lov56gkcqmf95f3mln8p3.png)

## 类的初始化
* 父类静态代码块 > 子类静态代码块 > 父类普通变量及其语句块 > 父类构造方法 > 子类普通变量及其语句块 > 子类构造方法

## GC
### 分代
> * Java虚拟机根据对象存活的周期不同，把堆内存划分为几块，一般分为新生代、老年代和永久代（对HotSpot虚拟机而言），这就是JVM的内存分代策略。
> * 堆内存是虚拟机管理的内存中最大的一块，也是垃圾回收最频繁的一块区域，我们程序所有的对象实例都存放在堆内存中
> * 新生代中的对象存活时间短，只需要在新生代区域中频繁进行GC，老年代中对象生命周期长，内存回收的频率相对较低，不需要频繁进行回收，永久代中回收效果太差，一般不进行垃圾回收
### 分代划分
* 新生代1/3，老年代2/3
![image](https://blog.imwj.club//upload/2020/04/37nj67852ui6spb5q2806hm35a.png)

#### 新生代（Young Generation） Minor GC
* HotSpot将新生代划分为三块，一块较大的Eden（伊甸）空间和两块较小的Survivor（幸存者）空间，默认比例为8：1：1。 划分的目的是因为HotSpot采用复制算法来回收新生代，设置这个比例是为了充分利用内存空间，减少浪费。新生成的对象在Eden区分配（大对象除外，大对象直接进入老年代），当Eden区没有足够的空间进行分配时，虚拟机将发起一次Minor GC
* 新生成的对象优先存放在新生代中，新生代对象朝生夕死，存活率很低，在新生代中，常规应用进行一次垃圾收集一般可以回收70% ~ 95% 的空间，回收效率很高。
* GC开始时，对象只会存在于Eden区和From Survivor区，To Survivor区是空的（作为保留区域）
* GC进行时，Eden区中所有存活的对象都会被复制到To Survivor区，而在From Survivor区中，仍存活的对象会根据它们的年龄值决定去向（默认为15）年龄值达到年龄阀值的对象会被移到老年代中，没有达到阀值的对象会被复制到To Survivor区。接着清空Eden区和From Survivor区，新生代中存活的对象都在To Survivor区。
#### 老年代（Old Generationn） Full GC
* 老年代中的对象生命周期较长，存活率比较高，在老年代中进行GC的频率相对而言较低，而且回收的速度也比较慢。
* 对象的大小大于Eden的二分之一会直接分配在old
#### 永久代（Permanent Generationn）
* 永久代存储类信息、常量、静态变量、即时编译器编译后的代码等数据对这一区域而言，Java虚拟机规范指出可以不进行垃圾收集，一般而言不会进行垃圾回收
* 1.8之后替换成了Metaspace（元空间 使用本地内存）
### 分代垃圾收集器分类
#### 次收集器Minor GC
* 发生在新生代的垃圾回收
* 当Eden空间不足以为对象分配内存时，会触发Scavenge GC，收集间隔较短
#### 全收集器Full GC
* 指发生在老年代的GC，出现了Full GC一般会伴随着至少一次的Minor GC，
* 当老年代或者用久代堆空间满了才会触发，收集间隔较长
* 可以使用System.gc()方法来显式的启动全收集
### 分代垃圾收集器（七个）
* 串行收集器（Serial）  
它为单线程环境设计 且只使用一个线程进行回收，回收时会暂停所有用户线程 不适合服务器环境
* 并行收集器（ParNew默认）  
多个线程并行工作，此时用户线程时暂停的 适用于大数据首页弱用户交流页面
* Parallel Scavenge收集器
* Serial Old收集器
* Parallel Old收集器
* CMS收集器（并发收集器）  
用户线程和垃圾收集线程同时执行，不需要停顿 常用它作为正式环境
* 分区收集- G1收集器(新生代/老年代通用)  
将堆内存分割成不同的区域然后并发的对其进行垃圾回收
```
-XX:+UseSerialGC
```

### 垃圾回收算法（四个）
#### 引用计数（Reference Counting）
* 此对象有一个引用，即增加一个计数，删除一个引用则减少一个计数。
* 无法处理循环引用的问题
#### 复制（Copying） 
* 新生代Minor GC中使用
* 垃圾回收时，遍历当前使用区域，把正在使用中的对象复制到另外一个区域中
* 复制成本较小，但需要两倍内存空间
#### 标记-清除（Mark-Sweep）
* 老年代Full GC中使用
* 此算法执行分两阶段。第一阶段从引用根节点开始标记所有被引用的对象，第二阶段遍历整个堆，把未标记的对象清除
* 需要暂停应用，且会产生内存碎片
#### 标记-整理（Mark-Compact）
* 结合了“标记-清除”+“复制”
* 第一阶段从根节点开始标记所有被引用对象，第二阶段遍历整个堆，把清除未标记对象并且把存活对象“压缩”到堆的其中一块，按顺序排放

### GC Roots
* Java垃圾回收中是通过一组名为“GC Roots”的对象作为起点，从该起点向下搜索（根链路扫描） 未能遍历到则说明该对象不可用
* GC Roots：  
1.虚拟机栈中引用的对象
2.方法区中的类静态属性引用对象
3.方法区常量引用对象
4.本地方法栈中Natvie引用的对象

## JVM调优/参数配置
* 标配参数：java -version
* X参数：了解
* XX参数：常用

### XX参数
#### boolean类型
* `-`表示关闭，`+`表示开启
* 查看所有正在运行的Java程序`jps -l`
* 查看指定参数`jinfo -flag 配置项 程序进程id`，例：jinfo -flag PrintGCDetails 1176
* 设定指定参数`-XX:-PrintGCDetails`，需要在VM options中配置

#### K-V类型
* 查看指定参数`jinfo -flag 配置项 程序进程id`，例：jinfo -flag MaxTenuringThreshold 1176
* 设定指定参数`-XX:MaxTenuringThreshold=15`，需要在VM options中配置(MaxTenuringThreshold是新生代晋级到老年代的次数)

#### jinfo 查看当前运行程序的配置
* 先要通过`jsp -l`查看运行程序的进程id
* 查看指定参数`jinfo -flag 配置项 程序进程id`，例：jinfo -flag InitialHeapSize 7328

#### 经典参数-Xms、-Xmx
* 这两个也是属于XX参数类型的
* -Xms等价于InitialHeapSize(初始化堆的大小，默认内存1/64)，Xmx等价于MaxHeapSize(最大堆的大小，默认内存1/4)
* -Xss等价于ThreadStackSize（单个线程栈空间的大小512k-1024k）

#### 盘点查看JVM默认值
* `=`表示初始值未修改，`:=`手动修改过的值
* 查看初始值
```
java -XX:+PrintFlagsInitial 
```
* 主要查看修改更新
```
java -XX:+PrintFlagsFinal
```
* 运行Java命令的同时打印参数
```
java -XX:+PrintFlagsFinal -Xss128k T //T运行Java类的名字
```
* 查看Java的Xms、Xmx等常用参数以及GC垃圾收集器
```
java -XX:+PrintCommandLineFlags
```

### JVM常用配置
* -Xms、-Xmx、-Xss
* -Xmn：设置年轻代的大小
* -XX:MetaspaceSize：设置元空间的大小，其实就是用内存条的大小（但默认21M）
### 总结（案例设置）
* 1.VM options中配置参数`-XX:+PrintCommandLineFlags`，运行main方法后会打印：Java的Xms、Xmx等常用参数以及GC垃圾收集器
* 2.配置我们自己的-Xms、-Xmx、-Xss等（UseSerialGC串行收集器）
```
-Xms128m -Xmx4096m -Xss1024k -XX:MetaspaceSize=512m -XX:+PrintCommandLineFlags -XX:+PrintGCDetails -XX:+UseSerialGC
```
#### InitialHeapSize 
* -Xms：初始化堆的大小，等价于`-XX:InitialHeapSize`默认大小是内存的1/64
#### MaxHeapSize
* -Xmx：最大堆的大小，等价于`-XX:MaxHeapSize`默认大小是内存的1/4
#### ThreadStackSize
* -Xss：单个线程栈的大小，等价于`ThreadStackSize`
#### MetaspaceSize
* -XX:MetaspaceSize：元空间的大小，默认大小21m
#### PrintCommandLineFlags
* -XX:PrintCommandLineFlags：打印Java的常用参数如Xms、Xmx
#### PrintGCDetails
* -XX:PrintGCDetails：打印GC回收详情
#### UseSerialGC
* -XX:+UseSerialGC：串行垃圾收集器

#### GC详情面板
* -XX:PrintGCDetails：打印GC回收详情
```
[GC (Allocation Failure) [PSYoungGen: 2048K->488K(2560K)] 2048K->708K(9728K), 0.0177434 secs] [Times: user=0.00 sys=0.00, real=0.03 secs] 
GC名称(Young)         【GC前Young内存占用->GC后Young内存占用(Young总大小)】 【GC前JVM堆占用->GC后JVM堆占用(JVM堆大小)】 【用户耗时，系统耗时，实际耗时】
```

#### SurvivorRatio
* `-XX:SurvivorRatio=8`：新生代中Eden/Survivo的比例，默认为8（8：1：1）

#### NewRation
* `-XX:NewRatio=2 `：堆内存中 老年代/新生代 的比例，默认为2（1 ：2）

#### MaxTenuringThreshold
* `-XX:MaxTenuringThreshold=15`：新生代晋级到老年代的次数，默认15

### 四大引用（强、软、弱、虚）
* Reference是Object下的一个子类，java.lang.ref.Reference
#### 强引用
* 无论是GC还是OOM(内存用完了)都不会清理掉强引用
```
Object obj = new Object();
Object obj1 = obj; //两个都是强引用
obj = null; //这样会清理掉obj，并不会清理掉obj1
```

#### 软引用SoftReference
* 内存充足时保留，内存不够时进行回收
```
Object obj = new Object();
SoftReference obj1 = new SoftReference(obj);
obj = null; //这样会清理掉obj，但在内存不足的时候就会把obj1清理掉
```

#### 弱引用WeakReference 
* 只要触发GC弱引用对象就会被回收掉
* WeakHashMap
```
Object obj = new Object();
WeakReference obj1 = new WeakReference(obj);

obj = null;
System.gc(); //gc之后obj1就等于null了
```

#### 虚引用PhantomReference 
* 形同虚设，如果一个对象仅持有虚引用那么它就和没有任何引用一样在任何时候都可能被回收掉，不能单独使用需要配合`ReferenceQueue`使用

### OOM(内存用完)
#### StackOverflowError 
* 栈溢出（方法递归调用）
* 解决方案：查看系统是否有使用copy大百内存的代码或死循环；添加JVM配置，来限制使用内存
```
    public static void StackOverflowErro(){
        StackOverflowErro();//递归调用
    }
```
#### OutOfMemeoryError:Java heap sapce
* Java内存堆用完
* 解决方案：加大-Xms、-Xmx的空间
```
    public static void OOMJavaHeapSpace(){
        byte[] bytes = new byte[20 * 1024 * 1024];
    }
```
#### OutOfMemeoryError：GC overhead limit exceeded
* GC回收时间过长，GC回收做了无用功
```
    public static void OOMGCoverHeadLimitExceeded(){
        int i = 0;
        List<String> list = new ArrayList<>();
        try {
            while (true){
                list.add(String.valueOf(i++).intern());
            }
        }catch (Exception e){
            System.out.println("*************i：" + i);
            e.printStackTrace();
        }
    }
```
#### OutOfMemeoryError：Direct buffer memory
* 直接内存溢出，常见于NIO程序（Netty）；JVM内存外的内存区域没有足够的空间了

#### OutOfMemeoryError：unable to create new native thread
* 线程创建数超出系统极限，linux默认一个进程的最大线程数是1024
* 解决方案：将代码中线程数降到最低、如果确实需要超高线程数可以修改linux默认参数
```
        for (int i = 0; ; i++) {
            System.out.println("************" + i);
            new Thread(() -> {
                try {TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);} catch (Exception e) {e.printStackTrace();}
            }, "" + i).start();
        }
```

#### OutOfMemeoryError：Metaspace
* 元空间内存溢出，元空间包含类信息、常量、静态变量、即时编译器编译后的代码等数据
* 生成类的数量超出元空间大小