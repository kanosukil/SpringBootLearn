## 注册Web原生组件
SpringBoot 默认以Jar包方式部署,默认没有 web.xml 因此不能使用web.xml配置 Servlet Filter Listener SpringBoot 提供2种方法注册:
1. 通过组件扫描注册
2. 使用 RegistrationBean 注册

* 组件扫描注册
    * servlet3.0的3个注解
        * @WebServlet 声明Servlet
        * @WebFilter 声明Filter
        * @WebListener 声明Listener
  > 这些注解可直接标注在对应组件上，它们与在 web.xml 中的配置意义相同。每个注解都具有与 web.xml 对应的属性，可直接配置，省去了配置 web.xml 的繁琐。
    * 在SpringBoot注册原生Web组件,可使用 @ServletComponentScan 注解实现(该注解可扫描@WebServlet @WebFilter @WebListener 注解的组件类,并将其注册到容器中)
  > 注: @ServletComponentScan 注解只能标记在启动类或配置类上

> 例子
> [Servlet](../src/main/java/com/learnboot/springbootlearn/servlet/TestServlet.java)
> [Filter](../src/main/java/com/learnboot/springbootlearn/filter/TestFilter.java)
> [Listener](../src/main/java/com/learnboot/springbootlearn/listener/TestListener.java)
> 启动类上添加 @ServletComponentScan 注解

* 使用 RegistrationBean 注册
    * 特点
        * 在配置类中使用
        * 不需要 @WebServlet @WebListener @WebFilter 注解
    * 为抽象类,负责将组件注册到 Servlet 容器中, Spring 提供三个实现类(分别注册Servlet Filter Listener)
        * ServletRegistrationBean
        * FilterRegistrationBean
        * ServletListenerRegistrationBean
    * 在配置类中,使用@Bean注解将 上三者 添加到 Spring 容器中 (将自定义的Servlet Filter Listener 注册到容器中使用)
  > 例子:
  > [ServletRegistration](../src/main/java/com/learnboot/springbootlearn/servlet/ServletRegistration.java)
  > [FilterRegistration](../src/main/java/com/learnboot/springbootlearn/filter/FilterRegistration.java)
  > [ListenerRegistration](../src/main/java/com/learnboot/springbootlearn/listener/ListenerRegistration.java)
  > [RegistrationConfig](../src/main/java/com/learnboot/springbootlearn/config/RegistrationConfig.java)
---
---