## spring-boot-starter-web (Web启动器)
> * Spring MVC 为 Spring 提供的基于MVC设计模式的轻量级 Web 开发框架, 其本身就是 Spring 框架的一部分,可与 Spring 框架无缝集成,性能优越
> * Spring Boot 为 Spring 基础上创建的一款开源框架, 其提供了 spring-boot-starter-web (Web场景启动器) 来为 Web 开发给予支持. spring-boot-starter-web 为我们提供了嵌入的 Servlet 容器以及 SpringMVC 的依赖, 并为 Spring MVC 提供了大量自动配置, 可以适用于大多数 Web 开发场景.

### SpringBootWeb 快速开发
SpringBoot 为 SpringMVC 提供了自动配置, 并在 SpringMVC 默认功能的基础上提供了以下特性:
1. 引入 ContentNegotiatingViewResolver 和 BeanNameViewResolver (视图解析器)
2. 对包括 WebJars 在内的静态资源提供支持
3. 自动注册 Converter GenericConverter Formatter (转换器和格式化器)
4. 对 HttpMessageConverters 的支持 (Spring MVC中用于转换 HTTP 请求和响应的消息转换器)
5. 自动注册 MessageCodesResolver (用于定义错误代码生成规则)
6. 支持对静态首页 (index.html) 的访问
7. 自动使用 ConfigurableWebBindingInitializer

只需要在 SpringBoot 项目的 pom.xml 中引入 spring-boot-starter-web ,即使不进行配置,也可直接使用 SpringMVC 进行 Web 开发.
> spring-boot-starter-web 默认引入核心启动器 spring-boot-start, 因此当 SpringBoot 项目中的 pom.xml 引入 spring-boot-starter-web 依赖后,就不用再引入 spring-boot-starter 核心启动器的依赖了.

---
##Spring Boot 静态资源映射
Web应用中涉及大量静态资源(JS CSS HTML等)
> * SpringMVC 导入静态资源文件时,需要配置静态资源的映射
> * SpringBoot 已经默认完成了该工作

