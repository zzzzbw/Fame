package com.zbw.fame.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbw.fame.model.entity.Article;
import com.zbw.fame.model.entity.ArticleCategory;
import com.zbw.fame.model.entity.Category;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author by zzzzbw
 * @since 2021/03/19 11:09
 */
public interface ArticleCategoryService extends IService<ArticleCategory> {

    /**
     * 根据文章Id获取分类
     *
     * @param articleId
     * @return
     */
    Category getCategoryByArticleId(Integer articleId);

    /**
     * 根据分类id删除关联
     *
     * @param categoryId
     */
    void deleteByCategoryId(Integer categoryId);

    /**
     * 根据文章id删除关联
     *
     * @param articleId
     */
    void deleteByArticleId(Integer articleId);

    /**
     * 获取分类下的文章列表
     * key: categoryId
     * value: 文章列表
     *
     * @param categoryIds
     * @param isFront
     * @return
     */
    Map<Integer, List<Article>> listArticleByCategoryIds(Set<Integer> categoryIds, boolean isFront);

    /**
     * 获取文章对应的分类
     *
     * @param articleIds
     * @return
     */
    Map<Integer, Category> listCategoryByArticleIds(Collection<Integer> articleIds);

    /**
     * 保存或更新文章分类关联
     *
     * @param articleId
     * @param categoryId
     */
    void createOrUpdate(Integer articleId, Integer categoryId);
}
