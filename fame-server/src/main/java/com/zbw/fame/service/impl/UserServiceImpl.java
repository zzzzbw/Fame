package com.zbw.fame.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbw.fame.exception.NotFoundException;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.mapper.UserMapper;
import com.zbw.fame.model.dto.LoginUser;
import com.zbw.fame.model.entity.User;
import com.zbw.fame.model.param.LoginParam;
import com.zbw.fame.model.param.ResetPasswordParam;
import com.zbw.fame.model.param.ResetUserParam;
import com.zbw.fame.service.UserService;
import com.zbw.fame.util.FameUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * User Service 层实现类
 *
 * @author zzzzbw
 * @since 2017/7/12 21:24
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, UserDetailsService {

    /**
     * 创建保存到Session的User
     *
     * @param user 数据库User
     * @return User
     */
    private LoginUser createSessionUser(User user) {
        LoginUser sessionUser = new LoginUser();
        FameUtils.copyPropertiesIgnoreNull(user, sessionUser);
        return sessionUser;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public LoginUser login(LoginParam param) {
        String username = param.getUsername();
        String password = param.getPassword();

        User user = lambdaQuery()
                .eq(User::getUsername, username)
                .or()
                .eq(User::getEmail, username)
                .one();
        if (null == user) {
            throw new TipException("用户名或者密码错误");
        }
        String md5 = FameUtils.getMd5(password);
        if (!md5.equals(user.getPasswordMd5())) {
            throw new TipException("用户名或者密码错误");
        }
        user.setLogged(new Date());
        updateById(user);
        return createSessionUser(user);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void resetPassword(Integer id, ResetPasswordParam param) {
        User user = getById(id);
        if (null == user) {
            throw new NotFoundException(User.class);
        }

        if (!user.getPasswordMd5().equals(FameUtils.getMd5(param.getOldPassword()))) {
            throw new TipException("原密码错误");
        }

        user.setPasswordMd5(FameUtils.getMd5(param.getNewPassword()));
        updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void resetUser(Integer id, ResetUserParam param) {
        User user = getById(id);
        if (null == user) {
            throw new NotFoundException(User.class);
        }

        user.setUsername(param.getUsername());
        user.setEmail(param.getEmail());
        updateById(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = lambdaQuery()
                .eq(User::getUsername, username)
                .one();
        if (null == user) {
            throw new TipException("用户不存在");
        }

        // 模拟构造包含用户角色列表的`List<GrantedAuthority>`对象
        // 预留 RABC 功能，现全部赋予管理员权限
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList("ADMIN");
        return createUserDetails(user, authorityList);
    }

    private UserDetails createUserDetails(User user, Collection<? extends GrantedAuthority> authorityList) {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return authorityList;
            }

            @Override
            public String getPassword() {
                return user.getPasswordMd5();
            }

            @Override
            public String getUsername() {
                return user.getUsername();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }
}
