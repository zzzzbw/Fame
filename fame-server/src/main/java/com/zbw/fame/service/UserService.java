package com.zbw.fame.service;

import com.zbw.fame.model.domain.User;
import com.zbw.fame.model.param.LoginParam;
import com.zbw.fame.model.param.ResetPasswordParam;
import com.zbw.fame.model.param.ResetUserParam;

/**
 * User Service 接口
 *
 * @author zzzzbw
 * @since 2017/7/12 21:25
 */
public interface UserService {

    /**
     * 用户登陆
     *
     * @return User
     */
    User login(LoginParam param);

    /**
     * 修改用户密码
     *
     * @param id
     * @param param
     */
    void resetPassword(Integer id, ResetPasswordParam param);

    /**
     * 修改用户信息
     *
     * @param id
     * @param param
     */
    void resetUser(Integer id, ResetUserParam param);
}
