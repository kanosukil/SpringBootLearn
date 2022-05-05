## SpringBoot 自定义 starter
1. 命名规范
   * 官方的 starter 命名形式: spring-boot-starter-xxx 
   * 第三方自定义的 starter 命名形式: xxx-spring-boot-starter
   * 与 SpringBoot 生态提供的 starter 进行区分
2. 模块规范
   * (非硬性要求)SpringBoot 建议自定义 starter 时,创建两个 Module: autoConfigure Module & starter Module 
     * 其中 starter Module 依赖于 autoConfigure Module
   * 若不需要自动配置代码和依赖项目分离,可组合在同一个 Module 中
3. 自定义 starter
   1. 创建工程
      1. 创建 Empty Project (空项目)
      2. Project Structure -> Modules 添加 New Module (starter + autoconfiguration)
   2. 添加 POM 依赖
      * 在 starter 的 pom.xml 中 将 autocohnfiguration 作为依赖项
   3. 定义 properties 类
      * 在autoconfiguration 的 properties 包下创建 Properties 实体类(映射配置信息)
        * @ConfigurationProperties() 绑定配置文件中的配置
   4. 定义 Service 类
      * 在 autoconfiguration 的 service 包下创建 Service 类(供其他项目使用)
   5. 定义配置类
      * 在 autoconfiguration 的 autoConfiguration 包下创建配置类
        * AutoConfiguration 使用的注解 
          * @Configuration 表示为一个配置类
          * @EnableConfigurationProperties(xxxProperties.class) 开启 xxxProperties 的属性配置功能,并将该类以组件的形式注入容器
          * @ConditionalOnMissingBean(xxxService.class) 表示容器中没有 xxxService 类时生效
          * @Bean 用于将该方法的返回值以 Bean 对象的形式注入到容器中
   6. 创建 spring.factories 文件
      1. 在 autoconfiguration 的类路径下 (/resources) 下创建 META-INF 文件夹 ,并在 META-INF 文件夹下创建 spring.factories 文件 (即 /resources/META-INF/spring.factories)
      2. 将 SpringBoot 的 EnableAutoConfiguration 接口与自定义的 starter 自动配置类 xxxAutoConfiguration 组成一组键值对添加到 spring.factories 文件中(方便 SpringBoot 启动时,获取自定义 starter 自动配置)
      > Spring Factories 机制是 Spring Boot 中的一种服务发现机制，这种机制与 Java SPI 机制十分相似。Spring Boot 会自动扫描所有 Jar 包类路径下 META-INF/spring.factories 文件，并读取其中的内容，进行实例化，这种机制也是 Spring Boot Starter 的基础。
   7. 构建 starter
      1. 构建 autoConfigure Module
         * 在 autoconfiguration 的 pom.xml 中执行 `mvn clean install` 进行构建
      2. 构造 starter Module 
         * 构建完 autoconfigure Module 后才可构建 starter Module(starter Module 构建完后 其他SpringBoot项目便可以应用该自定义的 starter )
           * 在 starter 的 pom.xml 中执行 `mvn clean install` 命令进行构建
           > 引用自定义 starter 的项目不在本地时, 使用 `mvn clean deploy` 命令将自定义的 starter 部署到 远程仓库