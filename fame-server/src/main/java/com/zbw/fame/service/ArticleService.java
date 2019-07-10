package com.zbw.fame.service;

import com.zbw.fame.model.domain.Article;
import com.zbw.fame.model.dto.Archive;
import com.zbw.fame.model.query.ArticleQuery;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 文章 Service 接口
 *
 * @author zbw
 * @since 2017/8/21 22:01
 */
public interface ArticleService {

    /**
     * 分页查询前端文章
     *
     * @param page  当前页面
     * @param limit 每页数量
     * @param sort  排序
     * @return Page<Article>
     */
    Page<Article> getFrontArticles(Integer page, Integer limit, List<String> sort);

    /**
     * 根据id获取前端文章
     *
     * @param id 文章id
     * @return Article
     */
    Article getFrontArticle(Integer id);

    /**
     * 分页查询后端文章
     *
     * @param page  当前页面
     * @param limit 每页数量
     * @param query 查询条件
     * @return Page<Article>
     */
    Page<Article> getAdminArticles(Integer page, Integer limit, ArticleQuery query);

    /**
     * 根据id获取后端文章
     *
     * @param id 文章id
     * @return Article
     */
    Article getAdminArticle(Integer id);

    /**
     * 保存或更新文章
     *
     * @param article 文章entity
     * @return Integer
     */
    Integer saveArticle(Article article);

    /**
     * 更新文章点击量
     *
     * @param articleId 文章id
     * @param hits      点击量
     * @return
     */
    boolean updateHits(Integer articleId, Integer hits);

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
    long count();

    /**
     * 获取归档信息
     *
     * @return List<Archive>
     */
    List<Archive> getArchives();


    /**
     * 根据title获取前端自定义页面
     *
     * @param title 页面标题
     * @return Article
     */
    Article getFrontPage(String title);

    /**
     * 分页查询后端自定义页面
     *
     * @param page  当前页面
     * @param limit 每页数量
     * @return Page<Article>
     */
    Page<Article> getAdminPages(Integer page, Integer limit);

    /**
     * 根据id获取后端页面
     *
     * @param id 文章id
     * @return Article
     */
    Article getAdminPage(Integer id);

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
