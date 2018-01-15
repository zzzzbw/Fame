package com.zbw.fame.util;

import com.zbw.fame.exception.TipException;

/**
 * 常量工具类
 *
 * @author zbw
 * @create 2017/7/9 22:21
 */
public class FameConsts {

    /**
     * 禁止实例化
     */
    private FameConsts() {
        throw new TipException("Constructor not allow");
    }

    /**
     * 登陆用户session key
     */
    public static final String USER_SESSION_KEY = "login_user";

    /**
     * md5加密盐值
     */
    public static final String MD5_SLAT = "riopwhjrv123bnopiw234q2ec";

    /**
     * 默认分页大小
     */
    public static final String PAGE_SIZE = "10";

    /**
     * 文章标题最大字数
     */
    public static final Integer MAX_TITLE_COUNT = 255;

    /**
     * 文章内容最大字数
     */
    public static final Integer MAX_CONTENT_COUNT = 200000;

    /**
     * 默认预览字数
     */
    public static final Integer MAX_PREVIEW_COUNT = 255;

    /**
     * 默认预览标记
     */
    public static final String PREVIEW_FLAG = "<!--read more-->";

    /**
     * 评论最大字数
     */
    public static final Integer MAX_COMMENT_CONTENT_COUNT = 1000;

    /**
     * 评论名称最大字数
     */
    public static final Integer MAX_COMMENT_NAME_COUNT = 255;

    /**
     * 评论邮件最大字数
     */
    public static final Integer MAX_COMMENT_EMAIL_COUNT = 255;

    /**
     * 评论网址最大字数
     */
    public static final Integer MAX_COMMENT_WEBSITE_COUNT = 255;


    /**
     * 文章缓存key
     */
    public static final String CACHE_ARTICLE_HITS = "cache_article_hits";

    /**
     * 文章点击量缓存数
     */
    public static final Integer CACHE_ARTICLE_HITS_SAVE = 10;

    /**
     * 网站设置缓存key
     */
    public static final String CACHE_SITESTATIC = "cache_sitestatic";

    /**
     * 网站默认title
     */
    public static final String SITESTATIC_DEFAULT_TITLE = "Fame博客";

    /**
     * 网站默认description
     */
    public static final String SITESTATIC_DEFAULT_DESCRIPTION = "Fame博客";

    /**
     * 网站默认keywords
     */
    public static final String SITESTATIC_DEFAULT_KEYWORDS = "Fame,blog,博客";

}
