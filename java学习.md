# Java

### sleep() 和 wait() 及 notify()/notifyAll() 的异同

代码：WaitAndNotify.java

1. sleep() 会让出 CPU 给其他的线程，但是监控状态依然保持（保持锁）。wait() 会放弃当前持有的锁。

2. 两者都是从 sleep()/wait() 执行处恢复运行。

3. 两者都需要捕获 InterruptedException 。

4. notify()/notifyAll() 不会释放锁，也不会抛出异常。

5. notify() 唤醒一个线程，具体唤醒哪个线程由线程调度器决定。notifyAll() 会唤醒所有 wait() 的线程，然后线程们会开始竞争锁，成功获得锁的线程继续运行，失败者继续等待锁释放，再竞争。

----------

### Java daemon（守护）线程

代码：Synchronized.java

1. 当主线程 main 结束时，守护线程自动退出。主线程会等待用户线程（普通线程）执行完毕。

2. Thread 对象通过 setDaemon 来设置线程是否是守护线程。 

3. 守护线程创建的线程默认为守护线程

----------

！外部类不能用 `protected` 修饰，却可以被 `default` 修饰。*可能仅仅是规定，并没有具体的原因。*

----------

### HashSet 等如何判断元素相同

首先取对象 hashCode()，如果 hashCode 相同，再用 equals() 判断，如果依然相等，则是同一个元素。

----------

### ThreadLocal 的原理及应用

ThreadLocal **不是为了满足多线程安全**而开发出来的，因为局部变量已经足够安全。 ThreadLocal 是为了方便线程处理自己的某种状态。

*网上有些文章写道：*

> 用 ThreadLocal<Connection\>来避免频繁创建 Connection 。

*这其实是错误的。即使用了 ThreadLocal ，每一个线程依然需要创建自己的 Connection 而不是直接使用唯一公有 Connection 对象的拷贝。因为使用了拷贝依然存在多线程不安全的问题。*

Thread 类中有一个 threadLocals 成员，变量类型是 ThreadLocalMap。这是每个 Thread 都有的，所以数据 Map 并不是在 ThreadLocal 中，而是在每个 Thread 的 ThreadLocalMap 里。

ThreadLocalMap Key 为 ThreadLocal<?>，值为 Object，根据调用的 ThreadLocal 对象修改对应的 Object 。因此 ThreadLocal 保存对象本质依然是线程内的局部变量。

至于为何 ThreadLocalMap 中要用 WeakReference 。原因在于，ThreadLocal 对象的回收，不应该被 ThreadLocalMap 所影响。一旦某个 ThreadLocal 不再被其他对象引用，那么说明这个 ThreadLocal 对象已经不会再被使用，即使它还存在于 ThreadLocalMap 中。

----------

### JVM 部分配置参数解释

`-Xmx10240m` 堆最大值

`-Xms10240m` 堆初始值 *也就是堆最小值*

`-Xmn5120m` 年轻代大小 *Young Gen = Eden + 2\*Survivor*

`-XXSurvivorRatio=3` 表示 Eden 与一个 Survivor 的比值 *Eden = 3\*Survivor*

`-XX:PermSize=64m` 持久代大小

`-XX:NewRatio=4` 年轻代与年老代比值 *Old Gen = 4\*Young Gen*
 
----------

### JVM 堆组成及 GC 简单过程

Heap 主要分为两个部分：

1. 年轻代 (Young Generation)

2. 年老代 (Old Generation)

**MinorGC 的简单过程**

年轻代分为一个 Eden 区和两个 Survivor 区 (From, To)，新创建的对象在  Eden 区。

在 Minor GC 开始时， To 区是空的。 Eden 中的存活对象会移动到 From 区，而 From 区的对象会根据对象经历的 GC 次数来决定去向，超过指定阈值的对象会移动到年老代，其他的则移动到 To 区。

移动完毕后，GC 会将 Eden 区和 From 区清空。然后将之前的 To 区指定为 From 区，之前的 From 区被指定为 To 区，至此 Minor GC 完毕。如果在 GC 将 From 移动至 To 区时满了，则将 To 区的所有对象移动到年老代。

**触发 Full GC 的条件**

1. 年老代空间不足。*创建大对象、大数组时，会将对象直接放入年老代，可能导致年老代空间不足。*

2. 永久代空间不足。*仅存在于 HotSpot，此种情况较少出现，永久代空间一般设置为默认的 64MB。JDK8 移除了永久代，变成 Metaspace 。*

