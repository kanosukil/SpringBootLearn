package com.learnboot.springbootlearn;

import com.learnboot.springbootlearn.entities.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class SpringBootLearnApplicationTests {

    @Autowired
    Person person;
    // IOC容器
    @Autowired
    ApplicationContext ioc;

    @Test
    public void testPersonService() {
        // 校验 IOC 容器中是否包含了 personService
        boolean b = ioc.containsBean("personService");
        System.out.println("personService" + (b?"包含":"不包含") + "在IOC容器中");
    }

    @Test
    void contextLoads() {
        System.out.println(person);
    }

}

