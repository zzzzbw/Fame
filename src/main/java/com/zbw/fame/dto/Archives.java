package com.zbw.fame.dto;

import com.zbw.fame.model.Articles;

import java.util.Date;
import java.util.List;

/**
 * 归档 Dto
 *
 * @auther zbw
 * @create 2017/9/21 11:24
 */
public class Archives {

    private String dateStr;

    private Date date;

    private Integer count;

    private List<Articles> articles;

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

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
        return "Archives{" +
                "dateStr='" + dateStr + '\'' +
                ", date=" + date +
                ", count='" + count + '\'' +
                ", articles=" + articles +
                '}';
    }
}
