package com.zbw.fame.util;

import com.zbw.fame.model.Users;
import org.pegdown.PegDownProcessor;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 公用工具类
 *
 * @auther zbw
 * @create 2017/7/9 22:08
 */
public class FameUtil {

    /**
     * 获取session中的users对象
     *
     * @return
     */
    public static Users getLoginUser() {
        HttpSession session = getSession();
        if (null == session) {
            return null;
        }
        Users user = (Users) session.getAttribute(FameConsts.USER_SESSION_KEY);
        return user;
    }

    /**
     * 获取session
     *
     * @return
     */
    public static HttpSession getSession() {
        HttpSession session = null;
        try {
            session = getRequest().getSession();
        } catch (Exception e) {
        }
        return session;
    }

    /**
     * 获取request
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs.getRequest();
    }

    /**
     * markdown转html
     *
     * @param md
     * @return
     */
    public static String mdToHtml(String md) {
        if (StringUtils.isEmpty(md)) {
            return "";
        }

        PegDownProcessor pdp = new PegDownProcessor(Integer.MAX_VALUE);
        return pdp.markdownToHtml(md);
    }
}
