package com.learnboot.springbootlearn.config;

import com.learnboot.springbootlearn.service.Impl.PersonServiceImpl;
import com.learnboot.springbootlearn.service.PersonService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author VHBin
 * @date 2021/12/04 - 15:32
 */

/*
    @Configuration 注解用于定义一个配置类(相当于Spring的配置文件)
    配置类中包含一个或多个被 @Bean 注解的方法,该方法相当于Spring配置文件中配置的<bean>标签定义的组件
 */

@Configuration
public class MyAppConfig {

    /*
        与<bean id="personService" class="PersonServiceImpl"></bean>等价
        该方法返回值以组件的形式添加到容器中
        方法名即为组件id(相当于<bean>标签的id)
     */

    @Bean
    public PersonService personService() {
        System.out.println("向容器中添加了一个组件:personService");
        return new PersonServiceImpl();
    }
}
