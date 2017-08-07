package com.zbw.fame.service;

import com.zbw.fame.model.Users;

/**
 * Users Service 接口
 *
 * @auther zbw
 * @create 2017/7/12 21:25
 */
public interface UsersService {

    /**
     * 用户登陆
     * @param username
     * @param password
     * @return
     */
    Users login(String username, String password);
}
