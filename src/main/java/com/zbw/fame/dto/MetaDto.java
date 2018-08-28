package com.zbw.fame.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zbw.fame.model.Metas;

import java.util.List;

/**
 * 属性 Dto
 *
 * @author zbw
 * @since 2017/8/30 15:15
 */
public class MetaDto extends Metas {

    private Integer count;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ArticleInfoDto> articles;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<ArticleInfoDto> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleInfoDto> articles) {
        this.articles = articles;
    }

    @Override
    public String toString() {
        return "MetaDto{" +
                "count=" + count +
                ", articles=" + articles +
                '}';
    }
}