SpringBoot提供的3种默认静态资源映射规则
* WebJars 映射
* 默认资源映射
* 静态首页(欢迎页)映射
---
* ***WebJars 映射***
    * 以Jar形式为Web项目提供资源文件
        * WebJars 可以将 Web 前端资源(JS CSS等)打成一个个 Jar 包, 然后把 Jar 包部署在 Maven 中央仓库中进行统一管理, 当Spring Boot 项目中需要引入 Web 前端资源时, 只要访问 [WebJars官网](https://www.webjars.org/) 找到所需资源的 pom 依赖,再将其导入到项目中即可.
        * 所有通过WebJars引入的前端资源都存放在当前项目的类路径(classpath)下的`/META-INF/resources/webjars/`目录中.
        * SpringBoot 通过 MVC 的自动配置类 WebMvcAutoConfiguration 为 WebJars 前端资源提供默认映射规则 [部分源码](../MethodsTXT/WebMvcAutoConfiguration.txt)
* ***默认静态资源映射***
    * 访问项目中的任意资源时,SpringBoot 会默认从以下路径查找资源文件(优先级依次降低):
        1. classpath:/META-INF/resources/
        2. classpath:/resources/
        3. classpath:/static/
        4. classpath:/public/
        * 以上称为静态文件夹
* ***静态首页(欢迎页)映射***
    * 静态资源文件夹下的所有index.html被称为静态首页或欢迎页,会被 /** 映射
    * 访问 '/' 或 '/index.html' 时,将跳转到静态首页(欢迎页)
  > 注: 同样由查找顺序优先级
---
##SpringBoot 定制 SpringMVC
SpringBoot抛弃了传统的xml配置文件,通过配置类(标识@Configuration的类)以JavaBean的形式进行相关配置
> SpringBoot对SpringMVC的自动配置可以满足大部分需求,但也可以通过自定义配置类并实现WebMvcConfiguration接口定制SpringMVC配置(拦截器、格式化程序、视图控制器等)
>> * SpringBoot1.5及之前通过继承WebMvcConfigurationAdapter抽象类定制SpringMVC
>> * SpringBoot2.0后改为实现WebMvcConfigurer接口定制SpringMVC
+ WebMvcConfigurer
    - 基于 Java8 的接口
    - 其中大部分方法为 default 类型(且都为空实现)

<table>
  <tbody>
    <tr>
      <th>方法</th>
      <th>说明</th>
    </tr>
    <tr>
      <td>default void configurePathMatch(PathMatchConfigurer configurer) {}</td>
      <td>HandlerMappings 路径的匹配规则。</td>
    </tr>
    <tr>
      <td>default void configureContentNegotiation(ContentNegotiationConfigurer configurer) {}</td>
      <td>内容协商策略（一个请求路径返回多种数据格式）。</td>
    </tr>
    <tr>
      <td>default void configureAsyncSupport(AsyncSupportConfigurer configurer) {}</td>
      <td>处理异步请求。</td>
    </tr>
    <tr>
      <td>default void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {}</td>
      <td>这个接口可以实现静态文件可以像 Servlet 一样被访问。</td>
    </tr>
    <tr>
      <td>default void addFormatters(FormatterRegistry registry) {}</td>
      <td>添加格式化器或者转化器。</td>
    </tr>
    <tr>
      <td><span style="color:#b22222;">default void addInterceptors(InterceptorRegistry registry) {}</span></td>
      <td><span style="color:#b22222;">添加 Spring MVC 生命周期拦截器，对请求进行拦截处理。</span></td>
    </tr>
    <tr>
      <td><span style="color:#b22222;">default void addResourceHandlers(ResourceHandlerRegistry registry) {}</span></td>
      <td><span style="color:#b22222;">添加或修改静态资源（例如图片，js，css 等）映射；<br />
      Spring Boot 默认设置的静态资源文件夹就是通过重写该方法设置的</span>。</td>
    </tr>
    <tr>
      <td>default void addCorsMappings(CorsRegistry registry) {}</td>
      <td>处理跨域请求。</td>
    </tr>
    <tr>
      <td><span style="color:#b22222;">default void addViewControllers(ViewControllerRegistry registry) {}</span></td>
      <td><span style="color:#b22222;">主要用于实现无业务逻辑跳转，例如主页跳转，简单的请求重定向，错误页跳转等</span></td>
    </tr>
    <tr>
      <td>default void configureViewResolvers(ViewResolverRegistry registry) {}</td>
      <td>配置视图解析器，将 Controller 返回的字符串（视图名称），转换为具体的视图进行渲染。</td>
    </tr>
    <tr>
      <td>default void addArgumentResolvers(List&lt;HandlerMethodArgumentResolver&gt; resolvers) {}</td>
      <td>添加解析器以支持自定义控制器方法参数类型<br/>实现该方法不会覆盖用于解析处理程序方法参数的内置支持；<br/>
      要自定义内置的参数解析支持， 同样可以通过 RequestMappingHandlerAdapter 直接配置 RequestMappingHandlerAdapter 。</td>
    </tr>
    <tr>
      <td>default void addReturnValueHandlers(List&lt;HandlerMethodReturnValueHandler&gt; handlers) {}</td>
      <td>添加处理程序来支持自定义控制器方法返回值类型<br/>使用此选项不会覆盖处理返回值的内置支持；<br />
      要自定义处理返回值的内置支持，请直接配置 RequestMappingHandlerAdapter。</td>
    </tr>
    <tr>
      <td>default void configureMessageConverters(List&lt;HttpMessageConverter&lt;?&gt;&gt; converters) {}</td>
      <td>用于配置默认的消息转换器（转换 HTTP 请求和响应）。</td>
    </tr>
    <tr>
      <td>default void extendMessageConverters(List&lt;HttpMessageConverter&lt;?&gt;&gt; converters) {}</td>
      <td>直接添加消息转换器，会关闭默认的消息转换器列表；<br />
      <strong>实现该方法即可在不关闭默认转换器的起提下，新增一个自定义转换器。</strong></td>
    </tr>
    <tr>
      <td>default void configureHandlerExceptionResolvers(List&lt;HandlerExceptionResolver&gt; resolvers) {}</td>
      <td>配置异常解析器。</td>
    </tr>
    <tr>
      <td>default void extendHandlerExceptionResolvers(List&lt;HandlerExceptionResolver&gt; resolvers) {}</td>
      <td>扩展或修改默认的异常解析器列表。</td>
    </tr>
  </tbody>
</table>

* SpringBoot中，定制SpringMVC的两种形式：
    * 拓展 SpringMVC
    * 全面接管 SpringMVC

* ***拓展 SpringMVC***

SpringBoot的自动配置不能满足需求时，自定义一个 `WebMvcConfigurer 类型(实现了 WebMvcCongfigurer 接口)的配置类(标注了 @Configuration 但没标注 @EnableWebMvc 注解)` 来拓展 SpringMVC. 不仅能保留SpringBoot对SpringMVC的自动配置,也额外增加自定义的SpringMVC配置

> controller 是使用 @Controller 注解还是 @RestController:
> * @Controller 用于返回静态页面
> * @RestController 用于返回 json 数据
    > 详例见 [IndexController.java](../src/main/java/com/learnboot/springbootlearn/controller/IndexController.java)

* ***全面接管 SpringMVC***

需要抛弃 SpringBoot 对 SpringMVC 的全部自动配置时,完全接管 SpringMVC,就需要自定义一个 WebMvcConfiguration 类型(实现 WebMvcConfiguration 接口的配置类),并标注 @EnableWebMvc 注解,来完全接管 SpringMVC
> 完全接管 SpringMVC 后, SpringBoot 对 SpringMVC 的自动配置 全部失效

---
---

##SpringBoot 整合 Thymeleaf
[Thymeleaf学习](ThymeleafStudy.md)

SpringBoot 推荐使用 Thymeleaf 作为模板引擎. SpringBoot 为 Thymeleaf 提供一系列默认配置, SpringBoot 项目一旦导入 Thymeleaf 依赖,相应的自动配置就会自动生效(ThymeleafAutoConfiguration 或 FreeMarkerAutoConfiguration)
+ Spring 整合 Thymeleaf 模板引擎, 步骤
    1. 引入 Starter 依赖
    2. 创建模板文件,并放在指定目录下
+ 引入依赖
    - pom.xml 中添加 Thymeleaf 的 Starter 依赖
```xml
<!--Thymeleaf 启动器-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```
+ 创建模板文件
    - ThymeleafAutoConfiguration 自动配置类的部分注解
      ```java
      @Configuration(proxyBeanMethods=false)
      @EnableConfigurationProperties({ThymeleafProperties.class})
      @ConditionalOnClass({ThymeleafMode.class, SpringTemplateEngine.class})
      @AutoConfigurationAfter({WebMvcAutoConfiguration.class, WebFluxAutoConfiguration.class})
      public class ThymeleafAutoConfiguration {
      }
      ```
    - ThymeleafAutoConfiguration 使用了 @EnableConfigurationProperties 注解导入 ThymeleafProperties 类, 该类的部分源码:
      ```java
      @ConfigurationProperties(prefix = "spring.thymeleaf")
      public class ThymeleafProperties {
          private static final Charset DEFAULT_ENCODING;
          public static final String DEFAULT_PREFIX = "classpath:/templates/";
          public static final String DEFAULT_SUFFIX = ".html";
          private boolean checkTemplate = true;
          private boolean checkTemplateLocation = true;
          private String prefix = "classpath:/templates/";
          private String suffix = ".html";
          private String mode = "HTML";
          private Charset encoding;
          private boolean cache;
      }
      ```
        - ThymeleafProperties 通过 @ConfigurationProperties 注解将配置文件(application.properties/yml)中前缀为 spring.thymeleaf 的配置和这个类的属性绑定
        - 在 ThymeleafProperties 中还提供了以下静态变量：
            1. DEFAULT_ENCODING:默认编码格式
            2. DEFAULT_PREFIX:视图解析器的前缀
            3. DEFAULT_SUFFIX:视图解析器的后缀
        - Thymeleaf 模板的默认位置在 resources/templates 目录下，默认的后缀是 html，即只要将 HTML 页面放在“classpath:/templates/”下，Thymeleaf 就能自动进行渲染。

---
---