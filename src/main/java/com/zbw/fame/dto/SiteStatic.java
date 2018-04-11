package com.zbw.fame.dto;

/**
 * 网站设置 Dto
 *
 * @author zbw
 * @create 2017/10/15 21:48
 */
public class SiteStatic {

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
    private boolean emailSend;

    /**
     * 邮箱Host
     */
    private String emailHost;

    /**
     * 邮箱Port
     */
    private Integer emailPort;

    /**
     * 网站评论提醒邮箱
     */
    private String emailUsername;

    /**
     * 网站评论提醒邮箱密码
     */
    private String emailPassword;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public boolean isEmailSend() {
        return emailSend;
    }

    public void setEmailSend(boolean emailSend) {
        this.emailSend = emailSend;
    }

    public String getEmailHost() {
        return emailHost;
    }

    public void setEmailHost(String emailHost) {
        this.emailHost = emailHost;
    }

    public Integer getEmailPort() {
        return emailPort;
    }

    public void setEmailPort(Integer emailPort) {
        this.emailPort = emailPort;
    }

    public String getEmailUsername() {
        return emailUsername;
    }

    public void setEmailUsername(String emailUsername) {
        this.emailUsername = emailUsername;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }
}
