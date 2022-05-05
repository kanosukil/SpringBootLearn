# SpringBoot 访问 JDBC 数据库
* 导入JDBC场景启动器(starter)
    * 引入依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jdbc</artifactId>
</dependency>
```
默认引入 HikariCP 数据源
* 导入驱动
    * maven配置
* 配置数据源
    * application.properties/yml中配置数据源
    * 见[application.properties](../src/main/resources/application.properties)
* 测试
    * Spring Boot 提供了一个名为 JdbcTemplate 的轻量级数据访问工具,它是对 JDBC 的封装.Spring Boot 对 JdbcTemplate 提供了默认自动配置,我们可以直接使用 @Autowired 或构造函数将它注入到 bean 中使用.
    * [SpringBootJdbcApplicationTests.java](../src/test/java/com/learnboot/springbootlearn/SpringBootJdbcApplicationTests.java)
---
### SpringBoot 数据源配置原理
* DataSourceAutoConfiguration
    * 5个内部静态类
        * EmbeddedDatabaseCondition
        * PooledDataSourceAvailableCondition
        * PooledDataSourceCondition
        * PooledDataSourceConfiguration (池化数据源自动配置类)
        * EmbeddedDatabaseConfiguration (内嵌数据源自动配置类)
    * EmbeddedDatabaseConfiguration
        * 内嵌式数据源的自动配置类
        * 没有方法实现该类,主要功能是都通过 @Import 注解 `@Import({EmbeddedDataSourceConfiguration.class})` 注入 EmbeddedDataSourceConfiguration 类实现
        * 向容器中添加SpringBoot内嵌数据源(支持 HSQL H2 DERBY数据库)
        * 功能:向容器添加一个内嵌的数据源(DataSource)(有条件限制)
        * 该类使用了 @Conditional 注解 `@Conditional({DataSourceAutoConfiguration.EmbeddedDatabaseCondition.class})` 使用DataSourceAutoConfiguration的内部限制条件类EmbeddedDatabaseCondition 进行条件判断
            * EmbeddedDatabaseCondition主要检测容器中是否存在池化数据源
                * 若存在,EmbeddedDatabaseConfiguration 不能被实例化
                * 不存在时,EmbeddedDatabaseConfiguration 才能被实例化(才能向容器中添加内嵌数据源)
    * PooledDataSourceConfiguraion
        * 池化数据源的自动配置类
        * 该类上使用了一个 @Conditional 注解 `@Conditional({DataSourceAutoConfiguration.PooledDataSourceCondition.class})`,该注解使用了 DataSourceAutoConfiguration 的内部限制条件类 PooledDataSourceCondition 来进行条件判断.
            * PooledDataSourceCondition 检测容器中是否存在池化数据源
                * 存在 被实例化 可向容器中添加池化数据源
        * 没有方法实现该类,主要功能是都通过 @Import 注解 `@Import({Hikari.class, Tomcat.class, Dbcp2.class, OracleUcp.class, Generic.class, DataSourceJmxConfiguration.class})` 注入其他类(Hikari Tomcat Dbcp2 OracleUcp Generic 五个数据源配置类[都为DataSourceConfiguration 内部类,功能相似(向容器中添加指定数据源)])实现.
            * 以 [Hikari](../MethodsTXT/Hikari.txt) 为例:
                * 主要使用的注解:
                    * @Configuration
                    * @ConditionalOnMissingBean({DataSource.class}) 没有自定义的数据源时生效
                    * @ConditionalOnClass({HikariDataSource.class}) 类路径下存在HikariDataSource类时,Hikari 才会实例化.
                  > 而 HikariDataSource 类是由 spring- boot-starter-jdbc 默认将其引入的,因此只要我们在 pom.xml 中引入了该 starter. Hikari 就会被实例化（这也是 Spring Boot 2.x 默认使用 HikariCP 作为其数据源的原因）
                    * @ConditionalOnProperty(name = {"spring.datasource.type"},havingValue="com.zaxxer.hikari.HikariDataSource",matchIfMissing=true) 当前Springboot配置文件中,配置了spring.datasource.type=com.zaxxer.hikari.HikariDataSource(明确指出使用Hikari数据源)或不配置spring.datasource.type(默认情况)时,Hikari才会实例化.
                * Hikari 类通过 @Bean 注解向容器中添加了 HikariDataSource 组件
              > Hikari类 使用 DataSourceConfiguration 的 createDataSource() 方法获得实例对象\
              而在 createDataSource() 方法中,调用 DataSourceProperties 的 initializeDataSourceBuilder() 来初始化 DataSourceBuilder\
              initializeDataSourceBuilder() 方法通过调用 DataSourceBuilder 的 create() 方法创建 DataSourceBuilder 对象,并根据SpringBoot配置文件中的配置,依次设置 数据源类型 驱动名 链接url 用户名和密码等信息.
* 上面提到 spring.datasource.type 默认是可以不用配置的，因此在 createDataSource() 方法在获取到回传回来的 DataSourceBuilder 对象后，还需要将其 type 属性再次设置为 HikariDataSource，并调用 DataSourceBuilder 的 build() 方法，完成 HikariDataSource 的初始化
* **总结**
    * 在用户没有配置数据源的情况，若容器中存在 HikariDataSource 类，则 Spring Boot 就会自动实例化 Hikari，并将其作为其数据源。
    * Spring Boot 的 JDBC 场景启动器（spring-boot-starter-data-jdbc）通过 spring- boot-starter-jdbc 默认引入了 HikariCP 数据源（包含 HikariDataSource 类），因此 Spring Boot 默认使用 HikariCP 作为其数据源。
---
### Spring Boot整合Druid数据源
* Druid 是阿里巴巴推出的一款开源的高性能数据源产品，Druid 支持所有 JDBC 兼容的数据库，包括 Oracle、MySQL、SQL Server 和 H2 等等。Druid 不仅结合了 C3P0、DBCP 和 PROXOOL 等数据源产品的优点，同时还加入了强大的监控功能。通过 Druid 的监控功能，可以实时观察数据库连接池和 SQL 的运行情况，帮助用户及时排查出系统中存在的问题。
* Druid 非 Springboot 提供的技术, 其属于第三方,需要通过一定方法整合:
    * 自定义整合 Druid
    * 通过 starter 整和 Druid
* 自定义整合 Druid
    * 指:根据 [Druid官方文档](https://github.com/alibaba/druid/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98) 和自身需求,手动创建Druid数据源的方式,将Druid整合到SpringBoot中
    * 步骤:
        1. 引入 Druid 依赖
            * Druid 0.1.18 之后版本都已发布到 Maven 中央仓库中
            * pom.xml依赖:
           ```xml
            <dependencise>
                <!--导入 JDBC 场景启动器-->
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-data-jdbc</artifactId>
                </dependency>
                <!--导入数据库驱动-->
                <dependency>
                    <groupId>mysql</groupId>
                    <artifactId>mysql-connector-java</artifactId>
                    <scope>runtime</scope>
                </dependency>
                <!--采用自定义方式整合 druid 数据源-->
                <!--自定义整合需要编写一个与之相关的配置类-->
                <dependency>
                    <groupId>com.alibaba</groupId>
                    <artifactId>druid</artifactId>
                 <version>1.2.6</version>
                </dependency>
            </dependencise>
           ```
        2. 创建数据源
            * 向容器中添加Druid数据源类(DruidDataSource 继承自Datasource),让Springboot 使用 Druid 作为数据源,不再使用HikariCP
            * 见 [TestDataSourceConfig.java](../src/main/java/com/learnboot/springbootlearn/config/TestDataSourceConfig.java)
           > 配置类创建Druid数据源对象时,应尽量避免数据源信息(url username password等)硬编码到代码中,应使用 @ConfigurationProperties("spring.datasource")注解,将数据源对象属性与配置文件的以 "spring.datasource" 开头的配置绑定
        3. 开启 Druid 内置监控页面
            * Druid 内置 StatViewServlet 的 Servlet(开启 Druid 内置的监控页面功能, 展示 Druid 的统计信息)
                * 用途
                    1. 提供监控信息展示的 html 页面
                    2. 提供监控信息的 JSON API
                  > 使用 StatViewServlet 建议 Druid 0.2.6 及以上
                * StatViewServlet 是一个标准的 javax.servlet.http.HttpServlet, 要开启 Druid 的内置的监控页面,需要将 Servlet 配置在 Web 应用中的 WEB-INF/web.xml 中.
                 ```xml
                 <servlet>
                     <servlet-name>DruidStatView</servlet-name>
                     <servlet-class>com.alibaba.druid.support.http.StatViewServlet</servlet-class>
                 </servlet>
                 <servlet-mapping>
                     <servlet-name>DruidStatView</servlet-name>
                     <url-pattern>/druid/*</url-pattern>
                 </servlet-mapping>
                 ```
                 * SpringBoot 项目中没有 WEB-INF/web.xml ,因此需要在配置类中,通过 ServletRegistrationBean 将 StatViewServlet 注册到容器中开启 Druid 内置监控页面.
                 * 启动 [StatViewServlet](../src/main/java/com/learnboot/springbootlearn/config/TestDataSourceConfig.java) 页面监控功能
        4. 开启 SQL 监控
           * Druid 内置提供一个 StatFilter (监控SQL的功能)
           * 别名 stat 这个别名的映射配置信息保存在 druid-xxx.jar!/META-INF/druid-filter.properties. 在 Spring 中配置该别名（stat）开启 Druid SQL 监控的方式
            ```xml
            <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">
            ... ...
                <property name="filters" value="stat" />
            </bean>
            ```
           * 转换成配置类 [StatFilter](../src/main/java/com/learnboot/springbootlearn/config/TestDataSourceConfig.java) 
           * JDBC测试代码 [IndexController testSQL()](../src/main/java/com/learnboot/springbootlearn/controller/IndexController.java)
        5. 开启防火墙
           * Druid 内置防火墙 WallFilter(防御SQL注入攻击)
           * WallFilter 别名 wall,这个别名映射配置信息保存在 druid-xxx.jar!/META-INF/druid-filter.properties 中.这个别名映射配置信息保存在 druid-xxx.jar!/META-INF/druid-filter.properties 中
           ```xml
           <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">
           ... ...
                <property name="filters" value="wall" />
           </bean>
           ```
           * 可与其他 filter 一起使用
           ```xml
           <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">
           ...
                <property name="filters" value="wall,stat"/>
           </bean>
           ```
           * 转换成配置类 [wall](../src/main/java/com/learnboot/springbootlearn/config/TestDataSourceConfig.java) (在 dataSource 的 bean 中的 setFilter() 方法中添加即可. 可与其他 filter 一起使用,使用 ',' 分隔)
        6. 开启 Web-JDBC 关联监控
           * Druid 内置 WebStatFilter 过滤器(监控并采集 Web-JDBC 关联监控的数据)
           * 想要开启 Druid 的 Web-JDBC 关联监控,只需要将 WebStatFilter 配置在 Web 应用中的 WEB-INF/web.xml 中即可
           ```xml
            <filter>
                <filter-name>DruidWebStatFilter</filter-name>
                <filter-class>com.alibaba.druid.support.http.WebStatFilter</filter-class>
                <init-param>
                    <param-name>exclusions</param-name>
                    <param-value>*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*</param-value>
                </init-param>
            </filter>
            <filter-mapping>
                <filter-name>DruidWebStatFilter</filter-name>
                <url-pattern>/*</url-pattern>
            </filter-mapping>
           ```
           * 转换为配置类 [WebStatFilter](../src/main/java/com/learnboot/springbootlearn/config/TestDataSourceConfig.java)
        7. 通过 starter 整合 Druid
           * 阿里官方提供 Druid Spring Boot Starter,帮助轻松整合 Druid 数据库连接池和监控功能
           * 步骤
             1. 引入 Druid Spring Boot Starter 依赖
                * Spring Boot 项目的 pom.xml 添加依赖 `druid-spring-boot-starter`
             2. 配置属性
                * Druid Spring Boot Starter 已经将 Druid 数据源中的所有模块都进行默认配置
                * 可通过 SpringBoot 的配置文件 (application.properties/yml) 修改 Druid 的各个模块的配置(否则使用默认)
                * 在 Spring Boot 配置文件中配置以下内容:
                  * JDBC 通用配置
                  * Druid 数据源链接配置
                  * Druid 监控配置
                  * Druid 内置 Filter 配置
                > 这些配置内容既可以在 application.properties 中进行配置，也可以在 application.yml 中尽心配置，当配置内容较多时，我们通常推荐使用 application.yml
* JDBC 通用配置
  * 可以在 Spring Boot 的配置文件中对 JDBC 进行通用配置(数据库用户名 数据库密码 数据库URL 数据库驱动)(配置与之前的一样)
  * Druid 数据源连接池配置 [application.yml](../src/main/resources/application.yml)
* Druid 监控配置
  * [application.yml](../src/main/resources/application.yml)

* Druid 内置 Filter 配置
  * Druid Spring Boot Starter 对以下 Druid 内置 Filter,都提供了默认配置:
    * StatFilter
    * WallFilter
    * ConfigFilter
    * EncodingConvertFilter
    * Slf4jLogFilter
    * Log4jFilter
    * Log4j2Filter
    * CommonsLogFilter
  * 我们可以通过 spring.datasource.druid.filters=stat,wall ... 的方式来启用相应的内置 Filter，不过这些 Filter 使用的都是默认配置.
  * 如果默认配置不能满足我们的需求，我们还可以在配置文件使用 spring.datasource.druid.filter.* 对这些 Filter 进行配置.
  * [application.yml](../src/main/resources/application.yml)
> * 在配置 Druid 内置 Filter 时，需要先将对应 Filter 的 enabled 设置为 true，否则内置 Filter 的配置不会生效
> * 以上所有内容都只是示例配置，Druid Spring Boot Starter 并不是只支持以上属性，它支持 DruidDataSource 内所有具有 setter 方法的属性。 
#### 总结
 [官方链接](https://github.com/alibaba/druid/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98) 最重要

---
## MyBatis 整合
MyBatis 开发了一套基于 Spring Boot 模式的 starter：`mybatis-spring-boot-starter`
* 引入依赖
  * org.mybatis.spring.boot;mybatis-spring-boot-starter
* 配置MyBatis
  * 在 SpringBoot 配置文件 (application.properties/yml) 中进行配置
    * 例如指定 mapper.xml 的位置
    ```yaml
    mybatis:
        mapper-locations: classpath:mybatis/mapper.*.xml # 指定 mapper.xml 的位置
        type-aliases-package: com.learnboot.springbootlearn.entities # 扫描实体类的位置在此处指明扫描实体类的包在 mapper.xml 内就可以不写实体类的全路径
        configuration:
            map-underscore-to-camel-case: true # 默认开启驼峰命名法可不用设置
    ```
    > 注意：使用 MyBatis 时，必须配置数据源信息，例如数据库 URL、数据库用户型、数据库密码和数据库驱动等。
* 创建实体类
  * [user](../src/main/java/com/learnboot/springbootlearn/entities/User.java)
* 创建 Mapper 接口
  * [userMapper](../src/main/java/com/learnboot/springbootlearn/mapper/UserMapper.java)
  > mapper 接口较多时, 可在 SpringBoot 的主启动类上使用 @MapperScan 注解扫描指定包下的 mapper 接口(这样不需要再每个 mapper 接口上标注 @Mapper 注解)
* 创建 Mapper 映射文件
  * 在配置文件 application.properties/yml 通过 mybatis.mapper-locations 指定的位置中创建 [UserMapper.xml](../src/main/resources/mybatis/mapper/UserMapper.xml)
  * 示例 [application.properties](../src/main/resources/application.properties) [mapper.xml](../src/main/resources/mybatis/mapper/UserMapper.xml) [Entities](../src/main/java/com/learnboot/springbootlearn/entities/User.java) [mapperInterface](../src/main/java/com/learnboot/springbootlearn/mapper/UserMapper.java) [service](../src/main/java/com/learnboot/springbootlearn/service/UserService.java) [serviceImpl](../src/main/java/com/learnboot/springbootlearn/service/Impl/UserServiceImpl.java)
### 注解改写 mapper.xml
MyBatis 针对实际实际业务中使用最多的“增伤改查”操作,提供注解替换 mapper 映射文件:
1. @Select
2. @Insert
3. @Update
4. @Delete

以上注解,基本满足对数据库的增删查改功能.
* 示例
  * [Mapper.java](../src/main/java/com/learnboot/springbootlearn/mapper/UserMapper.java) 
  > mapper 接口中的任何一种方法都只能使用一种配置(注解或mapper映射文件 二选一) 但不同方法可以混合使用