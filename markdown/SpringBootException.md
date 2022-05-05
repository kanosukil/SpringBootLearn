## SpringBoot 异常处理

SpringBoot提供的默认异常处理机制,会在出现异常时,自动识别客户端的类型(浏览器客户端\机器客户端),依此展示不同异常信息.

1. 对于浏览器客户端
    * SpringBoot显示"whitelabel"错误试图(HTML呈现)
2. 对于机器客户端
    * SpringBoot生成JSON响应

* SpringBoot 异常处理自动配置原理
    * ErrorMvcAutoConfiguration 对异常处理提供自动配置
        * 该配置向容器注入了4个组件
            * ErrorPageCustomizer 系统异常后,默认将请求转发到 "/error" 上
            * BasicErrorController 处理默认的 "/error" 请求
            * DefaultErrorViewResolver 默认的错误视图解析器,将异常信息解析到相应的错误试图上
            * DefaultErrorAttributes 用于页面上共享异常信息
        * ErrorPageCustomizer
            * 用于定制错误页面的响应规则
            * 通过 registerErrorPage() 方法注册错误页面的响应规则.
            * 系统异常后 ErrorPageCustomizer 组件自动生效,并转发请求到 "/error" 上,交由 BasicErrorController 处理
        * BasicErrorController
            * [BasicErrorController部分源码](../MethodsTXT/BasicErrorController.txt)
            * SpringBoot 通过 BasicErrorController 进行统一错误处理
            * SpringBoot 自动识别发出请求的客户端类型(浏览器/机器客户端),根据客户端类型,将请求分别交给 errorHtml() 和 error() 方法进行处理
                * 浏览器客户端 `ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response;` text/html(错误页面)
                * 机器客户端(安卓/IOS/Postman/etc.) `ResponseEntity<Map<String, Object>> error(HttpServletRequest request);` JSON数据
              > 就是说 浏览器异常通过errorHtml()处理;机器异常通过error()处理
                * errorHtml()方法调用父类(AbstractErrorController)的 [resolveErrorView()](../MethodsTXT/resolveErrorView.txt) 方法,在响应页面时,会在父类的 resolveErrorView() 方法中获取容器中的所有 ErrorViewResolver 对象(错误视图解析器,包括 DefaultErrorViewResolver 在内),一同解析异常信息.
        * DefaultErrorViewResolver
            * [DefaultErrorViewResolver组件](../MethodsTXT/DefaultErrorViewResolver.txt)
            * 发出请求的客户端为浏览器时,SpringBoot 会获取容器中所有的 ErrorViewResolver 对象,并分别调用它们的 resolverErrorView() 方法对异常信息进行解析(包括 DefaultErrorViewResolver)
            * DefaultErrorViewResolver 解析异常信息步骤:
                1. 根据错误状态码(404 500等),生成错误视图(error/status)[例:error/404 error/500]
                2. 尝试使用模板引擎解析 error/status 视图(即 尝试从 classpath 类路径下的 templates 目录下, 查找 error/status.html)[例: error/404.html error/500.html]
                3. 若模板引擎能够解析到 error/status 视图,将视图和数据封装成 ModelAndView 返回并结束整个解析流程(否则跳至第4步)
                4. 依次从各个静态资源文件夹中查找 error/status.html ,若静态文件夹中找到了错误页面,则返回并结束整个解析流程(否则跳至第5步)
                5. 将错误状态码(404 500)转换为4xx 或 5xx,然后重复前4步,若解析成功,则结束整个解析流程(否则跳至第6步)
                6. 处理默认的"/error"请求,使用SpringBoot的默认错误页面
        * DefaultErrorAttributes
            * [DefaultErrorAttributes](../MethodsTXT/DefaultErrorAttributes.txt) 是 SpringBoot 默认错误属性处理工具,可从请求中获取异常或错误信息,并封装成一个 Map 对象.
            * SpringBoot 默认 Error 控制器(BasicErrorController) 处理错误时,会调用 DefaultErrorAttributes 的 getErrorAttributes() 方法获取错误或异常信息, 并封装成 model 数据(Map对象), 返回到页面或JSON数据中
                * 该 model 数据含有的属性:
                    * timestamp 时间戳
                    * status 错误状态码
                    * error 错误的提示
                    * exception 导致请求处理失败的异常对象
                    * message 错误/异常消息
                    * trace 错误/异常栈信息
                    * path 错误/异常抛出时所请求的URL路径
                  > 所有通过 DefaultErrorAttributes 封装到 model 数据中的属性,都可以直接在页面或 JSON 中获取.
