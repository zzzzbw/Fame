package com.zbw.fame.util;

import java.io.File;

/**
 * 常量工具类
 *
 * @author zbw
 * @since 2017/7/9 22:21
 */
public interface FameConsts {

    /**
     * 登陆用户session key
     */
    String USER_SESSION_KEY = "login_user";

    /**
     * md5加密盐值
     */
    String MD5_SLAT = "riopwhjrv123bnopiw234q2ec";

    /**
     * 默认分页大小
     */
    String PAGE_SIZE = "10";

    /**
     * 文章标题最大字数
     */
    Integer MAX_TITLE_COUNT = 255;

    /**
     * 文章内容最大字数
     */
    Integer MAX_CONTENT_COUNT = 200000;

    /**
     * 默认预览字数
     */
    Integer MAX_PREVIEW_COUNT = 255;

    /**
     * 默认预览标记
     */
    String PREVIEW_FLAG = "<!--read more-->";

    /**
     * 评论最大字数
     */
    Integer MAX_COMMENT_CONTENT_COUNT = 1000;

    /**
     * 评论名称最大字数
     */
    Integer MAX_COMMENT_NAME_COUNT = 255;

    /**
     * 评论邮件最大字数
     */
    Integer MAX_COMMENT_EMAIL_COUNT = 255;

    /**
     * 评论网址最大字数
     */
    Integer MAX_COMMENT_WEBSITE_COUNT = 255;


    /**
     * 文章缓存key
     */
    String CACHE_ARTICLE_HITS = "cache_article_hits";

    /**
     * 文章点击量缓存数
     */
    Integer CACHE_ARTICLE_HITS_SAVE = 3;


    /**
     * 发送邮件的标题
     */
    String EMAIL_TEMPLATE_DEFAULT_SUBJECT = "来自Fame博客网站发送的邮件";

    /**
     * Fame保存目录
     */
    String FAME_HOME = ".fame" + File.separator;

    /**
     * 用户目录
     */
    String USER_HOME = System.getProperties().getProperty("user.home") + File.separator;

    /**
     * 上传文件的路径
     */
    String UPLOAD_DIR = "upload" + File.separator;

    /**
     * 媒体文件夹路径
     */
    String MEDIA_DIR = UPLOAD_DIR + "media" + File.separator;

    /**
     * 媒体缩略图后缀
     */
    String MEDIA_THUMBNAIL_SUFFIX = "_thumbnail";

}
