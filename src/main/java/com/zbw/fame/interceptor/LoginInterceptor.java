package com.zbw.fame.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zbw.fame.model.Users;
import com.zbw.fame.util.ErrorCode;
import com.zbw.fame.util.FameConsts;
import com.zbw.fame.util.RestResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 登陆拦截器
 *
 * @auther zbw
 * @create 2017/9/25 22:57
 */
public class LoginInterceptor implements HandlerInterceptor {

    private static final String[] IGNORE_URIS = {"/admin/login"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //要设置跨域,不然拦截器会把跨域信息覆盖掉
        if (request.getHeader(HttpHeaders.ORIGIN) != null) {
            response.addHeader("Access-Control-Allow-Origin", "http://localhost:8010");
            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT, HEAD");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type");
            response.addHeader("Access-Control-Max-Age", "3600");
        }


        //若url为不拦截的url，则直接return true
        boolean flag = false;
        String url = request.getRequestURI();
        for (String param : IGNORE_URIS) {
            if (StringUtils.endsWithIgnoreCase(url, param)) {
                flag = true;
                return flag;
            }
        }

        if (!flag) {
            Users user = (Users) request.getSession().getAttribute(FameConsts.USER_SESSION_KEY);
            if (null == user) {
                PrintWriter out = response.getWriter();
                ObjectMapper mapper = new ObjectMapper();
                out.print(mapper.writeValueAsString(RestResponse.fail(ErrorCode.NOT_LOGIN.getCode(), ErrorCode.NOT_LOGIN.getMsg())));
                out.flush();
                return false;
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
