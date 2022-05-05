## SpringBoot拦截器
+ 拦截器:
    - 登录校验
    - 权限验证
    - 乱码解决
    - 性能监控
    - 异常处理
    - etc.
+ SpringBoot使用拦截器
    1. 定义拦截器
    2. 注册拦截器
    3. 指定拦截规则(若拦截所有包括静态资源)
* **定义拦截器**
    * 创建一个拦截器类,实现 HandlerInterceptor 接口
    * HandlerInterceptor 接口定义的三个方法:
        * boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler); 控制器处理请求方法前执行,返回值表示是否中断后续操作(true继续;false中断后续)
        * void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView); 控制器处理请求方法后,解析试图之前执行,可通过此方法对请求域中的模型和视图做进一步的修改
        * void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object Handler, Exception ex); 视图渲染结束后执行,可通过此方法实现资源清理,记录日志信息等工作
        * [LoginInterceptor.java](../src/main/java/com/learnboot/springbootlearn/component/LoginInterceptor.java)
* **注册拦截器**
    * 创建一个实现了WebMvcConfiguration 接口的配置类 (使用了 @Configuration 注解的类),重写 addInterceptors() 方法,并在该方法内调用 registry.addInterceptor() 方法将自定义的拦截器注册到容器中
    * [MvcConfig.java](../src/main/java/com/learnboot/springbootlearn/config/MvcConfig.java)
* **指定拦截规则**
    * 使用 registry.addInterceptor() 方法注册到容器后,继续指定拦截器拦截规则
        + 指定拦截器规则时,调用的方法:
            - addPathPatterns: 拦截指定路径('/**'表示拦截所有)
            - excludePathPatterns: 排除拦截的路径
---
---