---
## SpringBoot 全局异常处理
* 定制错误页面
    * 自定义 error.html `resources/templates/error.html`
    * 自定义动态错误页面 `resources/templates/error/404||500||4xx||etc. .html`
    * 自定义静态错误页面 `resources/static/error/404||500||4xx||etc. .html`
* 自定义 error.html
    * 在模板引擎文件夹 /resources/templates 下创建,将覆盖 SpringBoot 默认的错误视图页面
    * 使用 Thymeleaf 模板引擎获取错误信息 (th:text=${};status error timestamp message path)
* 自定义 动态错误页面
    * SpringBoot 项目使用模板引擎,在发生异常时,SpringBoot 的默认错误视图解析器(DefaultErrorViewResolver) 将解析模板引擎文件夹 (/resources/templates) 下的 error 目录,寻找适配的错误页面.
    * 精确匹配:
        * 根据状态码精确匹配对应的错误页面(404.html 500.html)
    * 模糊匹配
        * 使用 4xx.html 和 5xx.html 作为动态错误页面的文件名,并放置在 error 目录下,来模糊匹配对应类型的所有错误
* 自定义静态错误页面
    * 若 SpringBoot 项目没有使用模板引擎, 程序异常时, SpringBoot 的默认错误视图解析器 (DefaultErrorViewResolver) 将解析 静态资源文件夹下的 error 目录中的静态页面
    * 精准匹配
        * 根据错误状态码的不同,分别创建不同的静态页面;程序异常时,SpringBoot 将根据错误状态码精准匹配对应的错误页面 (静态页面不能使用 Thymeleaf 表达式)
    * 模糊匹配
        * 使用 4xx.html 5xx.html ,异常时SpringBoot自动模糊匹配所有类型的错误
* **错误页面优先级**
    * 自定义动态精准 > 自定义静态精准 > 自定义动态模糊 > 自定义静态模糊 > 自定义 error.html
* **定制错误数据**
    1. 异常时 将请求转发到 "/error", 交由BasicErrorController (SpringBoot 默认的 Error 控制器) 进行处理
    2. BasicErrorController 根据客户端不同,自动适配返回的响应形式(浏览器 返回错误页面;机器 返回JSON数据)
    3. BasicErrorController 处理异常时,调用 DefaultErrorAttributes (默认错误属性处理工具)的 getErrorAttributes() 方法获取错误数据

定制SpringBoot错误数据:
1. 自定义异常处理类 将请求转发到 "/error" 交由 SpringBoot 底层(BasicErrorController)处理,自动适配浏览器端和机器端
2. 通过继承 DefaultErrorAttributes 定义一个错误属性处理工具, 并在原来的基础上,添加自定义的错误数据.

***1.自定义异常处理类***
@ControllerAdvice 注解的类可实现全局异常处理

> 示例见:
> [userNotExistException.java](../src/main/java/com/learnboot/springbootlearn/exception/UserNotExistException.java)
> [TestException.java](../src/main/java/com/learnboot/springbootlearn/controller/TestExceptionHandler.java)

***2.自定义错误属性处理工具***

> 见: [MyErrorAttributes](../src/main/java/com/learnboot/springbootlearn/component/MyErrorAttributes.java)