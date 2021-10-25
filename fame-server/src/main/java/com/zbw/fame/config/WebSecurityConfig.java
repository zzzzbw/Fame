package com.zbw.fame.config;

import com.zbw.fame.interceptor.JwtAuthenticationFilter;
import com.zbw.fame.util.ErrorCode;
import com.zbw.fame.util.FameUtils;
import com.zbw.fame.util.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * web security 配置
 *
 * @author zzzzbw
 * @since 2021/10/25 14:22
 */
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String[] EXCLUDE_URL = new String[]{"/"};

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf() // JWT不需要csrf
                .disable()
                .sessionManagement() // token不需要session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, EXCLUDE_URL)
                .permitAll()
                .antMatchers("/api/admin/login") // 登陆相关url放行
                .anonymous()
                .antMatchers(HttpMethod.OPTIONS) // 跨域options请求放行
                .permitAll()
                .anyRequest()
                .authenticated();

        http.headers().cacheControl(); // 禁用缓存

        // 增加过滤器
        http
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 添加自定义异常入口
        http
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler());
    }

    /**
     * AccessDeniedException 策略
     *
     * @return
     */
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) ->
                FameUtils.writeJsonResponse(RestResponse.fail(ErrorCode.NOT_LOGIN.getCode(), ErrorCode.NOT_LOGIN.getMsg()), response);
    }

    /**
     * AuthenticationException 策略
     *
     * @return
     */
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authenticationException) ->
                FameUtils.writeJsonResponse(RestResponse.fail(ErrorCode.NOT_LOGIN.getCode(), ErrorCode.NOT_LOGIN.getMsg()), response);
    }

}
