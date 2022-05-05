# 学习SpringBoot

>***注*** :端口被占用  cmd 指令:
>>
>>`netstat -aon|findstr 端口号` 
>>
>>`taskkill -f -pid 占用端口的pid`
>>
> **bean 的属性名最好为全小写**

**注**: 注入Bean时不能使用 驼峰命名 mybatis 需要关闭驼峰命名法 (解决方法 **未知**)

---
### 项目结构

`/src/main/java`:程序入口

`/src/main/resources`:配置文件入口

`/src/test`:测试入口

### 引入Web模块

在`pom.xml`(Maven配置文件)中引入依赖模块:

`spring-boot-starter` :核心,包括自动配置支持、日志和yaml

`spring-boot-starter-test`:测试,包括JUnit、Hamcrest、Mockito

`spring-boot-starter-web`:web模块

***idea已自动配置好***

>***注***:@SpringApplicationConfiguration注解已失效,应替换成@SpringBootTest

---
## SpringBoot Starter

**Starter(启动器)** 整合了应用研发的场景下可能用到的各种依赖, 程序员只需要 **在Maven中引入Starter依赖** ,SpringBoot就可自动扫描到要加载的信息并启动相应默认配置. Starter提供了大量自动配置,且所有Starter都遵循约定成俗的默认配置,并允许调整配置 (**约定大于配置**)

> **注**: 并非所有Starter都由SpringBoot官方提供,有部分Starter由第三方技术厂商提供[e.g.*druid-spring-boot-starter* *mybatis-spring-boot-starter*] 也有些SpringBoot官方没提供Starter,第三方厂商也没提供Starter的技术

拓: `mvn dependency:tree` 查看依赖树

通过 `mvn dependency:tree` 命令可知,SpringBoot导入了Springframework logging jackson Tomcat等依赖(开发Web项目时所需的).

### spring-boot-starter-parent (版本仲裁中心)

spring-boot-starter-parent是 **所有** SpringBoot项目的父级依赖,可对项目内的部分常用依赖进行统一管理.

Springboot项目可以通过继承spring-boot-starter-parent来获得一些合理的默认配置,其特性有:
    
+ 默认JDK版本(Java 8)
+ 默认字符集(UTF-8)
+ 依赖管理功能
+ 资源过滤
+ 默认插件配置
+ 识别 `application.properties` 和 `application.yml` 类型的配置文件

>在 `spring-boot-starter-parent` 底层中, 其有一个父级依赖 `spring-boot-dependencies` 而 `spring-boot-dependencies` 中有如下配置:
>>* dependencyManagement:负责管理依赖
>>* pluginManagement:负责管理插件
>>* properties:负责定义依赖或插件的版本号


* [SpringBoot 配置](markdown/SpringBootConfiguration.md)
* [SpringBoot 日志](markdown/SpringBootLog.md)
* [SpringBoot Web快速开发](markdown/SpringBootWebStatic.md)
* [SpringBoot 国际化](markdown/SpringBootInternationalization.md)
* [SpringBoot 拦截器](markdown/SpringBootInterceptor.md)
* [SpringBoot 异常处理](markdown/SpringBootInterceptor.md)
* [SpringBoot 注册Web原生组件](markdown/SpringBootWebRegistration.md)
* [SpringBoot 数据库](markdown/SpringBootDataBase.md)
* [SpringBoot 自定义Starter](markdown/SpringBootStarter.md) <label style="color:red;">(拓展)[非必须]</label>