3. 年轻代至年老代平均升级大小大于年老代剩余空间。

4. 调用 System.GC() 。

5. CMS 过程中。

**几种收集器的实现**

1. 新生代上的GC实现

- Serial：单线程的收集器，只使用一个线程进行收集，并且收集时会暂停其他所有工作线程（Stop the world）。它是Client模式下的默认新生代收集器。
- ParNew：Serial收集器的多线程版本。在单CPU甚至两个CPU的环境下，由于线程交互的开销，无法保证性能超越Serial收集器。
- Parallel Scavenge：也是多线程收集器，与ParNew的区别是，它是吞吐量优先收集器。吞吐量=运行用户代码时间/(运行用户代码+垃圾收集时间)。另一点区别是配置-XX:+UseAdaptiveSizePolicy后，虚拟机会自动调整Eden/Survivor等参数来提供用户所需的吞吐量。我们需要配置的就是内存大小-Xmx和吞吐量GCTimeRatio。

2. 老年代上的GC实现

- Serial Old：Serial收集器的老年代版本。
- Parallel Old：Parallel Scavenge的老年代版本。此前，如果新生代采用PS GC的话，老年代只有Serial Old能与之配合。现在有了Parallel Old与之配合，可以在注重吞吐量及CPU资源敏感的场合使用了。
- CMS：采用的是标记-清除而非标记-整理，是一款并发低停顿的收集器。但是由于采用标记-清除，内存碎片问题不可避免。可以使用-XX:CMSFullGCsBeforeCompaction设置执行几次CMS回收后，跟着来一次内存碎片整理。
- Serial New收集器是针对新生代的收集器，采用的是复制算法。
- Parallel New（并行）收集器，新生代采用复制算法，老年代采用标记整理。
- Parallel Scavenge（并行）收集器，针对新生代，采用复制收集算法。
- Serial Old（串行）收集器，新生代采用复制，老年代采用标记整理。
- Parallel Old（并行）收集器，针对老年代，标记整理。
- CMS收集器，基于标记清理。
- G1收集器：整体上是基于标记整理 ，局部采用复制。

----------

### JVM 内存分布

1. 堆 Heap
2. 方法区 Method Area
3. 虚拟机栈 VM Stack
4. 本地方法栈 Native Method Stack
5. 程序计数器 Program Counter

其中 1, 2 线程间共享，3, 4, 5 线程独有。

----------

### 形成死锁的四个必要条件

1. 互斥
2. 不可剥夺
3. 请求与保持
4. 循环等待

----------

### 双亲委派 (parent-delegation model)

**类的唯一性**：在 JVM 中，一个类的唯一性由该类本身和该类的加载器确定。

如果同一个类由不同的加载器加载，结果会产生不同的类。JVM 为了保证同一个类由相同的类加载器加载，引入了双亲委派机制。

**双亲委派的主要流程**：

1. 子类收到类加载请求时，先委托父类加载。

2. 如果没有父类，则在自己加载范围内查找目标类，若找不到目标类就返回子类。

3. 递归上述过程，直到返回初始的类加载器（第一个收到请求者）。

遵守上述流程，加载器只会在自身和父类加载器中寻找目标类，不会去请求自己的子类。

**几个特殊的加载器**：

- 启动类加载器 Bootstrap ClassLoader 顶层加载器，由 C++ 实现，负责加载 `%JRE_HOME%/lib` 和 JVM `-Xbootclasspath` 指定目录中的类。

- 扩展类加载器 Extension ClassLoader 	加载 `%JRE_HOME%/lib/ext` 中的类。

- 应用程序类加载器 Application ClassLoader 负责加载当前应用程序 classpath 下的类。

**破坏双亲委派机制**

使用 `Class.forName(DriverName, false, loader)` 加载类时，调用的是当前类的 ClassLoader。

`Thread.currentThread().getContextClassLoader()` 返回当前应用程序类加载器，实现父类委派子类加载类，破坏了双亲委派机制。

----------

### JNDI JDBC SPI

- **JNDI(Java Naming and Directory Interface)Java 命名和目录接口** *JNDI 将对象以名称的形式绑定到容器环境(Context)中，在程序中通过 Context 的 lookup 查找名称对应的对象或是节点。每一个 Context 都是一个节点，节点名称和对象名称组成目录。这样的对象就可以通过容器管理而不需要代码来管理。*

