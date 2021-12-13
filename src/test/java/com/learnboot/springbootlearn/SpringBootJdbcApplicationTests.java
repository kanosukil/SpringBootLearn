package com.learnboot.springbootlearn;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author VHBin
 * @date 2021/12/08 - 21:44
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootJdbcApplicationTests {
    //数据源组件
    @Autowired
    @Qualifier("DruidDataSource")
    DataSource dataSource;
    //用于访问数据库的组件
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    public void contextLoads() throws SQLException {
        assert dataSource != null;
        System.out.println("默认数据源为：" + dataSource.getClass());
        System.out.println("数据库连接实例：" + dataSource.getConnection());
        //访问数据库
        assert jdbcTemplate != null;
        Integer i = jdbcTemplate.queryForObject("SELECT count(*) from `course`", Integer.class);
        System.out.println("course 表中共有" + i + "条数据。");
    }
}
