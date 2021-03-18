package com.zbw.fame.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zbw.fame.model.dto.ArchiveDto;
import com.zbw.fame.model.dto.ArticleInfoDto;
import com.zbw.fame.model.entity.Article;
import com.zbw.fame.model.param.ArticleQuery;

import java.util.List;

/**
 * @author by zzzzbw
 * @since 2021/03/09 15:49
 */
public interface ArticleServiceNew extends IService<Article> {


    /**
     * 分页查询前端文章
     *
     * @param current 当前页面
     * @param size    每页数量
     * @param sort    排序
     * @return Page<ARTICLE>
     */
    Page<Article> pageArticleFront(Integer current, Integer size, List<String> sort);

    /**
     * 根据id获取前端文章
     *
     * @param id 文章url
     * @return ARTICLE
     */
    Article getArticleFront(Integer id);

    /**
     * 分页查询后端文章
     *
     * @param current 当前页面
     * @param size    每页数量
     * @param query   查询条件
     * @return Page<ARTICLE>
     */
    Page<Article> pageArticleAdmin(Integer current, Integer size, ArticleQuery query);

    /**
     * 根据id获取后端文章
     *
     * @param id 文章id
     * @return ARTICLE
     */
    Article getArticleAdmin(Integer id);

    /**
     * 保存或更新文章
     *
     * @param article 文章entity
     * @return Integer
     */
    Integer createOrUpdate(Article article);

    /**
     * 根据id删除文章
     *
     * @param id 文章id
     */
    void delete(Integer id);


    /**
     * 访问文章
     *
     * @param id 文章id
     */
    void visitArticle(Integer id);


    /**
     * 增加文章点击量
     *
     * @param id           文章id
     * @param increaseHits 点击量
     */
    void increaseHits(Integer id, Integer increaseHits);


    /**
     * 获取归档信息
     *
     * @return List<Archive>
     */
    List<ArchiveDto> getArchives();

    /**
     * 获取顶栏文章
     *
     * @return List<ArticleInfoDto>
     */
    List<ArticleInfoDto> listArticleHeader();
}
