package com.zbw.fame.service.impl;

import com.zbw.fame.Application;
import com.zbw.fame.service.LogsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zbw on 2017/12/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class LogsServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(LogsServiceImplTest.class);

    @Autowired
    private LogsService logsService;

    @Test

    public void save() throws Exception {
        logger.info(logsService.getLogs(1, 10).toString());
        logsService.save("测试3", "log3", "测试 message3", "add");
        logger.info(logsService.getLogs(1, 10).toString());
    }

    @Test
    public void save1() throws Exception {
    }

    @Test
    public void save2() throws Exception {
    }

    @Test
    public void getLogs() throws Exception {
        logger.info(logsService.getLogs(1, 10).toString());
    }

}