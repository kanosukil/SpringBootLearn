package com.learnboot.springbootlearn.filter; /**
 * @author VHBin
 * @date 2021/12/08 - 20:31
 */

import javax.servlet.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebFilter(filterName = "TestFilter")
public class TestFilter implements Filter {
    @Override
    public void init(FilterConfig config) throws ServletException {
        System.out.println("TestFilter 初始化");
    }

    @Override
    public void destroy() {
        System.out.println("MyFilter 销毁");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        System.out.println("MyFilter doFilter " + request.getServerName());
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        chain.doFilter(request, response);
    }
}
