package com.zbw.fame.dto;

import com.zbw.fame.model.BaseEntity;

/**
 * 文章信息Dto,用于一些列表页
 *
 * @author zbw
 * @since 2018/8/28 14:34
 */
public class ArticleInfoDto extends BaseEntity {
    /**
     * 内容标题
     */
    private String title;


    /**
     * 标签列表
     */
    private String tags;

    /**
     * 文章分类
     */
    private String category;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
