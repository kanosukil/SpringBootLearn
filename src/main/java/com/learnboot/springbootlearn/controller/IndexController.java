package com.learnboot.springbootlearn.controller;

import com.learnboot.springbootlearn.exception.UserNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author VHBin
 * @date 2021/12/06 - 12:03
 */

// @Controller 返回静态页面时使用
// @RestController (@ResponseBody + @Controller 的组合) 返回 json 数据时使用
// 使用 @Controller /login 将跳转到 login.html 页面
// 使用 @RestController /login 仅返回 login 字符串显示在页面上

@Controller
public class IndexController {
    // 跳转到登录页面

    @GetMapping(value = {"/login"})
    public String loginPage() {
        // 跳转到 login.html
        return "login";
    }

    @RequestMapping("/test")
    public String test(Map<String, Object> map) {
        // 通过 map向前台页面传数据
        map.put("name", System.currentTimeMillis());
        return "test";
    }

//    @RequestMapping("/main")
//    public String mainPage() {
//        System.out.println("IndexController");
//        return "blog_list";
//    }

    @RequestMapping(value = "/testException")
    public String testException(String user) {
        if (user == null||"user".equals(user)){
            throw new UserNotExistException();
        }
        return "login";
    }

    @Autowired
    JdbcTemplate jdbcTemplate;
    /**
     * 访问数据库
     * @return
     */
    @ResponseBody
    @RequestMapping("/testSQL")
    public String testSQL() {
        Integer i = jdbcTemplate.queryForObject("SELECT count(*) from `course`", Integer.class);
        return i.toString();
    }
}
