package com.zbw.fame.service;

import com.github.pagehelper.Page;
import com.zbw.fame.model.Articles;

/**
 * 文章 Service 接口
 *
 * @author zbw
 * @create 2017/8/21 22:01
 */
public interface ArticlesService {

    /**
     * 获取所有文章
     *
     * @param page  当前页面
     * @param limit 每页数量
     * @return Page<Articles>
     */
    Page<Articles> getArticles(Integer page, Integer limit);

    /**
     * 获取已发布的文章
     *
     * @param page  当前页面
     * @param limit 每页数量
     * @return Page<Articles>
     */
    Page<Articles> getContents(Integer page, Integer limit);

    /**
     * 根据id获取文章
     *
     * @param id 文章id
     * @return Articles
     */
    Articles get(Integer id);

    /**
     * 保存或更新文章
     *
     * @param article 文章entity
     * @return Integer
     */
    Integer saveArticle(Articles article);

    /**
     * 更新文章
     *
     * @param articles 文章entity
     * @return boolean
     */
    boolean updateArticle(Articles articles);

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
     * 获取所有自定义页面
     *
     * @param page  当前页面
     * @param limit 每页数量
     * @return Page<Articles>
     */
    Page<Articles> getPages(Integer page, Integer limit);

    /**
     * 根据title获取自定义页面
     *
     * @param title 页面title
     * @return Articles
     */
    Articles getPage(String title);

    /**
     * 根据id获取自定义页面
     *
     * @param id 页面id
     * @return Articles
     */
    Articles getPage(Integer id);

    /**
     * 保存或更新自定义页面
     *
     * @param page 页面entity
     * @return Integer
     */
    Integer savePage(Articles page);

    /**
     * 根据id删除自定义页面
     *
     * @param id 页面id
     * @return boolean
     */
    boolean deletePage(Integer id);


}
