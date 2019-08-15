package com.zbw.fame.service.impl;

import com.zbw.fame.exception.NotFoundException;
import com.zbw.fame.model.domain.Article;
import com.zbw.fame.model.enums.ArticleStatus;
import com.zbw.fame.model.query.ArticleQuery;
import com.zbw.fame.repository.ArticleRepository;
import com.zbw.fame.service.ArticleService;
import com.zbw.fame.service.OptionService;
import com.zbw.fame.util.FameUtil;
import com.zbw.fame.util.OptionKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * 文章 Service 实现类
 *
 * @author zbw
 * @since 2017/8/21 22:02
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public abstract class AbstractArticleServiceImpl<ARTICLE extends Article> implements ArticleService<ARTICLE> {

    public static final String ARTICLE_CACHE_NAME = "articles";

    protected final ArticleRepository<ARTICLE> articleRepository;

    protected final OptionService optionService;


    @Override
    @Cacheable(value = ARTICLE_CACHE_NAME, key = "'font_articles['+#page+':'+#limit+':'+#sort+']'")
    public Page<ARTICLE> pageFrontArticle(Integer page, Integer limit, List<String> sort) {
        Pageable pageable = PageRequest.of(page, limit, new Sort(Sort.Direction.DESC, sort));
        Page<ARTICLE> result = articleRepository.findAllByStatus(ArticleStatus.PUBLISH, pageable);

        String summaryFlag = optionService.get(OptionKeys.SUMMARY_FLAG);
        result.forEach(article -> {
            String content = FameUtil.contentTransform(article.getContent(), true, true, summaryFlag);
            article.setContent(content);
        });
        return result;
    }

    @Override
    @Cacheable(value = ARTICLE_CACHE_NAME, key = "'front_article['+#id+']'")
    public ARTICLE getFrontArticle(Integer id) {
        ARTICLE article = articleRepository.findByIdAndStatus(id, ArticleStatus.PUBLISH)
                .orElseThrow(() -> new NotFoundException(FameUtil.getGenericClass(getClass())));
        String content = FameUtil.contentTransform(article.getContent(), false, true, null);
        article.setContent(content);
        return article;
    }

    @Override
    public Page<ARTICLE> pageAdminArticle(Integer page, Integer limit, ArticleQuery articleQuery) {
        Page<ARTICLE> result = articleRepository.findAll((Specification<ARTICLE>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.notEqual(root.get("status"), ArticleStatus.DELETE));
            if (!StringUtils.isEmpty(articleQuery.getStatus())) {
                predicates.add(criteriaBuilder.equal(root.get("status"), articleQuery.getStatus()));
            }
            if (!StringUtils.isEmpty(articleQuery.getTitle())) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + articleQuery.getTitle() + "%"));
            }

            if (null != articleQuery.getPriority()) {
                predicates.add(criteriaBuilder.equal(root.get("priority"), articleQuery.getPriority()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, PageRequest.of(page, limit, FameUtil.sortDescById()));
        //只需要文章列表，不需要内容
        result.forEach(article -> article.setContent(""));
        return result;
    }

    @Override
    public ARTICLE getAdminArticle(Integer id) {
        ARTICLE article = articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(FameUtil.getGenericClass(getClass())));
        String content = FameUtil.contentTransform(article.getContent(), false, false, null);
        article.setContent(content);
        return article;
    }


    @Override
    @Cacheable(value = ARTICLE_CACHE_NAME, key = "'article_count'")
    public long count() {
        return articleRepository.countByStatusNot(ArticleStatus.DELETE);
    }
}
