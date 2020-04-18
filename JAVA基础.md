## Java类的加载过程
* 加载、验证、准备、解析和初始化五个阶段

## Object
### boolean equlas(Object obj)  
> 比较对象内容是否一致。
```
1.前提: 参数是否是null
2.比较参数和当前对象类型是否一致通过getClass()比较。
3.比较内容
```

### hashCode
> 和equals成对出现。计算对象的hash码值。  
```
1.是配合equals实现多数据对象比较的。为了提高比对效率的。
2.相同对象返回相同Hash码。不同对象尽可能返回不同Hash码。
```

### finalize()
> 资源释放。GC调用。
```
一般在重量级的工具中定义。如：DataSource、SqlSessionFactory、线程池
```

### clone
> 克隆方法(原型模式)。深拷贝？浅拷贝？
```
浅克隆：创建一个新对象，新对象的属性和原来对象完全相同，对于非基本类型属性，仍指向原有属性所指向的对象的内存地址。  
深克隆：创建一个新对象，属性中引用的其他对象也会被克隆，不再指向原有对象地址。
```

## 值传递|引用传递
* 值传递：不会改变原有的内容，八大基本类型及其包装类、String
* 引用传递：会改变其内容，数组、对象


## 集合
### Collection
* Collections：集合的工具类 注意区分Collection是接口
#### List
* ArrayList：底层是数组。相对查询性能高。线程不安全。轻量级，初始化10 每次扩容为x1.5
* LinkedList：底层是双向循环链表。相对增删效率高。线程不安全。轻量级
* Vector：除了线程安全外，和ArrayList完全相同。重量级 每次扩容x2
##### Set
* Set是只有key没有value的map（底层就是HashMap）
* HashSet：无序不重复、允许null、线程不安全（哈希表结构）
* TreeSet：有序不重复、允许null、线程不安全（红黑树）
#### Map
* HashMap：key算法为散列算法，线程不安全（数组 + 链表）
* TreeMap： key排序Map，要求key必须可排序，平衡二叉树实现的
* Hashtable： 和HashMap区别是，线程安全。所有方法都是同步的。key和value不能为null。

### ArrayList线程不安全
* add()方法没有加Syn 多线程下出现并发修改异常java.util.ConcurrentModificationException
* 解决方案：
* 1.使用Vector替代ArrayList：Vector每一个方法都是带Syn的
* 2.Collections.synchronizedList()：给ArrayList外边包上一层Syn 但遍历的时候需要手动加上Syn
* 3.new CopyOnWriteArrayList()：写时复制，读写分离（内部使用lock机制）

### HashSet线程不安全
* add()方法没有加Syn 多线程下出现并发修改异常java.util.ConcurrentModificationException
* 解决方案：同ArrayList
* 底层时HashMap：只使用了HashMap的key，value为一个Object常量

### HashMap线程不安全
* add()方法没有加Syn 多线程下出现并发修改异常java.util.ConcurrentModificationException
* 解决方案：
* 1.使用ConcurrentHashMap替代HashMap：底层CAS算法
* 2.Collections.synchronizedMap()：给ArrayList外边包上一层Syn 但遍历的时候需要手动加上Syn

## IO
### stream字节流

#### Reader|Writer字符流

## 线程
### Thread（继承）
* start()： 启动线程。开启一个子线程，并启动。
* run()： 线程具体执行的内容。默认无返回值。线程执行结束后，JVM处理。
* yield()：放弃CPU执行时间片。run方法停止，等待CPU时间片分配。
* join()：连接，等待连接的线程执行结束后，再运行。
* interrupt()：中断，中断线程阻塞状态。被中断阻塞的线程抛出InterruptedException。被中断异常。
### Runnable（实现）
### ThreadLocal
> 线程资源绑定对象。底层是map。key是Thread.currentThread()。value是要绑定的资源。
* set(Object obj){ map.put(Thread.currentThread(), obj); }
* get(){ map.get(Thread.currentThread()); }
* 注意：当线程资源使用结束后，必须remove(){map.remove(Thread.currentThread());}。因为JVM中的线程对象是可复用的。线程对象的唯一确定，只考虑线程ID。


## 网络
> 网络开发编码相对简单。复杂在于网络的模式。是否阻塞，是否异步。常见的Socket开发，都是同步阻塞的。 开发最简单。
### Socket/ServerSocket
* 服务端
```
//监听某端口，提供服务。
ServerSocket ss = new ServerSocket(port);
ss.accept(socket);
```
* 客户端
```
Socket s = new Socket(ip, port);
```
#### BIO、NIO、AIO
* 同步异步
```
同步和异步是针对应用程序和内核的交互而言的，同步指的是用户进程触发IO操作并等待或者轮询前去查看IO操作是否就绪，而异步是指用户进程触发IO操作以后便开始做自己的事情，而当IO操作已经完成的时候会得到IO完成的通知。
```
* 阻塞非阻塞
```
阻塞和非阻塞是针对于进程在访问数据的时候，根据IO操作的就绪状态来采取的不同方式，说白了是一种读取或者写入操作方法的实现方式，阻塞方式下读取或者写入函数将一直等待，而非阻塞方式下，读取或者写入方法会立即返回一个状态值，你可以先进行其他操作

例子：

阻塞 ： ATM排队取款，你只能等待（使用阻塞IO时，Java调用会一直阻塞到读写完成才返回）；非阻塞 ： 柜台取款，取个号，然后坐在椅子上做其它事，等号广播会通知你办理，没到号你就不能去，你可以不断问大堂经理排到了没有
```
* BIO：同步阻塞的编程方式。
```
客户端启动Socket发起网络请求，默认情况下ServerSocket回建立一个线程来处理此请求，如果服务端没有线程可用，客户端则会阻塞等待或遭到拒绝。  
BIO方式适用于连接数目比较小且固定的架构
```
* NIO： 同步非阻塞的编程方式
```
当socket有流可读或可写入socket时，操作系统会通知相应的应用程序进行处理，应用再将流读取到缓冲区或写入操作系统。也就是说，这个时候，已经不是一个连接就要对应一个处理线程了，而是有效的请求，对应一个线程，当连接没有数据时，是没有工作线程来处理的
```
* AIO：异步非阻塞的编程方式
```
对于读操作而言，当有流可读取时，操作系统会将可读的流传入read方法的缓冲区，并通知应用程序；对于写操作而言，当操作系统将write方法传递的流写入完毕时，操作系统主动通知应用程序
```

