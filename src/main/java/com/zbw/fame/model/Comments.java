package com.zbw.fame.model;

import javax.persistence.OrderBy;
import java.util.Date;

/**
 * 评论 Model
 *
 * @author zbw
 * @create 2018/1/19 16:27
 */
public class Comments extends BaseEntity {

    /**
     * 所属文章id
     */
    private Integer articleId;

    /**
     * 父评论id
     */
    private Integer pId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 昵称
     */
    private String name;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 网址
     */
    private String website;

    /**
     * 评论时间
     */
    @OrderBy("desc")
    private Date created;

    /**
     * 赞
     */
    private Integer agree;

    /**
     * 踩
     */
    private Integer disagree;

    /**
     * 评论ip
     */
    private String ip;

    /**
     * 评论agent
     */
    private String agent;

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Integer getAgree() {
        return agree;
    }

    public void setAgree(Integer agree) {
        this.agree = agree;
    }

    public Integer getDisagree() {
        return disagree;
    }

    public void setDisagree(Integer disagree) {
        this.disagree = disagree;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }
}
