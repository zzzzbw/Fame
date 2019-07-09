package com.zbw.fame.service.impl;

import com.zbw.fame.exception.TipException;
import com.zbw.fame.model.domain.User;
import com.zbw.fame.repository.UserRepository;
import com.zbw.fame.service.UserService;
import com.zbw.fame.util.FameUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public User login(String username, String password) {
        User user = userRepository.findByUsernameOrEmail(username, username);
        if (null == user) {
            throw new TipException("用户名或者密码错误");
        }
        String md5 = FameUtil.getMd5(password);
        if (!md5.equals(user.getPasswordMd5())) {
            throw new TipException("用户名或者密码错误");
        }
        user.setLogged(new Date());
        userRepository.save(user);
        return createSessionUser(user);
    }

    /**
     * 创建保存到Session的User
     *
     * @param user 数据库User
     * @return User
     */
    private User createSessionUser(User user) {
        User sessionUser = new User();
        BeanUtils.copyProperties(user, sessionUser);
        // 清空密码
        sessionUser.setPasswordMd5("");
        return sessionUser;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean resetPassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username);
        if (null == user) {
            throw new TipException("该用户名不存在");
        }

        if (!user.getPasswordMd5().equals(FameUtil.getMd5(oldPassword))) {
            throw new TipException("原密码错误");
        }

        user.setPasswordMd5(FameUtil.getMd5(newPassword));
        return userRepository.save(user) != null;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean resetUser(String oldUsername, String newUsername, String email) {
        User user = userRepository.findByUsername(oldUsername);
        if (null == user) {
            throw new TipException("该用户名不存在");
        }
        user.setUsername(newUsername);
        user.setEmail(email);

        return userRepository.save(user) != null;
    }
}
