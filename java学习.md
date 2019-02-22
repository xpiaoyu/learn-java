# Java

---

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

> 用 ThreadLocal\<Connection\>来避免频繁创建 Connection 。

*这其实是错误的。即使用了 TheadLocal ，每一个线程依然需要创建自己的 Connection 而不是直接使用唯一公有 Connection 对象的拷贝。因为使用了拷贝依然存在多线程不安全的问题。*

Thread 类中有一个 threadLocals 成员变量类型是 ThreadLocalMap。这是每个 Thread 都有的，所以数据 Map 并不是在 ThreadLocal 中，而是在每个 Thread 的 ThreadLocalMap 里。

ThreadLocalMap Key 为 ThreadLocal<?>，值为 Object，根据调用的 ThreadLocal 对象修改对应的 Object 。因此 ThreadLocal 保存对象本质依然是线程内的局部变量。

至于为何 ThreadLocalMap 中要用 WeakReference 。原因在于，ThreadLocal 对象的回收，不应该被 TheadLocalMap 所影响。一旦某个 ThreadLocal 不再被其他对象引用，那么说明这个 ThreadLocal 对象已经不会再被使用，即使它还存在于 TheadLocalMap 中。

----------

### JVM 部分配置参数解释

`-Xmx10240m` 堆最大值

`-Xms10240m` 堆初始值 *也就是堆最小值*

`-Xmn5120m` 年轻代大小 *Young Gen = Eden + 2\*Survivor*

`-XXSurvivorRatio=3` 表示 Eden 与一个 Survivor 的比值 *Eden = 3\*Survivor*

`-XX:PermSize=64m` 持久代大小

`-XX:NewRatio=4` 年轻代与年老代比值 *Old Gen = 4\*Young Gen*
 
----------

### JVM 堆 (Heap) 组成及 GC 简单过程

Heap 主要分为三个部分：

1. 年轻代 (Young Generation)

2. 年老代 (Old Generation)

3. 永久代 (Permanent Generation) *仅仅存在于 HotSpot 中，存储 Class 的信息、常量、静态变量等数据。物理上，永久代在堆中，逻辑上永久代与堆独立。*

**MinorGC 的简单过程**

年轻代分为一个 Eden 区和两个 Survivor 区 (From, To)，新创建的对象在  Eden 区。

在 Minor GC 开始时， To 区是空的。 Eden 中的存活对象会移动到 From 区，而 From 区的对象会根据对象经历的 GC 次数来决定去向，超过指定阈值的对象会移动到年老代，其他的则移动到 To 区。

移动完毕后，GC 会将 Eden 区和 From 区清空。然后将之前的 To 区指定为 From 区，之前的 From 区被指定为 To 区，至此 Minor GC 完毕。如果在 GC 将 From 移动至 To 区时满了，则将 To 区的所有对象移动到年老代。

**触发 Full GC 的条件**

1. 年老代空间不足。*创建大对象、大数组时，会将对象直接放入年老代，可能导致年老代空间不足。*

2. 永久代空间不足。*此种情况较少出现，永久代空间一般设置为默认的 64MB。JDK8 移除了永久代，变成 Metaspace 。*

3. 年轻代至年老代平均升级大小大于年老代剩余空间。

4. 调用 System.GC() 。

5. CMS 过程中。

----------

### JVM 内存分布

1. 堆 Heap

2. 方法区 Method Area

3. 虚拟机栈 VM Stack

4. 本地方法栈 Native Method Stack

5. 程序计数器 Program Counter

其中 1, 2 线程间共享，3, 4, 5 线程独有。

----------
