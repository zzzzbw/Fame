package com.zbw.fame.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbw.fame.mapper.ArticleTagMapper;
import com.zbw.fame.model.entity.Article;
import com.zbw.fame.model.entity.ArticleTag;
import com.zbw.fame.model.entity.BaseEntity;
import com.zbw.fame.service.ArticleServiceNew;
import com.zbw.fame.service.ArticleTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author by zzzzbw
 * @since 2021/03/15 16:53
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

    private final ArticleServiceNew articleServiceNew;

    @Override
    public Map<Integer, List<Article>> listArticleByTagIds(Collection<Integer> tagIds, boolean isFront) {
        List<ArticleTag> articleTags = lambdaQuery()
                .in(ArticleTag::getTagId, tagIds)
                .list();

        Set<Integer> articleIds = articleTags.stream()
                .map(ArticleTag::getArticleId)
                .collect(Collectors.toSet());
        Map<Integer, Article> articleMap = articleServiceNew.listByIds(articleIds, isFront)
                .stream()
                .collect(Collectors.toMap(BaseEntity::getId, article -> article, (o1, o2) -> o1));

        return articleTags
                .stream()
                .collect(Collectors.groupingBy(ArticleTag::getTagId,
                        Collectors.mapping(articleTag -> {
                            Integer articleId = articleTag.getArticleId();
                            return articleMap.get(articleId);
                        }, Collectors.toList())));
    }
}
