package com.learnboot.springbootlearn.config;

import com.learnboot.springbootlearn.filter.FilterRegistration;
import com.learnboot.springbootlearn.listener.ListenerRegistration;
import com.learnboot.springbootlearn.servlet.ServletRegistration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @author VHBin
 * @date 2021/12/08 - 21:01
 */

@Configuration
public class RegistrationConfig {

    @Bean public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new ServletRegistration(), "/ServletRegistration");
    }

    @Bean public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistration filterRegistration = new FilterRegistration();
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(filterRegistration);
        // 注册过滤器需要过滤的 url
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/ServletRegistration"));
        return filterRegistrationBean;
    }

    @Bean public ServletListenerRegistrationBean listenerRegistrationBean() {
        return new ServletListenerRegistrationBean(new ListenerRegistration());
    }
}
