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