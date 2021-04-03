package com.zbw.fame.interceptor;

import com.zbw.fame.exception.NotLoginException;
import com.zbw.fame.util.ErrorCode;
import com.zbw.fame.util.FameUtils;
import com.zbw.fame.util.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.stream.Stream;

/**
 * 管理后台 拦截器
 *
 * @author zzzzbw
 * @since 2017/10/11 14:10
 */
@Slf4j
@Component
public class AdminInterceptor implements HandlerInterceptor {

    private static final String AUTH_URIS = "/**/admin/**";

    private static final String[] IGNORE_URIS = {"/api/admin/login", "/api/admin/logout"};

    private static final PathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();

        //登录拦截
        if (isAuthUrl(url)) {
            // 调用方法查看是否有登录的用户
            try {
                FameUtils.getLoginUser();
            } catch (NotLoginException e) {
                log.info("Admin no login! requestUrl: {}, HttpMethod: {}, ip: {}", url, request.getMethod(), FameUtils.getIp());
                PrintWriter out = response.getWriter();
                RestResponse<RestResponse.Empty> resp = RestResponse.fail(ErrorCode.NOT_LOGIN.getCode(), ErrorCode.NOT_LOGIN.getMsg());
                String json = FameUtils.objectToJson(resp);
                out.print(json);
                out.flush();
                return false;
            }
        }

        return true;
    }


    /**
     * 判定是否要验证登录
     *
     * @param url 访问url
     * @return 是否要验证
     */
    private boolean isAuthUrl(String url) {
        return PATH_MATCHER.match(AUTH_URIS, url)
                && Stream.of(IGNORE_URIS)
                .noneMatch(ignore -> PATH_MATCHER.match(ignore, url));
    }
}
