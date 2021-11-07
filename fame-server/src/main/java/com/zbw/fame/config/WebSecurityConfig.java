package com.zbw.fame.config;

import com.zbw.fame.interceptor.JwtAuthenticationFilter;
import com.zbw.fame.util.ErrorCode;
import com.zbw.fame.util.FameUtils;
import com.zbw.fame.util.RestResponse;
import lombok.RequiredArgsConstructor;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * web security 配置
 *
 * @author zzzzbw
 * @since 2021/10/25 14:22
 */
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    /**
     * security的鉴权排除的url列表
     */
    private static final String[] EXCLUDED_AUTH_PAGES = {
            "/css/**", "/js/**", "/images/**", "/webjars/**", "/**/favicon*",
            "/*.html", "/**/*.html", "/**/*.css", "/**/*.js"
    };

    private static final String LOGIN_IRL = "admin/login";

    private static final String REFRESH_TOKEN = "admin/refresh";

    private static final String LOGOUT_URL = "admin/logout";

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
        // 不走Spring Security匹配
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS) // options请求
                .antMatchers(HttpMethod.GET, EXCLUDED_AUTH_PAGES) // 静态页面
                .antMatchers(apiUrl(LOGIN_IRL), apiUrl(REFRESH_TOKEN)) // 后台登陆刷新
                .requestMatchers(request -> !new AntPathRequestMatcher(apiUrl("admin/**")).matches(request)); // 非后台接口
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 开启跨域
                .cors().and()
                // CSRF 禁用，因为不使用 Session
                .csrf().disable()
                // 基于 token 机制，所以不需要 Session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .headers().frameOptions().disable()
                .and()
                .logout() // 配置登出接口
                .logoutUrl(apiUrl(LOGOUT_URL))
                .logoutSuccessHandler(logoutSuccessHandler())
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(apiUrl("admin/**")) // 后台接口验证
                .authenticated();

        // 增加过滤器
        http
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // 添加自定义异常入口
        http
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler());
    }

    private String apiUrl(String url) {
        return "/api/" + url;
    }

    /**
     * JWT 过滤器。
     * <p>
     * 这里必须通过new而不是Spring注入，否则Spring会把这个Filter注入到全局中，而不受Spring Security控制
     *
     * @return jwtAuthenticationFilter
     */
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(userDetailsService);
    }

    /**
     * 登出成功
     *
     * @return logoutSuccessHandler
     */
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> FameUtils.writeJsonResponse(RestResponse.ok(), response);
    }

    /**
     * AccessDeniedException 策略
     *
     * @return accessDeniedHandler
     */
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) ->
                FameUtils.writeJsonResponse(RestResponse.fail(ErrorCode.NOT_LOGIN.getCode(), ErrorCode.NOT_LOGIN.getMsg()), response);
    }

    /**
     * AuthenticationException 策略
     *
     * @return authenticationEntryPoint
     */
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authenticationException) ->
                FameUtils.writeJsonResponse(RestResponse.fail(ErrorCode.NOT_LOGIN.getCode(), ErrorCode.NOT_LOGIN.getMsg()), response);
    }

}
