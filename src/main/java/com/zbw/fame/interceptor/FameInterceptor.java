package com.zbw.fame.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zbw.fame.model.Users;
import com.zbw.fame.service.LogsService;
import com.zbw.fame.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Fame 拦截器
 *
 * @author zbw
 * @create 2017/10/11 14:10
 */
@Component
public class FameInterceptor implements HandlerInterceptor {

    private static final String AUTH_URIS = "/admin";

    private static final String[] IGNORE_URIS = {"/admin/login"};

    private static final Logger logger = LoggerFactory.getLogger(FameInterceptor.class);

    private SystemCache cache = SystemCache.instance();

    @Autowired
    private LogsService logsService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String url = request.getRequestURI();
        String ip = FameUtil.getIp();

        logger.info("用户访问地址: {}, ip地址: {}", url, ip);

        final String getMethod = "GET";
        if (getMethod.equals(request.getMethod().toUpperCase())) {
            this.updateClick(url);
        }

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
                    // 要设置跨域，不然输出信息没有
                    if (request.getHeader(HttpHeaders.ORIGIN) != null) {
                        response.setHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));
                        response.setHeader("Access-Control-Allow-Credentials", "true");
                        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
                        response.setHeader("Access-Control-Max-Age", "3600");
                        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
                    }
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

    /**
     * 更新访问量
     *
     * @param url
     */
    private void updateClick(String url) {
        String route = url.split("/")[1];
        final String adminRoute = "admin";
        if (adminRoute.equals(route)) {
            return;
        }
        Integer chits = cache.get(FameConsts.CACHE_ROUTE_VISIT, route);
        chits = null == chits ? 1 : chits + 1;
        if (chits >= FameConsts.CACHE_ROUTE_VISIT_SAVE) {
            logsService.save(Types.LOG_ACTION_VISIT, chits.toString(), route + Types.LOG_MESSAGE_VISIT, Types.LOG_TYPE_VISIT);
            cache.del(FameConsts.CACHE_ROUTE_VISIT, route);
        } else {
            cache.put(FameConsts.CACHE_ROUTE_VISIT, route, chits);
        }
    }
}
