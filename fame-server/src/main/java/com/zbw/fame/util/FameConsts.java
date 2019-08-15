package com.zbw.fame.util;

import java.io.File;
import java.util.Map;

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
    Integer MAX_SUMMARY_COUNT = 255;

    /**
     * 默认预览分隔符
     */
    String DEFAULT_SUMMARY_FLAG = "<!--read more-->";

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
     * 文章点击量缓存数
     */
    Integer CACHE_ARTICLE_HITS_SAVE = 3;


    /**
     * 发送邮件的标题
     */
    String DEFAULT_EMAIL_TEMPLATE_SUBJECT = "来自Fame博客网站发送的邮件";

    /**
     * 获取发送给管理员的邮件内容
     *
     * @param params 填充的参数
     * @return 邮件内容
     */
    static String getEmailTemplateAdminContent(Map<String, String> params) {
        String emptyString = "";
        String websiteName = params.getOrDefault("websiteName", emptyString);
        String name = params.getOrDefault("name", emptyString);
        String content = params.getOrDefault("content", emptyString);
        String website = params.getOrDefault("website", emptyString);
        String articleId = params.getOrDefault("articleId", emptyString);

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<title>" + websiteName + " Email</title>" +
                "</head>" +
                "<body>" +
                "<h3>" + websiteName + "有新的评论回复</h3>" +
                "<p>来自" + name + "的评论：" + content + "</p>" +
                "<br>" +
                "<a href=\"" + website + "article/" + articleId + "\">查看详情</a>" +
                "</body>" +
                "</html>";
    }

    static String getEmailTemplateUserContent(Map<String, String> params) {
        String emptyString = "";
        String websiteName = params.getOrDefault("websiteName", emptyString);
        String name = params.getOrDefault("name", emptyString);
        String content = params.getOrDefault("content", emptyString);
        String website = params.getOrDefault("website", emptyString);
        String articleId = params.getOrDefault("articleId", emptyString);

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <title>" + websiteName + " Email</title>" +
                "</head>" +
                "<body>" +
                "<h3>你在" + websiteName + "的评论有人回复了,快去查看吧!</h3>" +
                "<p>来自" + name + "的评论：" + content + "</p>" +
                "<br>" +
                "<a href=\"" + website + "article/" + articleId + "\">查看详情</a>" +
                "</body>" +
                "</html>";
    }


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
