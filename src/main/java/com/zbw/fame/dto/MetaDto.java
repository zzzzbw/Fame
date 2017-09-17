package com.zbw.fame.dto;

import com.zbw.fame.model.Articles;
import com.zbw.fame.model.Metas;

import java.util.List;

/**
 * 属性 Dto
 *
 * @auther zbw
 * @create 2017/8/30 15:15
 */
public class MetaDto extends Metas{

    private Integer count;

    private List<Articles> articles;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Articles> getArticles() {
        return articles;
    }

    public void setArticles(List<Articles> articles) {
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
