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
}
