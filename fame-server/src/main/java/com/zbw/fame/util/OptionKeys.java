package com.zbw.fame.util;

import java.util.Arrays;
import java.util.List;

/**
 * SysOption 的key值
 *
 * @author zbw
 * @since 2019-05-21 00:04
 */
public interface OptionKeys {

    /**
     * 系统是否初始化
     */
    String FAME_INIT = "fame_init";

    /**
     * 博客名
     */
    String BLOG_NAME = "blog_name";

    /**
     * 博客网址
     */
    String BLOG_WEBSITE = "blog_website";

    /**
     * 博客页脚
     */
    String BLOG_FOOTER = "blog_footer";

    /**
     * SEO title
     */
    String META_TITLE = "meta_title";

    /**
     * SEO description
     */
    String META_DESCRIPTION = "meta_description";

    /**
     * SEO keywords
     */
    String META_KEYWORDS = "meta_keywords";


    /**
     * google 站长验证
     */
    String GOOGLE_SITE_VERIFICATION = "google_site_verification";

    /**
     * baidu 站长认证
     */
    String BAIDU_SITE_VERIFICATION = "baidu-site-verification";

    /**
     * 是否开启邮件提醒
     */
    String IS_EMAIL = "is_email";

    /**
     * 邮箱host
     */
    String EMAIL_HOST = "email_host";

    /**
     * 邮箱port
     */
    String EMAIL_PORT = "email_port";

    /**
     * 邮箱
     */
    String EMAIL_USERNAME = "email_username";

    /**
     * 邮箱密码
     */
    String EMAIL_PASSWORD = "email_password";


    /**
     * 传给前端的Option的keys
     */
    List<String> FRONT_OPTION_KEYS = Arrays.asList(
            BLOG_NAME,
            BLOG_WEBSITE,
            BLOG_FOOTER,
            META_TITLE,
            META_DESCRIPTION,
            META_KEYWORDS,
            GOOGLE_SITE_VERIFICATION,
            BAIDU_SITE_VERIFICATION);
}
