package com.learnboot.springbootlearn.config;

import com.learnboot.springbootlearn.component.LoginInterceptor;
import com.learnboot.springbootlearn.component.ThisLocalResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author VHBin
 * @date 2021/12/06 - 12:00
 */

// 实现 WebMvcConfigurer 接口,拓展 SpringMVC 的功能
// @EnableWebMvc 不 接管 SpringMVC
// 全面接管 SpringMVC 时,才需要使用 @EnableWebMvc 注解

@Slf4j
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 访问 '/' 或 '/main.html' 时跳转到 login.html
        registry.addViewController("/").setViewName("main");
        registry.addViewController("/index.html").setViewName("main");
        // 添加视图映射 main.html 指向 其他文件
        registry.addViewController("/main.html").setViewName("main");
        registry.addViewController("/main").setViewName("main");
    }

    // 注册自定义的区域信息配置器
    @Bean
    public LocaleResolver localeResolver() {
        return new ThisLocalResolver();
    }

    // 注册自定义的拦截器  +  拦截规则的指定
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("注册拦截器");
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                // 拦截所有资源,包括静态资源
                .excludePathPatterns("/", "/login", "/main.html", "/user/login", "/css/**", "/images/**", "/js/**",
                        "/fonts/**", "/test", "/testException", "/error", "/main.html")
                // 放行登录页面 登陆操作 静态资源
        ;
    }

}
