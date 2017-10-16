package com.zbw.fame.dto;

import java.util.Date;

/**
 * 自定义页面 Dto
 *
 * @auther zbw
 * @create 2017/10/17 12:04
 */
public class Page {

    //内容标题
    private String title;

    //内容生成时间
    private Date created;

    //内容修改时间
    private Date modified;

    //内容
    private String content;

    //内容所属用户id
    private Integer authorId;

    //内容状态
    private String status;

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

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Page{" +
                "title='" + title + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                ", content='" + content + '\'' +
                ", authorId=" + authorId +
                ", status='" + status + '\'' +
                '}';
    }
}
