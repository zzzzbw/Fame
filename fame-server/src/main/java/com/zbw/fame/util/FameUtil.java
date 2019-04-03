package com.zbw.fame.util;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.options.MutableDataSet;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.model.domain.User;
import org.springframework.http.HttpHeaders;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;

/**
 * 公用工具类
 *
 * @author zbw
 * @since 2017/7/9 22:08
 */
public class FameUtil {

    /**
     * markdown 扩展设置
     */
    private static final MutableDataSet options = new MutableDataSet();

    static {
        options.setFrom(ParserEmulationProfile.MARKDOWN);
        options.set(Parser.EXTENSIONS, Collections.singletonList(TablesExtension.create()));
    }

    /**
     * markdown 解析器
     */
    private static final Parser PARSER = Parser.builder(options).build();

    /**
     * markdown html 解析器
     */
    private static final HtmlRenderer HTML_RENDER = HtmlRenderer.builder(options).build();

    /**
     * 禁止实例化
     */
    private FameUtil() {
        throw new TipException("Constructor not allow");
    }


    /**
     * 获取session中的users对象
     *
     * @return session中的用户
     */
    public static User getLoginUser() {
        HttpSession session = getSession();
        if (null == session) {
            return null;
        }
        return (User) session.getAttribute(FameConsts.USER_SESSION_KEY);
    }

    /**
     * 获取session
     *
     * @return {@link HttpSession}
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
     * @return {@link HttpServletRequest}
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs.getRequest();
    }

    /**
     * 获取域名
     *
     * @return 域名字符串
     */
    public static String getDomain() {
        StringBuffer url = getRequest().getRequestURL();
        return url.delete(url.length() - getRequest().getRequestURI().length(), url.length()).append("/").toString();
    }

    /**
     * 获取ip
     *
     * @return 访问ip
     */
    public static String getIp() {
        String unknown = "unknown";
        // nginx反向代理IP
        String ip = getRequest().getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = getRequest().getHeader("x-forwarded-for");
        }
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
     * 获取agent
     *
     * @return User-Agent信息
     */
    public static String getAgent() {
        return getRequest().getHeader(HttpHeaders.USER_AGENT);

    }

    /**
     * 获取字符串md5值(加盐)
     *
     * @param str 字符串
     * @return 加密的字符串
     */
    public static String getMd5(String str) {
        String base = str + FameConsts.MD5_SLAT;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

    /**
     * 获取文章预览
     *
     * @return 截取的预览字符串
     */
    public static String getSummary(String content) {
        int index = FameUtil.ignoreCaseIndexOf(content, FameConsts.PREVIEW_FLAG);
        if (-1 == index) {
            index = content.length() > FameConsts.MAX_PREVIEW_COUNT ? FameConsts.MAX_PREVIEW_COUNT : content.length();
        }
        return content.substring(0, index);
    }


    /**
     * markdown转html
     *
     * @param md markdown字符串
     * @return html字符串
     */
    public static String mdToHtml(String md) {
        if (StringUtils.isEmpty(md)) {
            return "";
        }

        Node document = PARSER.parse(md);
        return HTML_RENDER.render(document);
    }

    /**
     * 根据条件转换markdown内容
     *
     * @param content   markdown内容
     * @param isSummary 是否为摘要
     * @param isHtml    是否为 html 格式
     */
    public static String contentTransform(String content, boolean isSummary, boolean isHtml) {
        if (isSummary || isHtml) {
            if (isSummary) {
                content = FameUtil.getSummary(content);
            }
            if (isHtml) {
                content = FameUtil.mdToHtml(content);
            }
        }
        return content;
    }

    /**
     * 忽略大小写的indexOf
     *
     * @param str  被匹配的字符串
     * @param flag 匹配字符串
     * @return 所在的位置, 没有匹配返回-1
     */
    public static int ignoreCaseIndexOf(String str, String flag) {
        str = str.toUpperCase();
        flag = flag.toUpperCase();
        return str.indexOf(flag);
    }
}
