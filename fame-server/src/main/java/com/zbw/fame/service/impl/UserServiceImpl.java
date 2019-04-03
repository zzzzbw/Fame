package com.zbw.fame.service.impl;

import com.zbw.fame.exception.TipException;
import com.zbw.fame.mapper.UserMapper;
import com.zbw.fame.model.domain.User;
import com.zbw.fame.service.UserService;
import com.zbw.fame.util.FameUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * User Service 层实现类
 *
 * @author zbw
 * @since 2017/7/12 21:24
 */
@Service("usersService")
@Transactional(rollbackFor = Throwable.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String username, String password) {
        User record = new User();
        record.setUsername(username);
        String md5 = FameUtil.getMd5(password);
        record.setPasswordMd5(md5);
        User user = userMapper.selectOne(record);
        if (user == null) {
            throw new TipException("用户名或者密码错误");
        }
        user.setLogged(new Date());
        userMapper.updateByPrimaryKey(user);
        //清空密码
        user.setPasswordMd5(null);
        return user;
    }

    @Override
    public boolean reset(String username, String oldPassword, String newPassword) {
        User record = new User();
        record.setUsername(username);
        User user = userMapper.selectOne(record);
        if (null == user) {
            throw new TipException("该用户名不存在");
        }

        if (!user.getPasswordMd5().equals(FameUtil.getMd5(oldPassword))) {
            throw new TipException("原密码错误");
        }

        user.setPasswordMd5(FameUtil.getMd5(newPassword));
        int a = userMapper.updateByPrimaryKey(user);
        return a > 0;
    }
}
