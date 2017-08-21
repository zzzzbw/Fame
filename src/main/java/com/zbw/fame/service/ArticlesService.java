package com.zbw.fame.service;

import com.zbw.fame.model.Articles;

import java.util.List;

/**
 * 文章 Service 接口
 *
 * @auther zbw
 * @create 2017/8/21 22:01
 */
public interface ArticlesService {

    List<Articles> getArticles(Integer page);

    List<Articles> getContents(Integer page);

    Articles get(Integer id);

    Integer saveArticle(Articles article);

    boolean deleteArticle(Integer id);
}
