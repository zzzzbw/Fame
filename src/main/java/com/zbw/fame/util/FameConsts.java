package com.zbw.fame.util;

/**
 * 常量工具类
 *
 * @author zbw
 * @create 2017/7/9 22:21
 */
public class FameConsts {

    public static final String USER_SESSION_KEY = "login_user";

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
     * 文章缓存key
     */
    public static final String CACHE_ARTICLE_HITS = "cache_article_hits";

    /**
     * 文章点击量缓存数
     */
    public static final Integer CACHE_ARTICLE_HITS_SAVE = 10;

    /**
     * 访问量缓存key
     */
    public static final String CACHE_ROUTE_VISIT = "cache_route_hits";

    /**
     * 访问量缓存数
     */
    public static final Integer CACHE_ROUTE_VISIT_SAVE = 50;


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
