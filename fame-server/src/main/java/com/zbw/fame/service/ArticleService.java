package com.zbw.fame.service;

import com.zbw.fame.model.query.ArticleQuery;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 文章 Service 接口
 *
 * @author zbw
 * @since 2017/8/21 22:01
 */
public interface ArticleService<ARTICLE> {

    /**
     * 分页查询前端文章
     *
     * @param page  当前页面
     * @param limit 每页数量
     * @param sort  排序
     * @return Page<ARTICLE>
     */
    Page<ARTICLE> pageFrontArticle(Integer page, Integer limit, List<String> sort);

    /**
     * 根据id获取前端文章
     *
     * @param id 文章url
     * @return ARTICLE
     */
    ARTICLE getFrontArticle(Integer id);

    /**
     * 分页查询后端文章
     *
     * @param page  当前页面
     * @param limit 每页数量
     * @param query 查询条件
     * @return Page<ARTICLE>
     */
    Page<ARTICLE> pageAdminArticle(Integer page, Integer limit, ArticleQuery query);

    /**
     * 根据id获取后端文章
     *
     * @param id 文章id
     * @return ARTICLE
     */
    ARTICLE getAdminArticle(Integer id);

    /**
     * 保存或更新文章
     *
     * @param article 文章entity
     * @return Integer
     */
    Integer save(ARTICLE article);

    /**
     * 根据id删除文章
     *
     * @param id 文章id
     */
    void delete(Integer id);

    /**
     * 文章数量
     *
     * @return Integer
     */
    long count();
}
