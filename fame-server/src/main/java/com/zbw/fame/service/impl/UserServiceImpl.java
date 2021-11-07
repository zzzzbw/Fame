package com.zbw.fame.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbw.fame.exception.LoginExpireException;
import com.zbw.fame.exception.NotFoundException;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.mapper.UserMapper;
import com.zbw.fame.model.dto.TokenDto;
import com.zbw.fame.model.dto.UserDetailsDto;
import com.zbw.fame.model.entity.User;
import com.zbw.fame.model.param.LoginParam;
import com.zbw.fame.model.param.RefreshTokenParam;
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

    /**
     * 用户角色, 目前全部赋予ADMIN
     */
    private static final String ADMIN_ROLE = "ADMIN";
    /**
     * token刷新允许时间: 10分钟
     */
    private static final int TOKEN_REFRESH_SECOND = 60 * 10;

    /**
     * 创建保存到登录的User
     *
     * @param userDetails 登录的 userDetails
     * @return User
     */
    private TokenDto createTokenDto(UserDetails userDetails) {
        TokenDto tokenDto = new TokenDto();
        final String username = userDetails.getUsername();
        final Collection<? extends GrantedAuthority> authorityList = userDetails.getAuthorities();
        String roles = CollUtil.join(AuthorityUtils.authorityListToSet(authorityList), ",");
        String token = JwtUtil.generateToken(username, roles, null);
        tokenDto.setToken(token);
        String refreshToken = JwtUtil.generateRefreshToken(username, roles, null);
        tokenDto.setRefreshToken(refreshToken);

        return tokenDto;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public TokenDto login(LoginParam param) {
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


        return createTokenDto(userDetailsDto);
    }

    @Override
    public User getCurrentUser() {
        final User user = FameUtils.getLoginUser();
        User currentUser = new User();
        // 拷贝数据并清除密码
        FameUtils.copyPropertiesIgnoreNull(user, currentUser);
        currentUser.setPasswordMd5(null);
        return currentUser;
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
    public TokenDto refreshToken(RefreshTokenParam param) {
        String refreshToken = param.getRefreshToken();

        if (JwtUtil.isTokenExpired(refreshToken)) {
            throw new LoginExpireException();
        }

        // 避免频繁刷新token, 校验token最近刷新
        Date created = JwtUtil.getCreated(refreshToken);
        Date refreshDate = new Date();
        if (refreshDate.after(created) && refreshDate.before(DateUtil.offsetSecond(created, TOKEN_REFRESH_SECOND))) {
            // 直接返回当前token
            String jwtToken = FameUtils.getJwtHeaderToken();
            TokenDto tokenDto = new TokenDto();
            tokenDto.setToken(jwtToken);
            tokenDto.setRefreshToken(refreshToken);
            return tokenDto;
        }

        String username = JwtUtil.getSubject(refreshToken);
        UserDetails userDetails = loadUserByUsername(username);
        if (null == userDetails) {
            throw new TipException("用户不存在");
        }

        return createTokenDto(userDetails);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = lambdaQuery()
                .eq(User::getUsername, username)
                .or()
                .eq(User::getEmail, username)
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
