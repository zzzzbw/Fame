package com.zbw.fame.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbw.fame.exception.NotFoundException;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.mapper.UserMapper;
import com.zbw.fame.model.dto.LoginUser;
import com.zbw.fame.model.dto.UserDetailsDto;
import com.zbw.fame.model.entity.User;
import com.zbw.fame.model.param.LoginParam;
import com.zbw.fame.model.param.ResetPasswordParam;
import com.zbw.fame.model.param.ResetUserParam;
import com.zbw.fame.service.UserService;
import com.zbw.fame.util.FameUtils;
import com.zbw.fame.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private static final String ADMIN_ROLE = "ADMIN";

    /**
     * 创建保存到登录的User
     *
     * @param userDetailsDto 登录的 UserDetailsDto
     * @return User
     */
    private LoginUser createTokenUser(UserDetailsDto userDetailsDto) {
        LoginUser loginUser = new LoginUser();
        final User user = userDetailsDto.getUser();
        FameUtils.copyPropertiesIgnoreNull(user, loginUser);

        // 生成token
        final String username = userDetailsDto.getUsername();
        final Collection<? extends GrantedAuthority> authorityList = userDetailsDto.getAuthorityList();
        String roles = CollUtil.join(AuthorityUtils.authorityListToSet(authorityList), ",");
        String token = JwtUtil.generateToken(username, roles, null);
        loginUser.setToken(token);

        return loginUser;
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

        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(ADMIN_ROLE);
        UserDetailsDto userDetailsDto = createUserDetails(user, authorityList);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetailsDto, null, userDetailsDto.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return createTokenUser(userDetailsDto);
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
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(ADMIN_ROLE);
        return createUserDetails(user, authorityList);
    }

    private UserDetailsDto createUserDetails(User user, Collection<? extends GrantedAuthority> authorityList) {
        return new UserDetailsDto(user, authorityList);
    }
}
