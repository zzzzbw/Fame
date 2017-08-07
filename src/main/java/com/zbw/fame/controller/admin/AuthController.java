package com.zbw.fame.controller.admin;

import com.zbw.fame.controller.BaseController;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.model.Users;
import com.zbw.fame.service.UsersService;
import com.zbw.fame.util.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 后台用户验证 Controller
 *
 * @auther zbw
 * @create 2017/7/11 20:15
 */
@RestController
@RequestMapping("/api/admin")
public class AuthController extends BaseController {

    @Autowired
    private UsersService usersService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public RestResponse test(Boolean success) {
        if (success == null) {
            return RestResponse.fail("参数为空");
        }
        if (success) {
            return RestResponse.ok();
        } else {
            return RestResponse.fail();
        }

    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public RestResponse login(HttpServletResponse response, String username, String password, String remember_me) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return RestResponse.fail("用户名和密码不能为空");
        }
        try {
            Users user = usersService.login(username, password);
        } catch (Exception e) {
            String msg = "登陆失败";
            if (e instanceof TipException) {
                msg = e.getMessage();
            } else {
                e.printStackTrace();
            }
            return RestResponse.fail(msg);
        }

        return RestResponse.ok();
    }
}
