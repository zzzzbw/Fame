package com.zbw.fame.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbw.fame.mapper.ArticleCategoryMapper;
import com.zbw.fame.model.entity.Article;
import com.zbw.fame.model.entity.ArticleCategory;
import com.zbw.fame.model.entity.BaseEntity;
import com.zbw.fame.model.entity.Category;
import com.zbw.fame.service.ArticleCategoryService;
import com.zbw.fame.service.ArticleService;
import com.zbw.fame.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author by zzzzbw
 * @since 2021/03/19 11:09
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class ArticleCategoryServiceImpl extends ServiceImpl<ArticleCategoryMapper, ArticleCategory> implements ArticleCategoryService {

    private final CategoryService categoryService;

    private final ArticleService articleService;

    @Override
    public Category getCategoryByArticleId(Integer articleId) {
        ArticleCategory articleCategory = lambdaQuery()
                .eq(ArticleCategory::getArticleId, articleId)
                .one();
        if (null == articleCategory) {
            return null;
        }
        return categoryService.getById(articleCategory.getCategoryId());
    }

    @Override
    public void deleteByCategoryId(Integer categoryId) {
        lambdaUpdate()
                .eq(ArticleCategory::getCategoryId, categoryId)
                .remove();
    }

    @Override
    public void deleteByArticleId(Integer articleId) {
        lambdaUpdate()
                .eq(ArticleCategory::getArticleId, articleId)
                .remove();
    }

    @Override
    public Map<Integer, List<Article>> listArticleByCategoryIds(Set<Integer> categoryIds, boolean isFront) {
        List<ArticleCategory> articleCategories = lambdaQuery()
                .in(ArticleCategory::getCategoryId, categoryIds)
                .list();

        if (CollectionUtils.isEmpty(articleCategories)) {
            return Collections.emptyMap();
        }

        Set<Integer> articleIds = articleCategories.stream()
                .map(ArticleCategory::getArticleId)
                .collect(Collectors.toSet());

        Map<Integer, Article> articleMap = articleService.listByIds(articleIds, isFront)
                .stream()
                .collect(Collectors.toMap(BaseEntity::getId, article -> article, (o1, o2) -> o1));

        return articleCategories
                .stream()
                .collect(Collectors.groupingBy(ArticleCategory::getCategoryId,
                        Collectors.mapping(articleCategory -> {
                            Integer articleId = articleCategory.getArticleId();
                            return articleMap.get(articleId);
                        }, Collectors.toList())));
    }

    @Override
    public Map<Integer, Category> listCategoryByArticleIds(Collection<Integer> articleIds) {
        if (CollectionUtils.isEmpty(articleIds)) {
            return Collections.emptyMap();
        }
        List<ArticleCategory> articleCategories = lambdaQuery()
                .in(ArticleCategory::getArticleId, articleIds)
                .list();

        if (CollectionUtils.isEmpty(articleCategories)) {
            return Collections.emptyMap();
        }

        Set<Integer> categoryIds = articleCategories
                .stream()
                .map(ArticleCategory::getCategoryId)
                .collect(Collectors.toSet());

        Map<Integer, Category> categoryMap = categoryService.listByIds(categoryIds)
                .stream()
                .collect(Collectors.toMap(BaseEntity::getId, o -> o));

        Map<Integer, Category> map = new HashMap<>();
        for (ArticleCategory o : articleCategories) {
            if (map.put(o.getArticleId(), categoryMap.get(o.getCategoryId())) != null) {
                throw new IllegalStateException("Duplicate key");
            }
        }
        return map;
    }

    @Override
    public void createOrUpdate(Integer articleId, Integer categoryId) {
        Assert.notNull(articleId, "articleId can not be null!");

        // 删除原有关联
        lambdaUpdate()
                .eq(ArticleCategory::getArticleId, articleId)
                .remove();

        if (categoryId == null) {
            return;
        }
        // 重新关联
        ArticleCategory articleCategory = new ArticleCategory();
        articleCategory.setArticleId(articleId);
        articleCategory.setCategoryId(categoryId);
        save(articleCategory);

    }
}
