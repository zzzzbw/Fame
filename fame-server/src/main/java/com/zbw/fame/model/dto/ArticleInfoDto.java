package com.zbw.fame.model.dto;

import com.zbw.fame.model.entity.Article;
import lombok.Data;

import java.util.Date;

/**
 * @author by zzzzbw
 * @since 2021/03/10 14:39
 */
@Data
public class ArticleInfoDto {

    public ArticleInfoDto() {
    }

    public ArticleInfoDto(Article article) {
        super();
        this.id = article.getId();
        this.title = article.getTitle();
        this.publishTime = article.getPublishTime();
    }


    /**
     * id
     */
    private Integer id;

    /**
     * 内容标题
     */
    private String title;

    /**
     * 发布时间
     */
    private Date publishTime;
}
