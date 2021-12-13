## SpringBoot 国际化
国际化(Internationalization 简称I18n 首尾In,中间18个字符) 软件开发时应该支持的多语言和地区功能(开发的软件需要同时应对不同国家和地区的用户访问,并根据用户地区和语言习惯,提供相应的、符合阅读习惯的页面和数据)

+ Spring 项目实现国际化的步骤:
    - 编写国际化资源(配置)文件
    - 使用 ResourceBundleMessageSource 管理国际化资源文件
    - 在页面获取国际化内容

1. 编写国际化资源文件
    * 在 SpringBoot 类路径下创建国际化资源文件(格式: 基本名_语言代码_国家或地区代码;例子:login_zh_CN.properties)
        * 在`src/main/resources/i18n` 下创建国际化资源文件
        * IDEA 将识别创建完的资源文件并转换为 `Resource Bundle '基本名'` 的形式
2. 使用 ResourceBundleMessageSource 管理国际化资源文件
    * SpringBoot 已为 ResourceBundleMessageSource 提供了默认自动配置
    * SpringBoot 通过 [MessageSourceAutoConfiguration](../MethodsTXT/MessageSourceAutoConfiguration.txt) 对 ResourceBundleMessageSource 提供默认配置.
        + 从源码可知:
            - SpringBoot 将 MessageSourceProperties 已组件的形式加入到容器
            - MessageSourceProperties 的属性与配置文件中以 "spring.message" 开头的配置进行绑定
            - SpringBoot 从容器中获取 MessageSourceProperties 组件,并从中读取国际化资源文件的 basename encoding 等信息,并封装到 ResourceBundleMessageSource 内.
            - SpringBoot 将 ResourceBundleMessageSource 以组件的形式添加到容器中,进而实现对国际化资源文件的管理
            - MessageSourceProperties(与spring.message绑定) -> ResourceBundleMessageSource(从前者获取basename + encoding + etc.) 二者都要以组件形式进入容器
        + [MessageSourceProperties](../MethodsTXT/MessageSourceProperties.txt) 中可知,MessageSourceProperties 为 basename encoding 等默认信息(basename = 'message' 即 SpringBoot 默认从类路径下的 message.properties 以及 message_xxx.properties 作为国际化资源文件)[可以在application.properties/yml等配置文件中,使用参数 spring.messages.basename 重新指定国际化资源文件基本名]
3. 在页面获取国际化内容
    + 由于使用的 Thymeleaf 模板引擎,因此可以使用 #{..} 获取国际化内容
    + 例子见 [test.html](../src/main/resources/templates/test.html)
4. 区域信息系解析器自动配置
    * SpringMVC进行国际化时的2个重要对象
        1. Locale 区域对象
        2. LocaleResolver 区域信息解析器,容器中的组件,负责获取区域信息对象
    * SpringBoot 在 WebMvcAutoConfiguration 中为 LocaleResolver 进行了自动配置
        * 该方法默认向容器中添加了一个区域信息解析器组件,并根据请求头携带的Accept-Language参数获取相应区域信息的对象
        * 并使用了 @ConditionalOnMissingBean 注解,其函数 name 取值为 localResolver (与该方法注入到容器中的组件名称一致).也就是说,手动添加一个名为localResolver组件时,SpringBoot自动配置的区域信息配置器将失效,手动添加的将生效.
        * 手动切换语言:
            1. 修改html中需要切换语言的链接为`/index.html(l='zh_CN')`的形式(Thymeleaf模板引擎的参数使用"()"替代"?")
            2. 在 component 包下创建区域信息解析器 (实现 LocaleResolver 接口)
            3. 在 MvcConfig 中添加将自定义区域信息解析器(以组件的形式添加到容器)的方法
---
---