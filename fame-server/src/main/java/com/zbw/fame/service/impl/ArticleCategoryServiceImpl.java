package com.zbw.fame.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbw.fame.mapper.ArticleCategoryMapper;
import com.zbw.fame.model.entity.Article;
import com.zbw.fame.model.entity.ArticleCategory;
import com.zbw.fame.model.entity.BaseEntity;
import com.zbw.fame.service.ArticleCategoryService;
import com.zbw.fame.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author by zzzzbw
 * @since 2021/03/19 11:09
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class ArticleCategoryServiceImpl extends ServiceImpl<ArticleCategoryMapper, ArticleCategory> implements ArticleCategoryService {

    private final ArticleService articleService;

    @Override
    public void deleteByCategoryId(Integer categoryId) {
        lambdaUpdate()
                .eq(ArticleCategory::getCategoryId, categoryId)
                .remove();
    }

    @Override
    public Map<Integer, List<Article>> listArticleByCategoryIds(Set<Integer> categoryIds, boolean isFront) {
        List<ArticleCategory> articleCategories = lambdaQuery()
                .in(ArticleCategory::getCategoryId, categoryIds)
                .list();

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
}
