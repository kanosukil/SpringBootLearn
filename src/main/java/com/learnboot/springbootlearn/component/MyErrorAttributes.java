package com.learnboot.springbootlearn.component;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * @author VHBin
 * @date 2021/12/08 - 19:47
 */

// 向容器中添加自定义的错误属性处理工具
@Component
public class MyErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
        // 添加自定义错误数据
        errorAttributes.put("company", "VHbinSpringBootTest");
        // 获取 TestExceptionHandler 传入 request 域中的错误数据
        Map ext = (Map) webRequest.getAttribute("ext", 0);
        errorAttributes.put("ext", ext);
        return errorAttributes;
    }
}
