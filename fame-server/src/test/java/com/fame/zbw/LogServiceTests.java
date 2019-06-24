package com.fame.zbw;

import com.zbw.fame.model.domain.Log;
import com.zbw.fame.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * @author zhangbowen
 * @since 2019/6/24 13:54
 */
@Slf4j
public class LogServiceTests extends BaseTests {

    @Autowired
    private LogService logService;

    @Test
    public void test1() {
        Page<Log> page = logService.getLogs(0, 10);
        log.info("{}", page);
    }

}
