package com.zbw.fame.model;

import java.util.Date;

/**
 * 文章 Model
 *
 * @auther zbw
 * @create 2017/7/8 9:29
 */
public class Articles extends BaseEntity {

    //内容标题
    private String title;

    //内容生成时间
    private Date created;

    //内容修改时间
    private Date modified;

    //内容
    private String content;

    //内容所属用户id
    private Integer author_id;

    //点击量
    private Integer hits;

    //标签列表
    private String tags;

    //文章分类
    private String category;

    //内容状态
    private String status;

    // 是否允许评论
    private Boolean allow_comment;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(Integer author_id) {
        this.author_id = author_id;
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getAllow_comment() {
        return allow_comment;
    }

    public void setAllow_comment(Boolean allow_comment) {
        this.allow_comment = allow_comment;
    }
}
