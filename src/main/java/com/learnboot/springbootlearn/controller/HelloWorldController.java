package com.learnboot.springbootlearn.controller;

import com.learnboot.springbootlearn.entities.FamiliarPerson;
import com.learnboot.springbootlearn.entities.Person;
import com.learnboot.springbootlearn.entities.SpecialPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author VHBin
 * @date 2021/12/03 - 19:25
 */

//@RestController
//public class HelloWorldController {
//    @RequestMapping("/hello")
//    public String index() {
//        return "Hello SpringBoot!";
//    }
//}

@Controller
public class HelloWorldController {

    @Autowired
    private Person person;

    @Autowired
    private SpecialPerson specialPerson;

    @Autowired
    private FamiliarPerson familiarPerson;

    @ResponseBody
    @RequestMapping("/hello")
    public Person hello() {
        return person;
    }

    @ResponseBody
    @RequestMapping("/helloSpecial")
    public SpecialPerson helloSp() {
        return specialPerson;
    }

    @ResponseBody
    @RequestMapping(value = "/helloFamiliar")
    public FamiliarPerson helloFp() {
        // 解决中文乱码
        HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
        assert response != null;
        response.setCharacterEncoding("UTF-8");
        return familiarPerson;
    }

//    @RequestMapping("/")
//    public String index() {
//        return "forward:main.html";
//    }

}
