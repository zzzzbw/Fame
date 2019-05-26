package com.zbw.fame.controller.admin;

import com.zbw.fame.controller.BaseController;
import com.zbw.fame.model.domain.User;
import com.zbw.fame.service.UserService;
import com.zbw.fame.util.FameConsts;
import com.zbw.fame.util.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 后台用户验证 Controller
 *
 * @author zbw
 * @since 2017/7/11 20:15
 */
@RestController
@RequestMapping("/api/admin")
public class AuthController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 后台登录
     *
     * @param response   {@link HttpServletResponse}
     * @param username   用户名
     * @param password   密码
     * @param rememberMe 是否记住
     * @return {@see RestResponse.ok()}
     */
    @PostMapping("login")
    public RestResponse login(HttpServletResponse response, @RequestParam String username, @RequestParam String password, String rememberMe) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return RestResponse.fail("用户名和密码不能为空");
        }
        User user = userService.login(username, password);
        request.getSession().setAttribute(FameConsts.USER_SESSION_KEY, user);

        return RestResponse.ok();
    }

    /**
     * 登出
     *
     * @return {@see RestResponse.ok()}
     */
    @PostMapping("logout")
    public RestResponse logout() {
        User user = this.user();
        if (null == user) {
            return RestResponse.fail("没有用户登陆");
        }

        request.getSession().removeAttribute(FameConsts.USER_SESSION_KEY);
        return RestResponse.ok();
    }

    /**
     * 重置用户名密码
     *
     * @param oldUsername 原用户名
     * @param newUsername 新用户名
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return {@see Boolean}
     */
    @PostMapping("reset")
    public RestResponse resetPassword(@RequestParam String oldUsername, @RequestParam String newUsername, @RequestParam String oldPassword, @RequestParam String newPassword) {
        if (StringUtils.isEmpty(oldPassword)
                || StringUtils.isEmpty(newUsername)
                || StringUtils.isEmpty(oldPassword)
                || StringUtils.isEmpty(newPassword)) {
            return RestResponse.fail("填写数据不能为空");
        }

        if (!oldUsername.equals(this.user().getUsername())) {
            return RestResponse.fail("用户名与登陆的不符合");
        }

        boolean result = userService.reset(oldUsername, newUsername, oldPassword, newPassword);
        return RestResponse.ok(result);
    }

    /**
     * 获取用户名
     *
     * @return {@see String}
     */
    @GetMapping("username")
    public RestResponse username() {
        User user = this.user();
        if (null == user) {
            return RestResponse.fail("没有用户登陆");
        }

        return RestResponse.ok(user.getUsername());
    }

}