## 反射
> 动态获取指定类及类中的方法和属性，并运行其内容，极大的提高了程序的扩展性
```			
        Class clazz = Class.forName("com.imwj.servlet.ImServlet");
        //2.通过字节码文件创建实例对象
        Object obj = clazz.newInstance();
        //3.通过字节码文件获取方法(两个参数：第一个是方法名称；第二个参数是方法的参数)
        Method method = clazz.getMethod("service", null);
        //4.调用invoke方法执行实例对象里面的方法(前面写的方法init)【两个参数：第一个是调用方法的实例对象，第二个是方法的实参】
        method.invoke(obj, null);
```

## Servlet
### Servlet结构
* Servlet
是Servlet规范的顶级接口。核心关注方法是服务方法。
```
public void service(ServletRequest request, ServletResponse response) throws IOException, ServletException;
```
* GenericServlet
```
是Servlet规范提供的Servlet接口适配器类型  
适配器模式：是连接两个不同类型的中间桥梁。可以通过这个桥梁，将两个不相关的类型连接起来。  
```
* HttpServlet
是GenericServlet的子类型，是依赖于Http协议的Servlet实现。因为在这个类型中的唯一方法service中，直接进行方法参数的强制类型转换。
```
protected void service(HttpServlertRequest, HttpServletResponse)throws IOException, ServletException;
```
### Filter
> 为Servlet提供一个安全的服务环境。并设置统一的配置信息。 Filter执行的时候，Servlet是未调用状态。服务未提供。在服务提供前，完成所有的安全校验和统一配置，可以节省服务器资源消耗。
```
主要处理内容：安全校验、设置字符集、静态资源处理
实现接口Filter，核心方法是void doFilter(ServletRequest, ServletResponse,FilterChain)
```
```
过滤器（Filter） VS 拦截器（Interceptor）
拦截器是做什么的？
是给服务增加额外功能的代码。如：日志、事务、授权、登录成功后的资源初始化、退出成功后的资源回收。

理论上，在过滤器中可以实现的逻辑，在拦截器中都可以实现。反之不可。

如果将过滤需要实现的逻辑，附加到拦截器上，会造成服务器资源的额外消耗。
因为每个拦截器执行，意味着，服务已经开始执行了。
```
### Listener
> 监听器。监听各种事件，处理不同的事件。
```
ServletContextListener监听ServletContext初始化和销毁的监听器,
HttpSessionListener 监听HttpSession初始化和销毁的监听器,
HttpSessionAttributeListener 监听HttpSession中的Attribute变更的监听器,
HttpSessionBindingListener 监听HttpSession中的Attribute绑定的监听器,
ServletRequestListener 监听ServletRequest请求对象创建销毁的监听器,
ServletRequestAttributeListener 监听ServletRequest中Attribute变更的监听器。
```

### MVC模式
> 分层，模型层（module）、视图层（view）、控制层（controller），追求高内聚、低耦合！

## 异常处理
> 运行时异常RuntimeException 又叫做非可查异常，在编译过程中，不要求必须进行显示捕捉
> 一般异常又叫做可查异常，在编译过程中，必须进行处理，要么捕捉，要么通过throws 抛出去.
* 异常处理机制
```
异常的处理：代码中，绝对不能直接throws。异常的传递：代码中，除自开发的根以外，不能只try…catch。

Mapper|DAO: 代码必须try…catch，处理异常，且将捕获的异常封装后再抛出。

service：代码必须try…catch，且将捕获的异常封装后再抛出。

controller：只try…catch，除非自定义了ExceptionHandler。
```
* return
```
try里的return 和 finally里的return 都会执行，但是当前方法只会采纳finally中return的值
```
* 常见异常
```
NullPointerException 空指针异常
ArithmeticException 算术异常，比如除数为零
ClassCastException 类型转换异常
ConcurrentModificationException 同步修改异常，遍历一个集合的时候，删除集合的元素，就会抛出该异常
IndexOutOfBoundsException 数组下标越界异常NegativeArraySizeException 为数组分配的空间是负数异常
```

## 两种动态代理
* JDK动态代理：利用反射机制生成一个实现代理接口的匿名类，在调用具体方法前调用InvokeHandler来处理.
* CGlib动态代理：利用ASM（开源的Java字节码编辑库，操作字节码）开源包，将代理对象类的class文件加载进来，通过修改其字节码生成子类来处理。


# Reids/RabbitMQ
Redis、RabbitMQ加分项：举个项目例子

1.不要用qq邮箱
2.住址不要湖南湘潭
3.对自己写的技术特长非常了解
4.技术栈太浅
5.前端三大框架vue、redict、Angela，三选二
6.表现自己学习能力强
7.描述在项目中的重点
8.耐心 耐心 耐心
9.加深对spring的理解、设计模式