package com.learnboot.springbootlearn;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * @author VHBin
 * @date 2021/12/05 - 15:43
 */

@SpringBootTest
public class SpringBootLoggingApplicationTests {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void setLoggerTest() {
        logger.trace("trace 级别日志");
        logger.debug("debug 级别日志");
        logger.info("info 级别日志");
        logger.warn("warn 级别日志");
        logger.error("error 级别日志");

    }
}
