###SpringBoot的两种全局配置文件:
1. application.properties
2. application.yml

(其文件名固定)

.yml YAML语言编写的文件(application.properties和application.yml都可以被SpringBoot自动读取)

[YAML学习(入门)](YAMLStudy.md)

---
##SpringBoot配置绑定

配置绑定 意为:将配置文件的值与JavaBean中对应属性进行绑定.

通常将一些配置信息(如 数据库配置)放入配置文件,之后通过Java代码读取配置文件并将指定配置封装到JavaBean(实体类)之中.

+ SpringBoot的两种配置绑定
    - @ConfigurationProperties
    - @Value

* @ConfigurationProperties
    + 见application.yml 和 ***Person.java*** Dog.java
* @Value
    + 见application.yml 和 ***SpecialPerson.java*** Dog.java

+ 两者的异同
    - 同:
        - 都可以读取配置文件中的属性值并绑定到JavaBean中
    - 异
        - 使用位置不同
            - @ConfigurationProperties JavaBean 类名上
            - @Value JavaBean 属性上
        - 功能不同
            - @ConfigurationProperties 批量绑定配置文件中的配置
            - @Value 一个一个的绑定需要绑定的属性
        - 松散绑定支持不同
            - @ConfigurationProperties 支持松散绑定(松散语法)
                - 例:Person 中的 lastName 属性,配置文件中的属性名支持下列写法
                    - Person.lastName
                    - Person.last-name
                    - Person.last_name
                    - PERSON_LAST_NAME
            - @Value 不支持松散绑定
        - SpEL 支持不同
            - @ConfigurationProperties 不支持SpEl表达式
            - @Value 支持SpEL表达式
        - 复杂类型封装
            - @ConfigurationProperties 支持所有类型的封装(例如:Map List Set 以及对象等)
            - @Value 只支持基本数据类型(例如:字符串 布尔值 整数等)
        - 应用场景不同
            - 若只是获得配置文件中某项值 则使用 @Value
            - 若专门编写一个 JavaBean 来配置文件进行映射 则使用 @ConfigurationProperties
            - ***二者没有明显的优劣***

* @PropertySource
    + 将无关SpringBoot的配置写在单独的配置文件中(防止application.properties 或 application.yml 文件过于臃肿而难以维护),并在对应JavaBean上使用@PropertySource注解指向该配置文件
        + 见person.properties 和 FamiliarPerson.java

>***注*** :.properties文件默认编码为ios-5529-1输入中文将自动转码,idea需要在`settings -> editor -> file encodings` 内修改编码

---
##***SpringBoot导入Spring配置***
* 默认情况,SpringBoot不包含任何Spring配置文件(即便是手动添加,也不会识别).但SpringBoot提供了其他方法导入Spring配置
    * @ImportResource注解加载Spring配置文件
    * 使用全注解的方式加载Spring配置

###**@ImportResource**导入Spring配置文件

在主启动类上使用@ImportResource注解可导入一个或多个Spring配置文件,并使其生效

* 步骤(括号内为该项目中的例子)
    1. 创建Service接口(PersonService.java)
    2. 创建Impl类实现Service接口(PersonServiceImpl.java)
    3. 编写Spring配置文件(beans.xml)
    4. 在主启动程序类上使用@ImportResource注解(SpringBootLearnApplication.java)
    5. 测试Spring配置文件是否加载到IOC容器中(SpringBootLearnApplicationTests.java)

>若没有配置主启动程序类的@ImportResource注解,IOC容器中将识别不到Spring配置文件

###**全注解**加载Spring配置

* 实现方式:
    1. 使用 @Configuration 注解定义配置类,替换Spring配置文件
    2. 配置类内部可包含一个或多个被 @Bean 注解的方法(被注解的方法将被 AnnotationConfigApplicationContext 或 AnnotationConfigWebApplicationContext 类扫描,以构建bean的定义[相当于Spring配置文件中的<bean></bean>标签].方法的返回值将会以组件的方式加载到容器中,组件的id即为方法名)

###***总结***

* SpringBoot加载Spring的配置
    * @ImportResource(locations={"path"}) 写在主启动程序类中(需要写好Spring配置文件)
    * 全注解 创建以 @Configuration 注解定义的配置类,其中使用 @Bean 注解定义配置方法(方法名为组件id;方法返回值为组件)(无需再主启动程序类中添加额外的注解;不需要Spring配置文件)

---
##SpringBootProfile(多环境配置)

实际项目中开发时通常都会有多个环境(开发环境 测试环境 生产环境)不同环境配置也不同(开发环境使用开发数据库 测试环境使用测试数据库 生产环境使用线上正式数据库)

