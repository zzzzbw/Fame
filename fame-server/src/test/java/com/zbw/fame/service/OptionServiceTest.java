package com.zbw.fame.service;

import com.zbw.fame.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zzzzbw
 * @since 2019/6/24 10:44
 */
@Slf4j
public class OptionServiceTest extends BaseTest {

    @Autowired
    private OptionService optionService;

    @Test
    public void test1() {
        log.info("{}", optionService.getAllOptionMap());
    }

    @Test
    public void test2() {
        optionService.save("new_option", "new123");
    }
}
