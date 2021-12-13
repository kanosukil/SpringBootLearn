package com.learnboot.springbootlearn.controller;

import com.learnboot.springbootlearn.exception.UserNotExistException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author VHBin
 * @date 2021/12/08 - 19:23
 */

@ControllerAdvice
public class TestExceptionHandler {
    @ExceptionHandler(UserNotExistException.class)
    public String handleException(Exception e, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        // 向 request 对象传入错误代码
        request.setAttribute("javax.servlet.error.status_code", 500);
        // 根据当前处理的异常,自定义错误数据
        map.put("code", "user.not-exist");
        map.put("message", e.getMessage());
        // 将自定义的错误传入到 request 域中
        request.setAttribute("ext", map);
        return "forward:/error";
    }
}
