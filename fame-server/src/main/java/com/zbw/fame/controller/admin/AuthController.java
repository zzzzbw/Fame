package com.zbw.fame.controller.admin;

import com.zbw.fame.model.domain.User;
import com.zbw.fame.service.UserService;
import com.zbw.fame.util.FameConsts;
import com.zbw.fame.util.FameUtil;
import com.zbw.fame.util.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 后台用户验证 Controller
 *
 * @author zbw
 * @since 2017/7/11 20:15
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthController {

    private final HttpServletRequest request;

    private final UserService userService;

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
        request.getSession().removeAttribute(FameConsts.USER_SESSION_KEY);
        return RestResponse.ok();
    }

    /**
     * 修改用户名密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return {@see Boolean}
     */
    @PostMapping("reset/password")
    public RestResponse resetPassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        User user = FameUtil.getLoginUser();
        if (StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(oldPassword)) {
            return RestResponse.fail("填写数据不能为空");
        }

        boolean result = userService.resetPassword(user.getUsername(), oldPassword, newPassword);

        this.logout();
        return RestResponse.ok(result);
    }

    /**
     * 修改用户信息
     *
     * @return {@see Boolean}
     */
    @PostMapping("reset/user")
    public RestResponse resetUser(@RequestParam String username, @RequestParam String email) {
        User user = FameUtil.getLoginUser();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(email)) {
            return RestResponse.fail("填写数据不能为空");
        }

        boolean result = userService.resetUser(user.getUsername(), username, email);
        this.logout();
        return RestResponse.ok(result);
    }

    /**
     * 获取用户名
     *
     * @return {@see String}
     */
    @GetMapping("user")
    public RestResponse<User> getUser() {
        User user = FameUtil.getLoginUser();
        return RestResponse.ok(user);
    }

}
