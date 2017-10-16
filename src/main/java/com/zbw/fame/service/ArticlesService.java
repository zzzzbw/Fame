package com.zbw.fame.service;

import com.zbw.fame.dto.Page;
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

    boolean updateArticle(Articles articles);

    boolean deleteArticle(Integer id);

    List<Page> getPages(Integer page);

    Page getPage(String title);

    Integer count();

}
