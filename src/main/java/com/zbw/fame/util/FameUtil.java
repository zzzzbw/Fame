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
 * @author zbw
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
     * 获取ip
     *
     * @return
     */
    public static String getIp() {
        String ip = getRequest().getHeader("x-forwarded-for");
        String unknown = "unknown";
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = getRequest().getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = getRequest().getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = getRequest().getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取文章预览
     *
     * @return
     */
    public static String getPreView(String content) {
        int index = FameUtil.ignoreCaseIndexOf(content, FameConsts.PREVIEW_FLAG);
        if (-1 == index) {
            index = content.length() > FameConsts.MAX_PREVIEW_COUNT ? FameConsts.MAX_PREVIEW_COUNT : content.length();
        }
        return content.substring(0, index);
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

    /**
     * 忽略大小写的indexOf
     *
     * @param str
     * @param flag
     * @return
     */
    public static int ignoreCaseIndexOf(String str, String flag) {
        str = str.toUpperCase();
        flag = flag.toUpperCase();
        return str.indexOf(flag);
    }
}
