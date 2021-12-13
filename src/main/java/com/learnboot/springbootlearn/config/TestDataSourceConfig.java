package com.learnboot.springbootlearn.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author VHBin
 * @date 2021/12/09 - 16:46
 */

@Configuration
public class TestDataSourceConfig implements WebMvcConfigurer {
    /**
     * 像容器中添加Druid数据源
     * @ConfigurationPropertise 绑定配置文件中的 spring.datasource 开头的配置和数据源属性绑定
     * @return
     *
     */
    @ConfigurationProperties("spring.datasource.druid")
    @Bean("DruidDataSource")
    public DataSource dataSource() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        // 设置属性值为 "stat" 开启 SQL 监控; 开启防火墙 (wall)
        druidDataSource.setFilters("stat,wall");
        // 不建议硬编码数据源属性到代码中(即下m面的注释内的代码),而是使用@ConfigurationProperties 绑定
        //        druidDataSource.setUrl("jdbc:mysql://localhost:3306/experiment");
        //        druidDataSource.setUsername("root");
        //        druidDataSource.setPassword("root");
        //        druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return druidDataSource;
    }

    /**
     * 开启 Druid 内置监控页面
     *
     * @return
     */

    @Bean
    public ServletRegistrationBean statViewServlet() {
        StatViewServlet statViewServlet = new StatViewServlet();
        // 注入 StatViewServlet 并设置路径映射为 /druid/*
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(statViewServlet, "/druid/*");
        // 配置访问账号和密码(选配)
        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", "123456");
        return servletRegistrationBean;
    }

    /**
     * 向容器中添加 WebStatFilter
     * 开启 Web-JDBC 关联监控
     *
     * @return
     */
    public FilterRegistrationBean druidWebStatFilter() {
        WebStatFilter webStatFilter = new WebStatFilter();
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(webStatFilter);
        // 监控所有访问
        filterRegistrationBean.setUrlPatterns(Collections.singletonList("/*"));
        // 监控访问不包含以下路径
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }
}
