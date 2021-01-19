package com.zbw.fame.service;

import com.zbw.fame.model.param.LoginParam;
import com.zbw.fame.model.param.ResetUserParam;
import com.zbw.fame.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zzzzbw
 * @since 2019/6/24 11:32
 */
@Slf4j
public class UserServiceTest extends BaseTest {

    @Autowired
    private UserService userService;

    @Test
    public void test1() {
        log.info("{}", userService.login(new LoginParam()));
    }

    @Test
    public void test2() {
        userService.resetUser(1, new ResetUserParam());
    }

}
