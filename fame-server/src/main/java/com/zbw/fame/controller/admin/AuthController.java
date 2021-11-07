package com.zbw.fame.controller.admin;


import com.zbw.fame.model.dto.TokenDto;
import com.zbw.fame.model.entity.User;
import com.zbw.fame.model.param.LoginParam;
import com.zbw.fame.model.param.RefreshTokenParam;
import com.zbw.fame.model.param.ResetPasswordParam;
import com.zbw.fame.model.param.ResetUserParam;
import com.zbw.fame.service.UserService;
import com.zbw.fame.util.FameUtils;
import com.zbw.fame.util.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 后台用户验证 Controller
 *
 * @author zzzzbw
 * @since 2017/7/11 20:15
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthController {

    private final UserService userService;

    /**
     * 后台登录
     *
     * @return {@link RestResponse#ok()}
     */
    @PostMapping("login")
    public RestResponse<TokenDto> login(@RequestBody @Valid LoginParam param) {
        TokenDto tokenDto = userService.login(param);
        return RestResponse.ok(tokenDto);
    }

    /**
     * 修改用户名密码
     *
     * @return {@link RestResponse#ok()}
     */
    @PutMapping("reset/password")
    public RestResponse<RestResponse.Empty> resetPassword(@RequestBody @Valid ResetPasswordParam param) {
        userService.resetPassword(FameUtils.getLoginUserId(), param);
        return RestResponse.ok();
    }

    /**
     * 修改用户信息
     *
     * @return {@link RestResponse#ok()}
     */
    @PutMapping("reset/user")
    public RestResponse<RestResponse.Empty> resetUser(@RequestBody @Valid ResetUserParam param) {
        userService.resetUser(FameUtils.getLoginUserId(), param);
        return RestResponse.ok();
    }

    /**
     * 获取当前用户
     *
     * @return {@link RestResponse#ok()}
     */
    @GetMapping("user")
    public RestResponse<User> getUser() {
        User user = userService.getCurrentUser();
        return RestResponse.ok(user);
    }

    /**
     * 登出
     *
     * @return
     */
    @PostMapping("logout")
    public RestResponse<RestResponse.Empty> logout() {
        return RestResponse.ok();
    }

    /**
     * 刷新登陆
     *
     * @param param
     * @return
     */
    @PostMapping("refresh")
    public RestResponse<TokenDto> refresh(@RequestBody @Valid RefreshTokenParam param) {
        TokenDto tokenDto = userService.refreshToken(param);
        return RestResponse.ok(tokenDto);
    }
}
