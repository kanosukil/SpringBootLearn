package com.learnboot.springbootlearn.filter; /**
 * @author VHBin
 * @date 2021/12/08 - 20:57
 */

import javax.servlet.*;
import javax.servlet.annotation.*;
import java.io.IOException;

public class FilterRegistration implements Filter {
    @Override
    public void init(FilterConfig config) throws ServletException {
        System.out.println("FilterRegistration 初始化");
    }

    @Override
    public void destroy() {
        System.out.println("FilterRegistration 销毁");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        System.out.println("FilterRegistration doFilter");
        chain.doFilter(request, response);
    }
}
