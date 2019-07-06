package com.fame.zbw;

import com.zbw.fame.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhangbowen
 * @since 2019/6/24 11:32
 */
@Slf4j
public class UserServiceTests extends BaseTests {

    @Autowired
    private UserService userService;

    @Test
    public void test1() {
        log.info("{}", userService.login("fame", "123456"));
    }

    @Test
    public void test2() {
        boolean result = userService.resetUser("fame", "fame", "123@qq.com");
        log.info("{}", result);
    }

}
