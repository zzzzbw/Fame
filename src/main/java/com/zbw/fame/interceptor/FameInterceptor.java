package com.zbw.fame.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zbw.fame.model.Users;
import com.zbw.fame.service.LogsService;
import com.zbw.fame.util.ErrorCode;
import com.zbw.fame.util.FameConsts;
import com.zbw.fame.util.FameUtil;
import com.zbw.fame.util.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Fame 拦截器
 *
 * @auther zbw
 * @create 2017/10/11 14:10
 */
public class FameInterceptor implements HandlerInterceptor {

    private static final String AUTH_URIS = "/admin";

    private static final String[] IGNORE_URIS = {"/admin/login"};

    private static final Logger logger = LoggerFactory.getLogger(FameInterceptor.class);

    @Autowired
    private LogsService logsService;

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

        String url = request.getRequestURI();
        String ip = FameUtil.getIp();

        logger.info("用户访问地址: {}, 来源地址: {}", url, ip);


        if (url.contains(AUTH_URIS)) {
            boolean auth = true;
            //登录拦截忽略url
            for (String param : IGNORE_URIS) {
                if (StringUtils.endsWithIgnoreCase(url, param)) {
                    auth = false;
                }
            }
            //登录拦截
            if (auth) {
                Users user = (Users) request.getSession().getAttribute(FameConsts.USER_SESSION_KEY);
                if (null == user) {
                    PrintWriter out = response.getWriter();
                    ObjectMapper mapper = new ObjectMapper();
                    out.print(mapper.writeValueAsString(RestResponse.fail(ErrorCode.NOT_LOGIN.getCode(), ErrorCode.NOT_LOGIN.getMsg())));
                    out.flush();
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception {

    }
}