- **JDBC(Java DataBase Connectivity)Java 数据库连接** *这个比较好理解，JDBC 定义了一系列数据库操作的标准方法，利用 Java 进行数据库操作时只需要使用 JDBC 定义的标准方法，不需要考虑具体的数据库细节。具体的细节由不同数据的驱动(Driver)来实现，例如：`com.mysql.jdbc.Driver`。*

- **SPI(Service Provider Interface)服务提供者接口** *一种服务发现机制，提供动态的实现更换。很常见、很重要的机制。相关资料：1.[**Java SPI机制简介**](http://www.cnblogs.com/zhongkaiuu/articles/5040971.html) 2.[**【java规范】Java spi机制浅谈**](https://singleant.iteye.com/blog/1497259)*

----------

### Java 多态
 
多态分为两种：

1. 编译时多态 *重载 Overload，根据参数的静态类型和被调用对象的静态类型来决定运行的方法。*
2. **运行时多态** *重写 Override，根据被调用对象的实际类型来决定调用哪个方法。*

**理解“静态多分派，动态单分派”**

> 编译时期确定方法有两点依据,1.调用方法的静态类型,2.方法的参数,所以说是静态多分派;
> 
> 运行时期再确定方法只有一点依据,1.就是调用方法的实际类型,所以说是动态单分派;
>
> 所以最后实际的方法调用是编译和运行的结合,即调用方法的实际类型和参数(运行时期直接引用会根据调用方法的实际类型确定,编译时期调用方法的静态类型只不过是虚引用),按书上说法,在编译时期,方法名和参数就被确定了,运行时只需要确定调用者即可;所以方法选择上,编译时期缩小了范围,运行时期确定了具体的方法;
> 
> 静态多分派,动态单分派,也就是这个意思了;

相关资料：

[java方法的多态性理解](https://www.cnblogs.com/liujinhong/p/6003144.html)

----------

### Java Enum（枚举）

枚举类型本质是类 Class，有几个**特点**：
1. 只有一个实例。
2. 构造函数默认是 private，而且必须是 private。
3. 懒加载(lazy loading)。

**默认类方法**：
- values()	以数组形式返回枚举类型的所有成员
- valueOf()	将普通字符串转换为枚举实例
- compareTo()	比较两个枚举成员在定义时的顺序
- ordinal()	获取枚举成员的索引位置

扩展： EnumSet, EnumMap *Key 必须是同一个枚举类型的成员。*

----------

### Java 多线程

**Lock**

常见有 ReentrantLock。ReentrantLock 比 Synchronized 更加强大

1. 可以设置为公平锁，避免饥饿。

2. 有 tryLock() 方法，获取锁超时。

3. 有 newCondition() 方法获取多个等待队列，通过 Condition 的 await() 和 signal()/signalAll() 来等待和唤醒线程。

4.  lockInterruptibly() 可中断锁等待，避免线程无限等待。

**BlockingQueue**

主要由三组方法：

1. add()/remove() 如果队列满或者空，会抛出异常 IllegalStateException。

2. offer()/poll() 如果队列满，返回 false；如果队列空，返回 null。这组方法可以设置超时时间。

3. put()/take() 如果队列满或者空，会阻塞。

第 2 与第 3 组需要捕获 Checked Exception: InterruptedException。 

**ExecutorService**

Java 线程池，由于 new Thread(...).start() 方式的进程创建代价昂贵，因此实际使用中，都会采用线程池的方式来管理线程。

**参数解析**

	public ThreadPoolExecutor(int corePoolSize,
		int maximumPoolSize,
		long keepAliveTime,
		TimeUnit unit,
		BlockingQueue<Runnable> workQueue,
		ThreadFactory threadFactory,
		RejectedExecutionHandler handler)

- corePoolSize : 核心线程数，一旦创建将不会再释放。如果创建的线程数还没有达到指定的核心线程数量，将会继续创建新的核心线程，直到达到最大核心线程数后，核心线程数将不在增加；如果没有空闲的核心线程，同时又未达到最大线程数，则将继续创建非核心线程；如果核心线程数等于最大线程数，则当核心线程都处于激活状态时，任务将被挂起，等待空闲线程来执行。

- maximumPoolSize : 最大线程数，允许创建的最大线程数量。如果最大线程数等于核心线程数，则无法创建非核心线程；如果非核心线程处于空闲时，超过设置的空闲时间，则将被回收，释放占用的资源。

- keepAliveTime : 也就是当线程空闲时，所允许保存的最大时间，超过这个时间，线程将被释放销毁，但只针对于非核心线程。

- unit : 时间单位，TimeUnit.SECONDS等。

- workQueue : 任务队列，存储暂时无法执行的任务，等待空闲线程来执行任务。

- threadFactory :  线程工程，用于创建线程。

- handler : 当线程边界和队列容量已经达到最大时，用于处理阻塞时的程序

**四种常见线程池类型**

- 单线程池 SingleThreadExecutor

	FinalizableDelegatedExecutorService 是一个装饰器类，隐藏了 ThreadPoolExecutor 的部分方法，并且会在 GC 时自动调用 executor.shutdown()。

		public static ExecutorService newSingleThreadExecutor() {
			return new FinalizableDelegatedExecutorService
				(new ThreadPoolExecutor(1, 1,
				0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>()));
		}

- 固定线程池 FixedThreadPool

	FixedThreadPool(1) 与 SingleThreadExecutor 的参数定义完全一致，区别在于没有装饰器类。

		public static ExecutorService newFixedThreadPool(int nThreads) {
			return new ThreadPoolExecutor(nThreads, nThreads,
			  0L, TimeUnit.MILLISECONDS,
			  new LinkedBlockingQueue<Runnable>());
		}

- 缓存线程池

	适合生命周期短的任务。

		public static ExecutorService newCachedThreadPool() {
		        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
		                                      60L, TimeUnit.SECONDS,
		                                      new SynchronousQueue<Runnable>());
		 }

- 延时线程池

	循环执行任务。有两个特殊的方法 scheduleAtFixedRate() 与 scheduleWithFixedDelay()。前者表示相邻两次启动方法的时间点差值固定，也就是不把执行时间计算进去。后者指在上个任务执行完毕后，延迟指定的时间。**需要注意**：即使任务执行时间超过间隔时间，scheduleAtFixedRate() 也不会创建新线程去执行，而是等待上个任务结束完毕后，立即开始下个任务。

		public ScheduledThreadPoolExecutor(int corePoolSize) {
		        super(corePoolSize, Integer.MAX_VALUE, 0, TimeUnit.NANOSECONDS,
		              new DelayedWorkQueue());
		}

相关资料：

[threadPoolExecutor 中的 shutdown() 、 shutdownNow() 、 awaitTermination() 的用法和区别](https://blog.csdn.net/u012168222/article/details/52790400)

----------

### Java 克隆(clone)


----------

### Java 反射(reflection)

**Java 反射机制介绍**

> JAVA反射机制是在运行状态中，对于任意一个类，都能够知道这个类的所有属性和方法；对于任意一个对象，都能够调用它的任意方法和属性；这种动态获取信息以及动态调用对象方法的功能称为java语言的反射机制。

注意：反射可以获取所有的方法和域，包括私有的。

**获得 Class 对象**

1. `MyClass.class` 直接获取指定类的 Class 对象，这种方法，不会自动初始化类。

2. `classLoader.loadClass("MyClass")` 将 MyClass 载入 JVM ，不会初始化。

3. `Class.forName("MyClass", true, classLoader)` true 表示加载后初始化类，false 反之。

**newInstance 和 new 区别**

1. newInstance 只能调用无参构造器，new 可以调用任意。如果没有无参构造器，抛出 InstantiationException，如果无权限调用无参构造器，抛出 IllegalAccessException。

2. newInstance 可扩展性强于 new。

如果需要使用有参构造器，可以通过 constructor = getConstructor(Param.class) ，调用构造器的 constructor.newInstance(param)。

**Class 的对象方法**

- `getClassLoader()` 获取类的类加载器。

- `getFields()/getDeclaredFields() 与 getField(fieldName)/getDeclaredField(fieldName)` 前者获取类的所有公共字段，包括父类中公共字段的。后者获取所有字段，包括私有字段，但是不包括父类中声明的。

- `getConstructor(... paramClass)` 同上。

- `getDeclaredMethod(name, ... paramClass)` 同上

- `asSubclass(A.class)` 将 Class<?> 转化为 Class<? extends A>

----------

### Java 泛型

相关资料：

[java 泛型详解-绝对是对泛型方法讲解最详细的，没有之一](https://www.cnblogs.com/coprince/p/8603492.html)

----------
