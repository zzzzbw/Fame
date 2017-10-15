package com.zbw.fame.service.impl;

import com.zbw.fame.exception.TipException;
import com.zbw.fame.mapper.UsersMapper;
import com.zbw.fame.model.Users;
import com.zbw.fame.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * User Service 层实现类
 *
 * @auther zbw
 * @create 2017/7/12 21:24
 */
@Service("usersService")
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersMapper usersMapper;

    /**
     * 用户登陆
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public Users login(String username, String password) {
        Users temp = new Users();
        temp.setUsername(username);
        temp.setPasswordMd5(password);
        Users user = usersMapper.selectOne(temp);
        if (user == null) {
            throw new TipException("用户名或者密码错误");
        }
        user.setLogged(new Date());
        usersMapper.updateByPrimaryKey(user);
        //清空密码
        user.setPasswordMd5(null);
        return user;
    }

    @Override
    public boolean reset(String username, String oldPassword, String newPassword) {
        Users record = new Users();
        record.setUsername(username);
        Users user = usersMapper.selectOne(record);
        if (null == user) {
            throw new TipException("该用户名不存在");
        }

        if (!user.getPasswordMd5().equals(oldPassword)) {
            throw new TipException("原密码错误");
        }

        user.setPasswordMd5(newPassword);
        int a=usersMapper.updateByPrimaryKey(user);
        return a > 0;
    }
}
