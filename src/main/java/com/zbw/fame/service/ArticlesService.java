package com.zbw.fame.service;

import com.github.pagehelper.Page;
import com.zbw.fame.model.Articles;

/**
 * 文章 Service 接口
 *
 * @auther zbw
 * @create 2017/8/21 22:01
 */
public interface ArticlesService {

    Page<Articles> getArticles(Integer page, Integer limit);

    Page<Articles> getContents(Integer page, Integer limit);

    Articles get(Integer id);

    Integer saveArticle(Articles article);

    boolean updateArticle(Articles articles);

    boolean deleteArticle(Integer id);

    Page<Articles> getPages(Integer page, Integer limit);

    Articles getPage(Integer id);

    Integer savePage(Articles page);

    boolean deletePage(Integer id);

    Integer count();

}
