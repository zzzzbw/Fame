package com.zbw.fame.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.options.MutableDataSet;
import com.zbw.fame.exception.NotLoginException;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.model.dto.LoginUser;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

/**
 * 公用工具类
 *
 * @author zzzzbw
 * @since 2017/7/9 22:08
 */
@Slf4j
public class FameUtils {

    /**
     * {@code jackson} ObjectMapper
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * markdown 扩展设置
     */
    private static final MutableDataSet MARKDOWN_OPTIONS = new MutableDataSet();

    static {
        MARKDOWN_OPTIONS.setFrom(ParserEmulationProfile.MARKDOWN);
        MARKDOWN_OPTIONS.set(Parser.EXTENSIONS, Collections.singletonList(TablesExtension.create()));
    }

    /**
     * markdown 解析器
     */
    private static final Parser PARSER = Parser.builder(MARKDOWN_OPTIONS).build();

    /**
     * markdown html 解析器
     */
    private static final HtmlRenderer HTML_RENDER = HtmlRenderer.builder(MARKDOWN_OPTIONS).build();

    /**
     * 禁止实例化
     */
    private FameUtils() {
        throw new TipException("Constructor not allow");
    }


    /**
     * 设置User对象到登录缓存中
     *
     * @param user 登录用户
     */
    public static void setLoginUser(LoginUser user) {
        HttpSession session = getSession();
        session.setAttribute(FameConst.USER_SESSION_KEY, user);
    }

    /**
     * 清除登录中的用户
     */
    public static void clearLoginUser() {
        HttpSession session = getSession();
        session.removeAttribute(FameConst.USER_SESSION_KEY);
    }

    /**
     * 获取session中的User对象
     *
     * @return session中的用户
     */
    public static LoginUser getLoginUser() {
        HttpSession session = getSession();
        return (LoginUser) Optional.ofNullable(session.getAttribute(FameConst.USER_SESSION_KEY))
                .orElseThrow(NotLoginException::new);
    }

    /**
     * 获取session
     *
     * @return {@link HttpSession}
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 获取request
     *
     * @return {@link HttpServletRequest}
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Objects.requireNonNull(attrs).getRequest();
    }

    /**
     * 获取协议+域名
     *
     * @return 协议+域名字符串
     */
    public static String getDomain() {
        StringBuffer url = getRequest().getRequestURL();
        return url.delete(url.length() - getRequest().getRequestURI().length(), url.length()).toString();
    }

    /**
     * 获取域名
     *
     * @return 域名字符串
     */
    public static String getHostAddress() {
        InetAddress address;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            log.error("Get InetAddress error!", e);
            return "";
        }
        return address.getHostAddress();
    }

    /**
     * 获取ip
     *
     * @return 访问ip
     */
    public static String getIp() {
        String unknown = "unknown";
        String[] ipHeaders = new String[]{"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
        // nginx反向代理IP
        String ip;
        for (String header : ipHeaders) {
            ip = getRequest().getHeader(header);
            if (ip != null && ip.length() != 0 && !unknown.equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return getRequest().getRemoteAddr();
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
        String base = str + FameConst.MD5_SLAT;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

    /**
     * 获取文章预览
     *
     * @return 截取的预览字符串
     */
    public static String getSummary(String content, String flag) {
        int index = 0;
        if (!ObjectUtils.isEmpty(flag)) {
            index = FameUtils.ignoreCaseIndexOf(content, flag);
        }
        if (ObjectUtils.isEmpty(flag) || -1 == index) {
            index = content.length() > FameConst.MAX_SUMMARY_COUNT ? FameConst.MAX_SUMMARY_COUNT : content.length();
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
        if (ObjectUtils.isEmpty(md)) {
            return "";
        }

        Node document = PARSER.parse(md);
        return HTML_RENDER.render(document);
    }

    /**
     * 根据条件转换markdown内容
     *
     * @param content     markdown内容
     * @param isSummary   是否为摘要
     * @param isHtml      是否为 html 格式
     * @param summaryFlag 预览分隔符
     */
    public static String contentTransform(String content, boolean isSummary, boolean isHtml, String summaryFlag) {
        if (isSummary || isHtml) {
            if (isSummary) {
                content = FameUtils.getSummary(content, summaryFlag);
            }
            if (isHtml) {
                content = FameUtils.mdToHtml(content);
            }
        }
        return content;
    }


    /**
     * json转换为对象
     *
     * @param json json字符串
     * @param clz  对象Class
     * @param <T>  对象类型
     * @return 对象实体
     */
    @SneakyThrows
    public static <T> T jsonToObject(@NonNull String json, @NonNull Class<T> clz) {
        return OBJECT_MAPPER.readValue(json, clz);
    }

    /**
     * 对象转为json
     *
     * @param data 目标对象
     * @return json字符串
     */
    @SneakyThrows
    public static String objectToJson(@NonNull Object data) {
        return OBJECT_MAPPER.writeValueAsString(data);
    }


    /**
     * 忽略大小写的indexOf
     *
     * @param str  被匹配的字符串
     * @param flag 匹配字符串
     * @return 所在的位置, 没有匹配返回-1
     */
    public static int ignoreCaseIndexOf(String str, String flag) {
        return StrUtil.indexOfIgnoreCase(str, flag);
    }


    /**
     * 转换String到对应数据类型
     *
     * @param value String值
     * @param type  数据类型
     * @param <T>   T
     * @return 值
     */
    public static <T> T convertStrTo(String value, Class<T> type) {
        return Convert.convert(type, value);
    }

    /**
     * 转换数据类型
     *
     * @param source    源对象
     * @param targetClz 转换目标对象类
     * @return 转换后对象
     */
    public static <T> T convertTo(Object source, Class<T> targetClz) {
        return BeanUtil.toBean(source, targetClz);
    }

    /**
     * 复制非Null的属性
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyPropertiesIgnoreNull(Object source, Object target) {
        BeanUtil.copyProperties(source, target, CopyOptions.create().ignoreNullValue());
    }

    /**
     * 获取项目保存目录
     *
     * @return 项目目录文件
     */
    public static Path getFameDir() {
        Path dir = Paths.get(FameConst.USER_HOME, FameConst.FAME_HOME);
        if (!Files.exists(dir)) {
            try {
                Files.createDirectories(dir);
            } catch (IOException e) {
                throw new TipException(e);
            }
        }
        return dir;
    }

    /**
     * 获取文件文件名,不包括后缀
     *
     * @return 文件名
     */
    public static String getFileBaseName(String fileName) {
        return FileUtil.getPrefix(fileName);
    }


    /**
     * 返回文件后缀
     *
     * @param fileName 文件名
     * @return 文件后缀
     */
    public static String getFileSuffix(String fileName) {
        return FileUtil.getSuffix(fileName);
    }

    /**
     * 压缩图片
     *
     * @param source       源文件
     * @param target       目标文件
     * @param imageQuality 压缩图片质量
     */
    public static void compressImage(File source, File target, float imageQuality) {
        ImgUtil.scale(source, target, imageQuality);
    }
}
