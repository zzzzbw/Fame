package com.zbw.fame.service;

import com.github.pagehelper.Page;
import com.zbw.fame.model.domain.Article;
import com.zbw.fame.model.param.ArticleParam;
import com.zbw.fame.model.dto.Archive;

import java.util.List;

/**
 * 文章 Service 接口
 *
 * @author zbw
 * @since 2017/8/21 22:01
 */
public interface ArticleService {

    /**
     * 根据条件分页查询文章
     *
     * @param page  当前页面
     * @param limit 每页数量
     * @param param 查询条件
     * @return Page<Article>
     */
    Page<Article> getArticles(Integer page, Integer limit, ArticleParam param);


    /**
     * 根据条件查询指定id文章
     *
     * @param param 查询条件
     * @return Article
     */
    Article getArticle(ArticleParam param);


    /**
     * 保存或更新文章
     *
     * @param article 文章entity
     * @return Integer
     */
    Integer saveArticle(Article article);

    /**
     * 更新文章
     *
     * @param articles 文章entity
     * @return boolean
     */
    boolean updateArticle(Article articles);

    /**
     * 根据id删除文章
     *
     * @param id 文章id
     * @return boolean
     */
    boolean deleteArticle(Integer id);

    /**
     * 文章数量
     *
     * @return Integer
     */
    Integer count();

    /**
     * 获取归档信息
     *
     * @return List<Archive>
     */
    List<Archive> getArchives();


    /**
     * 保存或更新自定义页面
     *
     * @param page 页面entity
     * @return Integer
     */
    Integer savePage(Article page);

    /**
     * 根据id删除自定义页面
     *
     * @param id 页面id
     * @return boolean
     */
    boolean deletePage(Integer id);
}
