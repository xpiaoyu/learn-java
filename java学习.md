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

*这其实是错误的。即使用了 TheadLocal ，每一个线程依然需要创建自己的 Connection 而不是直接使用唯一公有 Connection 对象的拷贝。因为使用了拷贝依然存在多线程不安全的问题。*

Thread 类中有一个 threadLocals 成员，变量类型是 ThreadLocalMap。这是每个 Thread 都有的，所以数据 Map 并不是在 ThreadLocal 中，而是在每个 Thread 的 ThreadLocalMap 里。

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

### Spring MyBatis

* 实体类(Entity Class)：对应数据库中的表，类型与字段名按规定转换。
* 访问接口(DAO Interface)：定义访问数据的方法，与 XML 文件对应。
* 映射文件(Mapper XML)：提供 SQL 语句，实现访问接口。

----------

### Spring 有关配置文件介绍

* **web.xml (.../WEB-INF/web.xml)**

	* 定义前端控制器(servlet)及映射(servlet-mapping)。
	* 指明 applicationContext.xml 文件地址。声明 ContextLoaderListener，让其在 Web 容器加载时自动装载 applicationContext 的配置信息。
	* 各种过滤器(filter)及过滤器映射(filter-mapping)，例如 CharacterEncodingFilter 设置网页编码。
	* 其他固定页面，如欢迎页(welcome-file-list)、错误页(error-page)。

* **dispatcher-servlet.xml (SpringMVC 配置文件)**

	* 视图解析器(InternalResourceViewResolver)用于检索视图文件，例如 controller 中返回的某个视图逻辑名为 Hello 根据视图解析器中配置的 prefix=/WEB-INF/views/ 和 suffix=.jsp，Spring 会解析 .../WEB-INF/views/Hello.jsp 这个视图。
	* 自动扫描方式(context:component-scan)扫描指定包下的类寻找含有注解的类，SpringMVC 通常只扫描 controller 包。
	* 单个 controller 配置，例如：`<bean name="/hello.action" class="com.my.project.controller.HelloController"/>`
	* 配置处理器映射器及适配器 `<mvc:annotation-driven/>` 相当于注册了 RequestMappingHandlerMapping 和 RequestMappingHandlerAdapter 两个 Bean 用于 controller 请求的分发。
	* 拦截器(mvc:interceptors)拦截自定义路径的请求，拦截器(Interceptor)继承 HandlerInterceptorAdapter 类或者实现 HandlerInterceptor 接口，一共有三个方法 preHandle, postHandle, afterCompletion。
		* preHandle 用于预处理请求，返回 true 表示继续流程，返回 false 直接中断流程不会再继续执行其他拦截器或处理器。
		* postHandle 在处理器后，渲染视图前执行。
		* afterCompletion 请求处理完毕后执行，只有 preHandle 返回 true 的拦截器，才会执行 afterCompletion 方法。
	* 全局异常处理器(exceptionResolver)不同于 @ControllerAdvice 注解形式的全局异常处理器，Resolver 通常与视图有关，如果是 Restful 风格 API 建议采用注解形式。
	* 文件上传解析器(multipartResolver)处理文件上传，设置相关参数。
	* 静态资源处理(mvc:resources)用于映射静态资源文件，例如：`<mvc:resources mapping="/js/**" location="/js/" />` 也可以使用 `<mvc:default-servlet-handler/>` 表示没有映射到 controller 的请求使用默认设置，即 web.xml 中的配置。

* **applicationContext.xml (Spring 配置文件)**

	* 资源文件配置(context:property-placeholder)引入配置文件，在类对象成员变量上添加注解引入配置的值，例如：`@Value("${jdbc.url}")`。需要注意的是，如果要在 controller 中使用 Value 注解，则必须在 SpringMVC 配置文件中也加入这条语句。
	* 数据源 Bean 定义例如：`<bean id="dataSource" class="...">...</...>`。有时候数据源会利用注解类形式定义。
	* sqlSessionFactory Bean 配置 MyBatis。
	* Mapper 扫描器 Bean(MapperScannerConfigurer)，配合 sqlSessionFactory 使用。
	* transactionManager Bean 配置数据源的事务控制器。
	* 事务配置(tx:advice)配置各种方法的事务属性，例如：`<tx:method name="select*" propagation="SUPPORTS" read-only="true"/>`。
	* 配置 AOP 切面(aop:config)例如：`<aop:advisor advice-ref="txAdvice" pointcut="execution(* com.xx.impl.*.*(..))"/>`。
	* 注解方式的事务配置(tx:annotation-driven)，很常见的事务配置方式，Spring Boot 的默认方式。
	* 定义 service Bean

----------

### 事务隔离级别及 Spring 事务传播

**隔离级别**

1. **Read Uncommitted** A 事务未提交的修改，B 事务能够看到。导致 A 回滚后，B 事务读到“脏数据”即**脏读**。

2. **Read Committed**  A 事务提交的修改被 B 看到，B 事务在 A 提交之前和之后读取两次，两次数据不一致，导致**不可重复读**。

3. **Repeatable Read** A 事务插入或删除一条数据并提交，B 事务在 A 前后分别读取一个范围的数据，发现两次结果不一致，导致**幻读**。

4. **Serializable** 串行事务，极少用到，性能很差。

**Spring 事务传播**

- PROPAGATION_REQUIRED 如果当前没有事务，就新建一个事务，如果已经存在一个事务中，加入到这个事务中。这是 最常见的选择。

- PROPAGATION_SUPPORTS 支持当前事务，如果当前没有事务，就以非事务方式执行。

- PROPAGATION_MANDATORY 使用当前的事务，如果当前没有事务，就抛出异常。

- PROPAGATION_REQUIRES_NEW 新建事务，如果当前存在事务，把当前事务挂起。

- PROPAGATION_NOT_SUPPORTED 以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。

- PROPAGATION_NEVER 以非事务方式执行，如果当前存在事务，则抛出异常。

- PROPAGATION_NESTED 	如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则执行与 PROPAGATION_REQUIRED 类似的操作。

----------

### JPA

----------