Profile为不同环境的不同配置提供支持(可以通过激活 指定参数的方式快速切换环境)

###多Profile文件方式

>***properties文件的优先级高于yml文件(不管是主配置文件还是环境配置文件)***

* 配置文件的命名方式:
    * application-{profile}.properties/yml
        * 其中 profile 指各个环境的名称或简称(例如:dev test prod)
* properties文件配置
    * src/main/resources 下的4个properties配置文件
        1. application.properties 主配置文件
        2. application-dev.properties 开发环境配置文件
        3. application-test.properties 测试环境配置文件
        4. application-prod.properties 生产环境配置文件
    * application.properties 文件中(详见该项目下的application.properties)
        * 指定服务器端口号
        * 通过指令激活指定环境
    * application-dev.properties 文件中(详见该项目下的application-dev.properties)
        * 指定开发环境端口
    * application-test.properties 文件中(详见该项目下的application-test.properties)
        * 指定测试环境端口
    * application-prod.properties 文件中(详见该项目下的application-prod.properties)
        * 指定生产环境端口

>注:激活指定profile后主配置文件中设置的端口不可用,只有激活环境的端口可用[可在SpringBoot启动时的控制台前3条知道端口号]

* yml配置
    * 和properties文件类似
    * src/main/resources 下的4个yml配置文件
        1. application.yml 默认配置
        2. application-dev.yml 开发环境配置
        3. application-test.yml 测试环境配置
        4. application-prod.yml 生产环境配置
    * application.yml 文件中(详见该项目下的application.yml)
        * 指定服务器端口号
        * 通过指令激活指定环境
    * application-dev.yml 文件中(详见该项目下的application-dev.yml)
        * 指定开发环境端口
    * application-test.yml 文件中(详见该项目下的application-test.yml)
        * 指定测试环境端口
    * application-prod.yml 文件中(详见该项目下的application-prod.yml)
        * 指定生产环境端口
>***注***: 若同时配置了properties文件和yml文件(两者配置不同),以properties文件为主(不会报错,而是覆盖yml文件的配置)

* **多Profile文档块模式**
    * 主要是yml文件
    * yml配置文件中,可使用`---`将文件分隔成多个文件块(在不同文档块针对不同环境进行不同配置,并在第一个文档块内对配置进行切换)
>**注**:文档块的优先级低于yml文件(即 同时有文档块和yml文件配置,优先加载yml文件配置)

* 激活Profile
    * 方式
        1. 配置文件中配置激活指定Profile
        2. 命令行激活
        3. 虚拟机参数激活
    * 命令行激活
        * 将SpringBoot打包成jar包(`mvn clean package`)后,在命令行中运行时,可以通过配置命令行参数,激活指定Profile
  >命令行参数`java -jar SpringBootLearn-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev`(注意profiles有s)
    * 优先级高于配置文件
    * 虚拟机参数激活
        * SpringBoot项目运行时,指定虚拟机参数激活指定Profile
        *
        <del>* 命令行参数`java -Dspring.profiles.active=dev -jar SpringBootLearn-0.0.1-SNAPSHOT.jar`
        其中 `-Dspring.profiles.active=dev` 为激活环境Profile的虚拟机参数</del>
        * 存疑

---
---
## SpringBoot 默认配置文件

SpringBoot启动时会将resources目录下的application.properties文件或application.yml文件作为其默认配置文件,但并不意味这SpringBoot项目中只能存在一个application.properties或application.yml文件

###默认配置文件

***SpringBoot项目可以存在多个application.properties或application.yml文件***

