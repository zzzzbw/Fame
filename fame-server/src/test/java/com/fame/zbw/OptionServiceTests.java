package com.fame.zbw;

import com.zbw.fame.service.OptionService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhangbowen
 * @since 2019/6/24 10:44
 */
@Slf4j
public class OptionServiceTests extends BaseTests {

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
