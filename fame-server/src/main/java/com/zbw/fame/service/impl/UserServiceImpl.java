package com.zbw.fame.service.impl;

import com.zbw.fame.exception.NotFoundException;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.model.domain.User;
import com.zbw.fame.model.param.LoginParam;
import com.zbw.fame.model.param.ResetPasswordParam;
import com.zbw.fame.model.param.ResetUserParam;
import com.zbw.fame.repository.UserRepository;
import com.zbw.fame.service.UserService;
import com.zbw.fame.util.FameUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * User Service 层实现类
 *
 * @author zzzzbw
 * @since 2017/7/12 21:24
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * 创建保存到Session的User
     *
     * @param user 数据库User
     * @return User
     */
    private User createSessionUser(User user) {
        User sessionUser = new User();
        FameUtils.copyPropertiesIgnoreNull(user, sessionUser);
        // 清空密码
        sessionUser.setPasswordMd5("");
        return sessionUser;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public User login(LoginParam param) {
        String username = param.getUsername();
        String password = param.getPassword();

        User user = userRepository.findByUsernameOrEmail(username, username);
        if (null == user) {
            throw new TipException("用户名或者密码错误");
        }
        String md5 = FameUtils.getMd5(password);
        if (!md5.equals(user.getPasswordMd5())) {
            throw new TipException("用户名或者密码错误");
        }
        user.setLogged(new Date());
        userRepository.save(user);
        return createSessionUser(user);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void resetPassword(Integer id, ResetPasswordParam param) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(User.class));

        if (!user.getPasswordMd5().equals(FameUtils.getMd5(param.getOldPassword()))) {
            throw new TipException("原密码错误");
        }

        user.setPasswordMd5(FameUtils.getMd5(param.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void resetUser(Integer id, ResetUserParam param) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(User.class));
        user.setUsername(param.getUsername());
        user.setEmail(param.getEmail());
        userRepository.save(user);
    }
}
