package com.zbw.fame.util;

/**
 * 常量工具类
 *
 * @auther zbw
 * @create 2017/7/9 22:21
 */
public class FameConsts {

    public static String USER_SESSION_KEY = "login_user";

    /**
     * 默认分页大小
     */
    public static Integer PAGE_SIZE = 10;

    /**
     * 文章标题最大字数
     */
    public static Integer MAX_TITLE_COUNT = 255;

    /**
     * 文章内容最大字数
     */
    public static Integer MAX_CONTENT_COUNT = 200000;

    /**
     * 默认预览字数
     */
    public static Integer MAX_PREVIEW_COUNT = 255;

    /**
     * 默认预览标记
     */
    public static String PREVIEW_FLAG = "<!--read more-->";


    /**
     * 文章缓存key
     */
    public static String CACHE_ARTICLE_HITS = "cache_article_hits";

    /**
     * 文章点击量缓存数
     */
    public static Integer CACHE_ARTICLE_HITS_SAVE = 10;

    /**
     * 访问量缓存key
     */
    public static String CACHE_ROUTE_VISIT = "cache_route_hits";

    /**
     * 访问量缓存数
     */
    public static Integer CACHE_ROUTE_VISIT_SAVE = 50;

}
