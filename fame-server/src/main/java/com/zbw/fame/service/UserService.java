package com.zbw.fame.service;

import com.zbw.fame.model.domain.User;

/**
 * User Service 接口
 *
 * @author zbw
 * @since 2017/7/12 21:25
 */
public interface UserService {

    /**
     * 用户登陆
     *
     * @param username 用户名
     * @param password 密码
     * @return User
     */
    User login(String username, String password);

    /**
     * 用户重置密码
     *
     * @param oldUsername 原用户名
     * @param newUsername 新用户名
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @return boolean
     */
    boolean reset(String oldUsername, String newUsername, String oldPassword, String newPassword);
}
