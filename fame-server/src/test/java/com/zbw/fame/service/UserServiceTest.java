package com.zbw.fame.service;

import com.zbw.fame.BaseTest;
import com.zbw.fame.model.dto.LoginUser;
import com.zbw.fame.model.entity.User;
import com.zbw.fame.model.param.LoginParam;
import com.zbw.fame.model.param.ResetUserParam;
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
    public void login() {
        LoginParam loginParam = new LoginParam();
        loginParam.setUsername("fame");
        loginParam.setPassword("123456");
        LoginUser login = userService.login(loginParam);
        log.info("{}", login);
    }

    @Test
    public void resetUser() {
        userService.resetUser(1, new ResetUserParam());
    }

}
