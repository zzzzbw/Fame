package com.zbw.fame.config;

import com.zbw.fame.dto.SiteConfig;
import com.zbw.fame.util.FameConsts;
import com.zbw.fame.util.SystemCache;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Fame 博客配置
 *
 * @author zbw
 * @since 2018/12/18 20:24
 */
@Data
@Component
@ConfigurationProperties(prefix = "fame.config")
public class FameConfig {

    @PostConstruct
    public void init() {
        SiteConfig config = SiteConfig.builder()
                .title(title)
                .description(description)
                .keywords(keywords)
                .emailSend(sendMail)
                .emailHost(mail.host)
                .emailPort(mail.port)
                .emailUsername(mail.username)
                .emailPassword(mail.password)
                .build();
        SystemCache.instance().put(FameConsts.CACHE_SITE_CONFIG, config);
    }

    /**
     * 网站title
     */
    private String title;

    /**
     * 网站描述
     */
    private String description;

    /**
     * 网站keywords
     */
    private String keywords;

    /**
     * 是否设置邮箱提醒
     */
    private boolean sendMail;

    /**
     * 邮件发送配置
     */
    private Mail mail;

    @Data
    public static class Mail {

        /**
         * 邮箱Host
         */
        private String host;

        /**
         * 邮箱Port
         */
        private int port;

        /**
         * 网站评论提醒邮箱
         */
        private String username;

        /**
         * 网站评论提醒邮箱密码
         */
        private String password;
    }
}