* SpringBoot 在启动时会扫描5个位置的 application.properties 文件或 application.yml 文件:
    1. file:./config/
    2. file:./config/*/
    3. file:./
    4. classpath:/config/
    5. classpath:/

> file:当前项目根目录; classpath:当前项目的类路径,即resources目录

以上所有位置的配置文件都将被加载,且优先级依次降低(从上至下依次降低),相同位置的application.properties的优先级高于application.yml
> 所有位置的都将加载,但高优先级的配置将**覆盖**掉低优先级的配置即
>> * 存在相同的配置内容时,高优先级的内容将覆盖低优先级的内容
>> * 存在不同的配置内容时,高优先级和低优先级的配置内容取并集

---
##SpringBoot外部配置文件

* 指定外部配置文件的路径的参数
    * spring.config.location
    * spring.config.addition-location
* spring.config.location
    * jar 命令行参数 `--spring.config.location={外部配置文件全路径}`
> **注**:配置该参数指定配置文件后,会使默认配置文件(application.properties 或 application.yml)失效(是失效,不是覆盖),SpringBoot将只加载外部配置文件.
* spring.config.additional-location
    * jar 命令行参数 `--spring.config.additional-location={外部配置文件全路径}`
> **注**: `--spring.config.location` 和 `--spring.config.additional-location` 两者有一定区别.后者不会使默认配置文件失效,使用该命令行参数添加的外部配置文件和默认的配置文件共同生效,取并集并且外部配置文件优先级最高(高于任何默认配置文件)

Maven 对项目打包时,位于项目根目录的配置文件将无法被打包进项目的jar包中,因此位于根目录下的默认配置文件无法在jar包中生效.即项目将只加载外部配置文件和项目类目录下的默认配置文件,且其优先级为:
1. `spring.config.additional-location` 指定外部配置文件
2. `classpath:/config/application.yml`
3. `classpath:/application.yml`

> 解决根目录下的默认配置文件无法加载的问题:
> * 在IDEA的运行配置(Run/Debug Configuration)中,添加虚拟机参数 `-Dspring.config.additional-location=外部配置文件全路径`.
> * 在IDEA的运行配置(Run/Debug Configuration)中,添加程序运行参数 `-spring.config.additional-location=外部配置文件全路径`.
> * 在主启动类中调用 `System.setProperty()` 方法添加系统属性 `spring.config.additional-location` 指定外部配置文件.

---
## SpringBoot配置加载顺序

* SpringBoot配置优先级(由高到低)
    1. 命令行参数
    2. 来自java:comp/env的JNDI属性
    3. Java系统属性(System.getProperties())
    4. 操作系统环境变量
    5. RandomValuePropertySource 配置的 random.*属性值
    6. 配置文件(yml文件和properties文件)
    7. @Configuration 注解类上的 @PropertySource 指定的配置文件
    8. 通过 SpringApplication.setDefaultProperties 指定的默认属性

> 以上都会加载,但在遇到相同内容时,高优先级的配置将覆盖低优先级的配置;遇到不同内容时,高优先级和低优先级的配置内容取并集,同时生效,形成互补配置

* 命令行参数
    * SpringBoot中所有配置,都可以通过命令行参数进行指定,配置形式:
        * `java -jar {jar包文件名} --{参数1}={参数值1} --{参数2}={参数值2}`
        * 示例:
      ```
      java -jar springbootdemo-0.0.1-SNAPSHOT.jar --server.port=8081 --server.servlet.context-path=/bcb
      ```
        + 参数说明:
            - --server.port: 指定端口
            - --server.servlet.context-path: 指定上下文路径(项目访问路径)

* 配置文件
    * SpringBoot 启动时,将自动加载jar包内部及jar包所在目录指定位置的配置文件(properties文件 yml文件).
      ![配置文件优先级顺序](../learn_Image/SpringBoot加载配置文件优先级顺序.png "优先级")

+ 说明:
    - /project : JAR包所在目录(名称自定义)
    - /childDir : JAR包所在目录下的config目录的子目录(名称自定义)
    - JAR : 项目打包生成的JAR包
    - 其余带 '/' 标识的目录名称不可修改
+ 优先级顺序规则:
    1. 先加载JAR包外的配置文件,再加载JAR包内的配置文件
    2. 先加载config目录下的配置文件,再加载config目录外的配置文件
    3. 先加载config子目录下的配置文件,再加载config目录下的配置文件
    4. 先加载application-{profile}.properties/yml文件,再加载application.properties/yml文件
    5. 先加载.properties文件,再加载.yml文件

---
##SpringBoot自动配置原理

由于SpringBoot自动化配置,因此SpringBoot项目创建完成后,即使不进行任何配置,也可运行

可以再application.properties 或 application.yml中进行对自动配置属性(e.g. server.port logging.level.* spring.config.active.no-profile etc.)的修改,可从
[SpringBoot官方文档](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#application-properties) 查看SpringBoot的自动配置属性(但是,SpringBoot的配置属性数量庞大,仅靠官方文档配置也很麻烦).

* Spring Factories 机制
    * SpringBoot的自动配置基于Spring Factories机制实现
        * Spring Factories机制是SpringBoot中的服务实现机制(和Java SPI机制相似).
      > SpringBoot会自动扫描所有Jar包类路径下的META-INF/spring.factories文件,并读取其中内容,进行实例化.(同时此机制为Spring Boot Starter的基础)
* spring.factories 文件(本质上与properties 文件相似)包含一组或多组键值对,其中:key的取值为接口的完全限定名;value的取值为接口实现类的完全限定名(一个接口可以设置多个实现类,不同实现类用','隔开) 例如
```properties
org.springframework.boot.autoconfigure.AutoConfigurationImportFilter=\org.springframework.boot.autoconfigure.condition.OnBeanCondition,\org.springframework.boot.autoconfigure.condition.OnClassCondition,\org.springframework.boot.autoconfigure.condition.OnWebApplicationCondition
```
> 文件配置内容过长,为方便阅读而手动换行时,可以使用'\'防止内容丢失

* Spring Factories实现原理
    * spring-core包定义了SpringFactoriesLoader类,此类会扫描所有JAR包类路径下的META-INF/spring.factories文件,并获取指定接口与的配置.再SpringFactoriesLoader类中有两个对外的方法:

| 返回值            | 方法                                                                          | 描述                                                |
|:---------------|:----------------------------------------------------------------------------|:--------------------------------------------------|
| `<T> List<T>`  | `loadFactories(Class<T> factoryType, @Nullable ClassLoader classLoader)`    | 静态方法;根据接口获取其实现类的实例;该方法返回的是实现类对象列表(接口->实现类对象列表)    |
| `List<String>` | `loadFactoryNames(Class<?> fcatoryType, @Nullable ClassLoader classLoader)` | 公共静态方法;根据接口获取其实现类的类名;该方法返回的是实现类的类名列表(接口->实现类类名列表) |

> 关键:从**指定ClassLoader**获取**spring.factories文件**,并解析获得**类名列表**

loadFactories() 方法获得 指定接口 的 实现类对象列表
[loadFactories()](../MethodsTXT/loadFactories.txt)

loadFactoryNames() 方法获得 指定接口 的实现类类名列表
[loadFactoryNames()](../MethodsTXT/loadFactoryNames.txt)

loadSpringFactories() 方法读取该项目下 所有JAR包 类路径下的 META-INF/spring.factories 文件的配置内容,并以 Map的形式返回
[loadSpringFactories()](../MethodsTXT/loadSpringFactories.txt)

* 自动配置的加载
    * SpringBoot自动化配置也基于 Spring Factories 机制实现.
        * spring-boot-autoconfigure-xxx.jar 类路径下的 META-INF/spring.factories 中设置了SpringBoot的自动配置内容(仅为该文件的一块)
          [spring.factories(autoConfiguration Part)](../MethodsTXT/spring_factories_autoConfogurePart.txt)
          其中,value的取值由多个 xxxAutoConfiguration (逗号分隔) 组成,每个 xxxAutoConfiguration 都是一个自动配置类.
> Spring Boot 启动时,将利用Spring-Factories的机制,将 xxxAutoConfiguration 实例化,并作为组件加入到容器中,以实现 Spring Boot 的自动配置.

* @SpringBootApplication 注解
    * 所有SpringBoot项目的主程序启动类都使用了一个 @SpringBootApplication 注解(最重要之一,且是实现SpringBoot自动化配置的关键)

+ @SpringBootApplication 组合元注解,主要包含:
    - @SpringBootConfiguration
    - @EnableAutoConfiguration

> @EnableAutoConfiguration 为 SpringBoot 自动化配置关键

* @EnableAutoConfiguration 注解
    * 开启SpringBoot的自动配置功能
    * 使用 Spring 框架提供的 @Import 注解通过 AutoConfigurationImportSelector 类(选择器) 给容器导入自动配置组件 `@Import({AutoConfigurationImportSelector.class})`

* AutoConfigurationImportSelector 类
    * 实现了 DeferredImportSelector 接口
    * 还包括 静态内部类 AutoConfigurationGroup (实现了 DeferredImportSelector 接口和 Group 内部接口)

|           返回值            |                                              方法声明                                               |                 描述                  | 是否为内部类方法 |          内部类           |
|:------------------------:|:-----------------------------------------------------------------------------------------------:|:-----------------------------------:|:--------:|:----------------------:|
| `Class<? extends Group>` |                                       `getImportGroup()`                                        |     该方法**获取**实现了Group接口的类,并实例化      |    否     |
|          `void`          | `process(AnnotationMetadata annotationMetadata, DeferredImportSelector deferredImportSelector)` |             用于引入自动配置的集合             |    是     | AutoConfigurationGroup |
|    `Iterable<Entry>`     |                                        `selectImports()`                                        | 遍历自动配置类的集合(Entry类型的集合),并逐个解析集合中的配置类 |    是     | AutoConfigurationGroup |

AutoConfigurationImportSelector 内部方法执行顺序:
1. getImportGroup()
2. process()
3. selectImports()

调用过程介绍:
1. getImportGroup()
    * 主要用于**获取**实现了 DeferredImportSelector.Group 接口的类
        * [getImportGroup()](../MethodsTXT/getImportGroup.txt)
2. process()
    * 静态内部类 AutoConfigurationGroup 的核心方法
    * 通过调用 getAutoConfigurationEntry() 方法读取 spring.factories 文件中的内容,获取自动配置类的集合
        * [process()](../MethodsTXT/process.txt)
        * getAutoConfigurationEntry() 通过调用 getCandidateConfiguration() 来获取自动配置类的完全限定类名,并通过排除 过滤等处理后,才将其加入成员变量之中.
            * [getAutoConfigurationEntry()](../MethodsTXT/getAutoConfigurationEntry.txt)
            * 在 getCandidateConfigurations() 方法中,根据 Spring Factories 机制调用 SpringFactoriesLoader 的 loadFactoryNames() 方法,根据 EnableAutoConfiguration.class(自动配置接口) 获取其实现类(自动配置类) 的类名集合.
          > List&lt;String&gt; = SpringFactoriesLoader.loadFactorNames(EnableAutoConfiguration.class, BeanClassLoader)
            * `protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAtttributes attributes);`
3. selectImports()
    * 执行完以上命令后 AutoConfigurationImportSelector.AutoConfigurationGroup#selectImports() 将过滤 排除 最后添加 process() 获取的自动配置类到容器之中.
        * [selectImports()](../MethodsTXT/selectImports.txt)

* 自动配置的生效和修改
    * spring.factories 文件中的所有自动配置类 (以 AutoConfiguration 结尾的) 都是在一定条件下才会作为组件加入到容器中的(加入容器配置的内容才会生效).
        * 限制条件在 SpringBoot 中以 @Conditional 派生注解的形式体现 (以 Conditional 开头的注解)

|               注解                |              生效条件               |
|:-------------------------------:|:-------------------------------:|
|       @ConditionalOnJava        |         应用使用指定Java版本时生效         |
|       @ConditionalOnBean        |         容器中存在指定Bean时生效          |
|    @ConditionalOnMissingBean    |         容器中不存在指定Bean时生效         |
|    @ConditionalOnExpression     |         满足指定SpEL表达式时生效          |
|       @ConditionalOnClass       |            存在指定类时生效             |
|   @ConditionalOnMissingClass    |            不存在指定类时生效            |
|  @ConditionalOnSingleCandidate  | 容器中只存在一个指定Bean或指定Bean为首选Bean时生效 |
|     @ConditionalOnProperty      |         系统中指定属性存在指定值时生效         |
|     @ConditionalOnResource      |         类路径下存在指定资源文件时生效         |
|  @ConditionalOnWebApplication   |          当前应用时Web应用时生效          |
| @ConditionalOnNotWebApplication |         当前应用不为Web应用时生效          |

* ServletWebServerFactoryAutoConfiguration (Spring Boot 自动配置生效实例)
    * [源码](../MethodsTXT/ServletWebServerFactoryAutoConfiguration.txt)
    * ServletWebServerFactoryAutoConfiguration 使用了一个 @EnableConfigurationProperties 注解导入了一个 ServerProperties 类 [ServerProperties](../MethodsTXT/ServerProperties.txt)
    * ServletWebServerFactoryAutoConfiguration 使用 @EnableConfigurationProperties 导入 ServerProperties 类, 而 ServerProperties 类又使用 @ConfigurationProperties 注解
  > @ConfigurationProperties 将类的所有属性和配置文件相关配置绑定(以便于获取或修改配置)
  >   功能由容器提供(被它注解的类必须是容器中的一个组件,否则功能不能生效)
  >> @EnableConfigurationProperties 将指定类以组件的形式注入IOC容器,并开启 @ConfigurationProperties的功能
  > 因此,需要 组合使用 @EnableConfigurationProperties + @ConfigurationProperties 以便 xxxProperties 类实现配置绑定

  ***自动配置类 xxxAutoConfiguration*** 负责使用 ***xxxProperties*** 中的属性进行自动配置. ***xxxProperties*** 负责将自动配置属性与配置文件的相关配置进行绑定(以便通过配置文件对默认配置进行修改).
  > 真正"限制"我们可以在配置文件中配置哪些属性的类就是这些 xxxProperties 类,它与配置文件中定义的 prefix 关键字开头的一组属性是唯一对应的.
  > ***注***: AutoConfiguration 与 Properties 并非一一对应, 大多数为多对多关系(1个 AutoConfiguration 同时使用 多个 Properties 中的属性; 1个 Properties 中的属性可被多个 AutoConfiguration 使用)
---
---