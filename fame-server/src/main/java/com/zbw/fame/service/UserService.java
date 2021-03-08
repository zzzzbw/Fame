package com.zbw.fame.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbw.fame.model.dto.LoginUser;
import com.zbw.fame.model.entity.User;
import com.zbw.fame.model.param.LoginParam;
import com.zbw.fame.model.param.ResetPasswordParam;
import com.zbw.fame.model.param.ResetUserParam;

/**
 * User Service 接口
 *
 * @author zzzzbw
 * @since 2017/7/12 21:25
 */
public interface UserService extends IService<User> {

    /**
     * 用户登陆
     *
     * @param param 登录参数
     * @return User
     */
    LoginUser login(LoginParam param);

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
