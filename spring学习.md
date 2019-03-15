# Spring 相关

### Spring MyBatis

* 实体类(Entity Class)：对应数据库中的表，类型与字段名按规定转换。
* 访问接口(DAO Interface)：定义访问数据的方法，与 XML 文件对应。
* 映射文件(Mapper XML)：提供 SQL 语句，实现访问接口。

----------

### Spring Hibernate

Bean 定义大致与 MyBatis 类似。由 `dataSource` 定义 `sessionFactory`，然后定义出 `transactionManager`。

#### Hibernate 对象的三种状态

- 瞬时状态(transient) 对象刚刚创建的状态，不在 session 缓存中，也不予 session 实例关联，数据库中没有对应的记录。*OID 为 null。*

- 持久化状态(persistent) 在 session 缓存中，并与 session 实例关联，在数据库中有对应记录。在清理 session 缓存时，会根据持久化对象的属性变化更新数据库。*session.commit() 和 session.flush() 会清理缓存。*

- 游离状态(detached) 数据库中有记录，session 中没有缓存。*可以用 update() 关联游离对象，使之变为持久化状态，OID 不为 null。*

![](https://i.imgur.com/eapBXFp.jpg)

示例：

[Spring Hibernate 注解方式示例](https://blog.csdn.net/m0_37914211/article/details/80977920)

[Spring Hibernate 配置文件方式示例](https://www.cnblogs.com/juaner767/p/5597009.html)

[hibernate中对象的3种状态：瞬时态(Transient)、 持久态(Persistent)、脱管态(Detached)](https://www.cnblogs.com/goloving/p/8268311.html)

----------

### Spring 相关配置文件介绍

* **web.xml (.../WEB-INF/web.xml)**

	* 定义前端控制器(servlet)及映射(servlet-mapping)。
	* 指明 applicationContext.xml 文件地址。声明监听器 `<listener>` ContextLoaderListener，让其在 Web 容器加载时自动装载 applicationContext 的配置信息。
	* 各种过滤器(filter)及过滤器映射(filter-mapping)，例如 CharacterEncodingFilter 设置网页编码。
	* 其他固定页面，如欢迎页(welcome-file-list)、错误页(error-page)。

[应用上下文 webApplicationContext](https://www.cnblogs.com/brolanda/p/4265597.html)

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
	* 事务配置(`<tx:advice id="txAdvice" transaction-manager="transactionManager">`)配置各种方法的事务属性，例如：`<tx:method name="select*" propagation="SUPPORTS" read-only="true"/>`。
	* 配置 AOP 切面(aop:config)例如：`<aop:advisor advice-ref="txAdvice" pointcut="execution(* com.xx.impl.*.*(..))"/>` 当然也可以启用注解方式的切面 `<aop:aspectj-autoproxy proxy-target-class="true"/>`。[AspectJ 切入点表达式](https://blog.csdn.net/lk7688535/article/details/51989746)
	* 注解方式的事务配置(tx:annotation-driven)，很常见的事务配置方式，Spring Boot 的默认方式。
	* 定义 service Bean
	* 引入其他配置文件，例如：`<import resource="classpath:applicationContext-amq.xml" />`。

----------

### Spring ServletContext 加载流程

1. WEB 容器读取 web.xml 配置文件，将 `<context-param>` 节点中以键值对形式保存在 ServletContext 中，然后读取 `<listener>` 节点并创建监听。
2. 监听容器启动和关闭的监听器都实现了 `ServletContextListener` 接口，接口中有两个方法 `contextInitialized(ServletContextEvent event)` 和 `contextDestroyed(ServletContextEvent event)` 表示 SelvetContext 生命周期中的启动和终止。监听器通过 `event` 可以获取到之前保存的键值对，然后进行初始化。
3. 以 Spring 为例，Spring 中的监听器 `ContextLoaderListener` 实现了 `ServletContextListener` 接口，监听器在初始化过程创建 `WebApplicationContext`(WAC)也就是 IoC 容器。WAC 会读取 web.xml 中 `contextConfigLocation` 的值并导入配置，于是 WAC 开始 Bean 的解析、加载、注入等等流程。
4. `<listener>` 节点加载完毕后会继续加载 `<servlet>` 节点，加载并实例化各个 servlet。并将这些 servlet context 的 parentContext 设置为 WAC。因此，可以在 MVC 上下文中获取到 WAC 的上下文数据。

参考文章：

[Spring-MVC理解之一：应用上下文webApplicationContext](https://www.cnblogs.com/brolanda/p/4265597.html)

[Spring 初始化 ContextLoaderListener 与 DispatcherServlet](https://blog.csdn.net/pange1991/article/details/81282823)

[SpringMVC加载WebApplicationContext源码分析](https://blessht.iteye.com/blog/2121845)

----------

### 事务隔离级别及 Spring 事务传播

**事务四特性 ACID**

- **A**tomicity 	原子性
- **C**onsistency 一致性
- **I**solation 隔离性
- **D**urability 	持久性

**隔离级别**

1. **Read Uncommitted** A 事务未提交的修改，B 事务能够看到。导致 A 回滚后，B 事务读到“脏数据”即**脏读**。

2. **Read Committed**  A 事务提交的修改被 B 看到，B 事务在 A 提交之前和之后读取两次，两次数据不一致，导致**不可重复读**。

3. **Repeatable Read** A 事务插入或删除一条数据并提交，B 事务在 A 前后分别读取一个范围的数据，发现两次结果不一致，导致**幻读**。

4. **Serializable** 串行事务，极少用到，性能很差。

**Spring 事务传播**

- **PROPAGATION_REQUIRED** 如果当前没有事务，就新建一个事务，如果已经存在一个事务中，加入到这个事务中。这是 最常见的选择。

- **PROPAGATION_REQUIRES_NEW** 新建事务，如果当前存在事务，把当前事务挂起。

- **PROPAGATION_SUPPORTS** 支持当前事务，如果当前没有事务，就以非事务方式执行。

- **PROPAGATION_NOT_SUPPORTED** 以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。

- **PROPAGATION_MANDATORY** 使用当前的事务，如果当前没有事务，就抛出异常。

- **PROPAGATION_NEVER** 以非事务方式执行，如果当前存在事务，则抛出异常。

- **PROPAGATION_NESTED** 如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则执行与PROPAGATION\_REQUIRED 类似的操作。

----------

### Spring 切面优先级设置

如果一个方法有多个切面对其进行增强，怎么定义切面执行的顺序呢？

答：使用 `@Order(1)` 注解，定义切面的执行顺序。执行顺序为递增。定义事务 order
`<tx:annotation-driven transaction-manager="txManager"  order="0"/>` 在这个例子中，事务切面会在自定义切面之前运行，如果抛出异常，全部都会回滚。

----------

### Spring 切面对象内部调用不拦截的解决办法

这个问题比较常见也是比较严重，因为 Spring 中的事务也是基于切面的。这个问题会导致事务传播出现异常。

1. 避免内部调用。*这不是个严格意义上的解决办法。*

2. `<aop:aspectj-autoproxy expose-proxy="true" />` 然后将内部调用改为 `((XxService)AopContext.currentProxy()).innertMethod()`。这样，内部调用也会通过代理对象运行，拦截并实现切面功能。*凡是需要拦截的方法必须是 `public` 修饰。*

参考资料：

[spring 的aop proxy 代理](https://www.cnblogs.com/hanxue53/p/5280099.html)

[SpringAOP嵌套调用的解决办法](https://fyting.iteye.com/blog/109236)

----------

### Spring Bean 作用域(Scope)及 proxyMode

Spring bean 有四种常见默认作用域：

- 单例（singleton）*整个应用中只有一个对象。*
- 原型（prototype）*每一次注入和创建时都会新建一个对象。*
- 会话（session）*每个 session 对应一个对象。*
- 请求（request）*每个请求对应一个对象。*

还有一种不常见的全局会话（global session）*与 Portlet 应用有关*。

proxyMode 代理模式的用法，举个例子：如果在单例 Bean 中使用 session Bean 那么 session Bean 必须启用代理模式。因为单例 Bean 需要使用多个 session Bean，那么应该用代理类对象而不是实际的对象。

INTERFACE(JDK 代理) 与 TARGET_CLASS(CGLIB) 区别在于 INTERFACE 针对类的接口生成代理，TARGET_CLASS 针对类实现代理 。**这两个方法也是 Spring AOP 采用的两种动态代理方式。**

**注意**：如果使用 `session`, `request`, `global session` 这三种作用域，一定要在 web.xml 引入 RequestContextListener 监听器，否则会报错。这个监听器用于通知 IoC 容器请求进入。

----------

### Spring BeanFactory 与 FactoryBean

TODO

[Spring BeanFactory与FactoryBean的区别及其各自的详细介绍于用法](https://www.cnblogs.com/redcool/p/6413461.html)

----------

### Spring Data JPA

在实体类中，使用 `@Entity` 、 `@Table` 、`@Id`、`@Column` 等注解。

在 Repository 类中，定义接口：`public interface XxRepository extends JpaRepository<T, KeyType>`，其中 T 是表对应的类， KeyType 是表的主键类型。具体的方法名称有相应的规则来进行定义。例如：[Spring Data JPA 简单查询--方法定义规则](https://www.cnblogs.com/rulian/p/6434631.html)

----------

### Spring 依赖注入的三种方式

1. **构造器注入** 能够保证注入的对象一定是正确的；对象创建完毕后立即可以使用；脱离 IoC 框架也能正常使用；如果依赖多，构造函数会很大。

2. **Field 注入** 短小精悍；脱离 IoC 框架无法使用。

3. **setter 方法注入** 非常灵活，可以在运行时改变依赖；set 依赖之前对象无法使用。

----------

### Spring 框架中 Bean 的生命周期

Bean 自身主要有两个方法 init-method(@PostConstruct) 和 destroy-method(@PreDestroy)。

[Spring Bean的生命周期（非常详细）](https://www.cnblogs.com/zrtqsk/p/3735273.html)

----------

### Spring 中常用的注解

- `@Autowire` 用于标注需要注入依赖的变量和方法。
- `@Qualifier` 区分同类型的 Bean，指明 Bean 的名称。
- `@Component` 一些 Bean 注解的统称，官方不推荐。
- `@Controller` 控制器，负责处理 DispatcherServlet 分发的请求。
- `@Service` 通常用于 service 层。
- `@Repository` 通常用于 DAO 层。
- `@Scope` 声明 Bean 的作用域，例如 `@Scope(value="session", proxyMode=...)`。
- `@SessionAttributes` 将 ModelMap 中的属性放入 session 中。
- `@ModelAttribute` 配合 `@SessionAttributes`  使用。
- `@PathVariable` 获取路径中的变量。

----------

### RabbitMQ 与 Spring 

**RabbitMQ 四种 Exchange 模式**：

- **Fanout Exchange** 发送至 Fanout Exchange 的消息会被转发到这个 Exchange 绑定的所有队列(Queue)中。需要注意，每个 Consumer 注册到 Fanout Exchange 时都会新建一个随机名称的队列。

- **Direct Exchange** 消息被转发到路由键(Routine Key)指定的队列中，路由键必须完全相同。

- **Topic Exchange** 路由键中含有通配符 `*` 和 `#`，星号表示匹配一个单词，井号表示匹配多个单词，单词之间用点分隔。参考下图：
![](https://i.imgur.com/wIIA2wd.png)

- **Header Exchange** 消息根据 header 来匹配队列，header 保存了一系列键值对 `<key, value>`，其中有个特殊的 key `x-match`  有两个值 any 与 all。any 表示只要有一个键值对匹配即可，all 表示需要匹配所有的键值对。

**Spring 中使用 RabbitMQ**

1. `rabbit:connection-factory` 定义工厂的 id, host, port, username, password, virtual-host 等等。

2. `rabbit:admin` 这个具体功能不是很清楚，不过与 Exchange 和 Queue 的创建有关。 //TODO

3. `rabbit:queue` 定义一个 Queue。注意，如果是 Fanout 模式并且需要动态订阅，则只能定义 id，不定义 name，因为在 Fanout 模式中，Queue 名称是自动生成的。

4. `rabbit:*-exchange` 这是定义具体的 Exchange，消费者会在 Exchange 中绑定不同的 Queue。

5. `rabbit:listener-container` 监听器容器也就是消费者容器，要指定 connectionFactory。

6. `rabbit:template` 定义生产者，包含 id, connection-factory, exchange, message-converter 等属性。

**注意**：
1. 按 AMQP 标准，生产者只与 Exchange 通信，而数据会传递到哪一个具体的 Queue 这是 Exchange 的职责。同样地，消费者只与 Queue 通信，消息来自哪个 Exchange 这与自己没有关系。

2. Queue 的定义及 Queue 与 Exchange 的绑定是由消费者或者生产者来实现，Exchange 的定义由生产者实现。

3. 如果 Queue 没有显式绑定 Exchange 则绑定默认的名字为空的 Exchange。

**持久化**：

- Message 持久化：开启后，未发送的消息会在服务重启后依然存在。必须与 Queue 持久化一起开启才有意义。

- Queue 持久化：关闭后，Queue 会在服务重启后消失。

- Exchange 持久化：关闭后，Exchange 会消失。

	即使全部开启持久化也不能保证消息不丢失，必须关闭消费者 autoACK，当消费者成功处理完后再手动 ACK，这样才能让消息不丢失地传递。

**消息确认机制**：

生产者向 Broker 发送消息时没有反馈，如果消息发送失败，生产者也无法知晓。为了解决这个问题，引入生产者消息确认机制。RabbitMQ 有两种生产者消息确认机制：

1. AMQP 事务 

	基于 AMQP 的协议事务，通过 channel.txSelect(), ch.txCommit(), ch.txRollback() 三个方法来实现。用法与 DB 事务类似。

2. channel 的 Confirm 模式 

	性能较好，消息会被分配一个唯一的 ID，消息被 Broker 接收后，Broker 会向生产者发送确认。Confirm 模式又分为三种方式：

	1. 普通confirm模式：每发送一条消息后，调用waitForConfirms()方法，等待服务器端confirm。实际上是一种串行confirm了。
	2. 批量confirm模式：每发送一批消息后，调用waitForConfirms()方法，等待服务器端confirm。一批中有一个失败，则重发这批所有消息。
	3. 异步confirm模式：提供一个回调方法，服务端confirm了一条或者多条消息后Client端会回调这个方法。

同样地，Broker 把消息发送给消费者后，不知道消息是否被接收、处理。如果消息发送后，消费者重启了，消息就有可能丢失。因此，消费者也有消息确认机制。消费者的确认机制较为简单，通过手动发送 ACK 来反馈消息被正常接收处理。

相关资料：

[RabbitMQ中 exchange、route、queue的关系](https://www.cnblogs.com/linkenpark/p/5393666.html)

[Rabbit之消息持久化](https://blog.csdn.net/u013256816/article/details/60875666)

[RabbitMQ之消息确认机制（事务+Confirm）](https://blog.csdn.net/u013256816/article/details/55515234)

[（微服务）分布式事务-最大努力交付 && 消息最终一致性方案](https://segmentfault.com/a/1190000011479826)

----------