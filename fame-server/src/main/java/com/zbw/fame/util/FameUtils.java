package com.zbw.fame.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.options.MutableDataSet;
import com.zbw.fame.exception.NotLoginException;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.model.domain.User;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
    public static void setLoginUser(User user) {
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
    public static User getLoginUser() {
        HttpSession session = getSession();
        return (User) Optional.ofNullable(session.getAttribute(FameConst.USER_SESSION_KEY))
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
     * 根据字段倒叙排序
     *
     * @param properties 排序字段列表
     * @return {@link Sort}
     */
    public static Sort sortDescBy(List<String> properties) {
        return sortDescBy(properties.toArray(new String[0]));
    }

    /**
     * 根据字段倒叙排序
     *
     * @param properties 字段
     * @return {@link Sort}
     */
    public static Sort sortDescBy(String... properties) {
        return Sort.by(Sort.Direction.DESC, properties);
    }


    /**
     * 根据id倒叙排序
     *
     * @return {@link Sort}
     */
    public static Sort sortDescById() {
        return sortDescBy("id");
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
        str = str.toUpperCase();
        flag = flag.toUpperCase();
        return str.indexOf(flag);
    }

    /**
     * 获取泛型类
     *
     * @param clz 类
     * @return 泛型类
     */
    public static Class<?> getGenericClass(Class<?> clz) {
        ParameterizedType parameterizedType = (ParameterizedType) clz.getGenericSuperclass();
        return (Class<?>) parameterizedType.getActualTypeArguments()[0];
    }


    /**
     * 转换String到对应数据类型
     *
     * @param value String值
     * @param type  数据类型
     * @param <T>   T
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public static <T> T convertStringToType(String value, Class<?> type) {
        if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            return (T) Boolean.valueOf(value);
        } else if (type.equals(Integer.class) || type.equals(int.class)) {
            return (T) Integer.valueOf(value);
        } else if (type.equals(Byte.class) || type.equals(byte.class)) {
            return (T) Byte.valueOf(value);
        } else if (type.equals(Short.class) || type.equals(short.class)) {
            return (T) Short.valueOf(value);
        } else if (type.equals(Long.class) || type.equals(long.class)) {
            return (T) Long.valueOf(value);
        } else if (type.equals(Float.class) || type.equals(float.class)) {
            return (T) Float.valueOf(value);
        } else if (type.equals(Double.class) || type.equals(double.class)) {
            return (T) Double.valueOf(value);
        } else if (type.equals(Character.class) || type.equals(char.class)) {
            return (T) Character.valueOf(value.charAt(0));
        }

        return (T) value;
    }

    /**
     * 转换数据类型
     *
     * @param source    源对象
     * @param targetClz 转换目标对象类
     * @return 转换后对象
     */
    public static <T> T convertTo(Object source, Class<T> targetClz) {
        if (null == source) {
            return null;
        }
        T targetInstance;
        try {
            Constructor<T> tConstructor = ReflectionUtils.accessibleConstructor(targetClz);
            targetInstance = tConstructor.newInstance();
            copyPropertiesIgnoreNull(source, targetInstance);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new TipException(e);
        }
        return targetInstance;
    }

    /**
     * 复制非Null的属性
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyPropertiesIgnoreNull(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    /**
     * 获取Null属性名称
     *
     * @param source 对象
     * @return Null属性名称
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
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
        if (ObjectUtils.isEmpty(fileName)) {
            return "";
        }
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return fileName;
        }
        return fileName.substring(0, index);
    }


    /**
     * 返回文件后缀
     *
     * @param fileName 文件名
     * @return 文件后缀
     */
    public static String getFileSuffix(String fileName) {
        if (ObjectUtils.isEmpty(fileName)) {
            return "";
        }
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        return fileName.substring(index + 1);
    }

    /**
     * 压缩图片
     *
     * @param source       源文件
     * @param target       目标文件
     * @param imageQuality 压缩图片质量
     */
    public static void compressImage(File source, File target, float imageQuality) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        ImageWriter imageWriter = null;
        ImageOutputStream imageOutputStream = null;
        try {
            inputStream = new FileInputStream(source);
            outputStream = new FileOutputStream(target);
            // Create the buffered image
            BufferedImage bufferedImage = ImageIO.read(inputStream);

            // Get image writers
            Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName("jpg");

            if (!imageWriters.hasNext()) {
                throw new IllegalStateException("Writers Not Found!!");
            }

            imageWriter = imageWriters.next();
            imageOutputStream = ImageIO.createImageOutputStream(outputStream);
            imageWriter.setOutput(imageOutputStream);

            ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();

            // Set the compress quality metrics
            imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            imageWriteParam.setCompressionQuality(imageQuality);

            // Created image
            imageWriter.write(null, new IIOImage(bufferedImage, null, null), imageWriteParam);
        } catch (IOException e) {
            throw new TipException(e);
        } finally {
            // close all streams
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (imageOutputStream != null) {
                    imageOutputStream.close();
                }
                if (imageWriter != null) {
                    imageWriter.dispose();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
