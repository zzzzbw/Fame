package com.zbw.fame.service.impl;

import com.zbw.fame.exception.TipException;
import com.zbw.fame.model.domain.Article;
import com.zbw.fame.model.dto.Archive;
import com.zbw.fame.model.query.ArticleQuery;
import com.zbw.fame.repository.ArticleRepository;
import com.zbw.fame.service.ArticleService;
import com.zbw.fame.service.CommentService;
import com.zbw.fame.service.MetaService;
import com.zbw.fame.util.FameConsts;
import com.zbw.fame.util.FameUtil;
import com.zbw.fame.util.Types;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 文章 Service 实现类
 *
 * @author zbw
 * @since 2017/8/21 22:02
 */
@Slf4j
@Service("articlesService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ArticleServiceImpl implements ArticleService {

    public static final String ARTICLE_CACHE_NAME = "articles";

    private final ArticleRepository articleRepository;

    private final CommentService commentService;

    private final MetaService metasService;


    @Override
    @Cacheable(value = ARTICLE_CACHE_NAME, key = "'font_articles['+#page+':'+#limit+']'")
    public Page<Article> getFrontArticles(Integer page, Integer limit) {
        Article record = new Article();
        record.setStatus(Types.PUBLISH);
        record.setType(Types.POST);

        Page<Article> result = articleRepository.findAll(Example.of(record), PageRequest.of(page, limit));
        result.forEach(article -> {
            String content = FameUtil.contentTransform(article.getContent(), true, true);
            article.setContent(content);
        });
        return result;
    }

    @Override
    @Cacheable(value = ARTICLE_CACHE_NAME, key = "'front_article['+#id+']'")
    public Article getFrontArticle(Integer id) {
        Article record = new Article();
        record.setId(id);
        record.setStatus(Types.PUBLISH);
        record.setType(Types.POST);

        Article article = articleRepository.findOne(Example.of(record))
                .orElseThrow(() -> new TipException("该文章不存在"));
        String content = FameUtil.contentTransform(article.getContent(), false, true);
        article.setContent(content);
        return article;
    }

    @Override
    public Page<Article> getAdminArticles(Integer page, Integer limit, ArticleQuery articleQuery) {
        Page<Article> result = articleRepository.findAll((Specification<Article>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("type"), Types.POST));
            predicates.add(criteriaBuilder.notEqual(root.get("status"), Types.DELETE));
            if (!StringUtils.isEmpty(articleQuery.getStatus())) {
                predicates.add(criteriaBuilder.equal(root.get("status"), articleQuery.getStatus()));
            }
            if (!StringUtils.isEmpty(articleQuery.getTitle())) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + articleQuery.getTitle() + "%"));
            }
            if (!StringUtils.isEmpty(articleQuery.getTag())) {
                predicates.add(criteriaBuilder.like(root.get("tags"), "%" + articleQuery.getTag() + "%"));
            }
            if (!StringUtils.isEmpty(articleQuery.getCategory())) {
                predicates.add(criteriaBuilder.like(root.get("category"), "%" + articleQuery.getCategory() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, PageRequest.of(page, limit));
        //只需要文章列表，不需要内容
        result.forEach(article -> article.setContent(""));
        return result;
    }

    @Override
    public Article getAdminArticle(Integer id) {
        Article record = new Article();
        record.setId(id);
        record.setType(Types.POST);
        Article article = articleRepository.findOne(Example.of(record))
                .orElseThrow(() -> new TipException("该文章不存在"));
        String content = FameUtil.contentTransform(article.getContent(), false, false);
        article.setContent(content);
        return article;
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    @CacheEvict(value = ARTICLE_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public Integer saveArticle(Article article) {
        if (null == article) {
            throw new TipException("文章对象为空");
        }
        if (StringUtils.isEmpty(article.getTitle())) {
            throw new TipException("文章标题不能为空");
        }
        if (article.getTitle().length() > FameConsts.MAX_TITLE_COUNT) {
            throw new TipException("文章标题字数不能超过" + FameConsts.MAX_TITLE_COUNT);
        }

        if (StringUtils.isEmpty(article.getContent())) {
            throw new TipException("文章内容不能为空");
        }
        if (article.getContent().length() > FameConsts.MAX_CONTENT_COUNT) {
            throw new TipException("文章内容字数不能超过" + FameConsts.MAX_CONTENT_COUNT);
        }
        if (null == article.getAuthorId()) {
            throw new TipException("请先登陆后发布文章");
        }

        if (null != article.getId()) {
            Article oldArticle = articleRepository.findById(article.getId())
                    .orElseThrow(() -> new TipException("修改文章id不存在"));

            FameUtil.copyPropertiesIgnoreNull(article, oldArticle);
            articleRepository.saveAndFlush(oldArticle);
        } else {
            article.setType(Types.POST);
            articleRepository.saveAndFlush(article);
        }

        Integer id = article.getId();
        //存储分类和标签
        metasService.saveOrRemoveMetas(article.getCategory(), Types.CATEGORY, id);
        metasService.saveOrRemoveMetas(article.getTags(), Types.TAG, id);
        return id;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    @CacheEvict(value = ARTICLE_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public boolean updateHits(Integer articleId, Integer hits) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new TipException("文章不能为空"));
        article.setHits(hits);
        return articleRepository.saveAndFlush(article) != null;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    @CacheEvict(value = ARTICLE_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public boolean deleteArticle(Integer id) {
        Article record = new Article();
        record.setId(id);
        record.setType(Types.POST);
        Article article = articleRepository.findOne(Example.of(record))
                .orElseThrow(() -> new TipException("没有id为" + id + "的文章"));

        article.setStatus(Types.DELETE);
        if (articleRepository.save(article) != null) {
            log.info("删除文章: {}", article);
            int commentsResult = commentService.deleteCommentByArticleId(id);
            log.info("删除对应的评论,数量: {}", commentsResult);

            // 传空的属性，则移除该文章关联的属性
            metasService.saveOrRemoveMetas("", Types.CATEGORY, article.getId());
            metasService.saveOrRemoveMetas("", Types.TAG, article.getId());
            return true;
        }
        return false;
    }

    @Override
    @Cacheable(value = ARTICLE_CACHE_NAME, key = "'article_count'")
    public long count() {
        return articleRepository.count((Specification<Article>) (root, query, criteriaBuilder) -> {
            Predicate p1 = criteriaBuilder.equal(root.get("type"), Types.POST);
            Predicate p2 = criteriaBuilder.notEqual(root.get("status"), Types.DELETE);
            return criteriaBuilder.and(p1, p2);
        });
    }

    @Override
    @Cacheable(value = ARTICLE_CACHE_NAME, key = "'archives'")
    public List<Archive> getArchives() {
        List<Article> articles = articleRepository.findAllByStatusAndType(Types.PUBLISH, Types.POST);
        List<Archive> archives = new ArrayList<>();
        String current = "";
        for (Article article : articles) {
            // 清空文章内容
            article.setContent("");
            Calendar cal = Calendar.getInstance();
            cal.setTime(article.getCreated());
            String dateStr = String.valueOf(cal.get(Calendar.YEAR));
            if (dateStr.equals(current)) {
                Archive arc = archives.get(archives.size() - 1);
                arc.getArticles().add(article);
                arc.setCount(arc.getArticles().size());
            } else {
                current = dateStr;
                Archive arc = new Archive();
                arc.setDateStr(dateStr);
                arc.setCount(1);
                List<Article> arts = new ArrayList<>();
                arts.add(article);
                arc.setArticles(arts);
                archives.add(arc);
            }
        }
        return archives;
    }

    @Override
    @Cacheable(value = ARTICLE_CACHE_NAME, key = "'front_page['+#title+']'")
    public Article getFrontPage(String title) {
        Article record = new Article();
        record.setTitle(title);
        record.setStatus(Types.PUBLISH);
        record.setType(Types.PAGE);
        Article article = articleRepository.findOne(Example.of(record)).orElseThrow(() -> new TipException("文章不存在"));
        String content = FameUtil.contentTransform(article.getContent(), false, true);
        article.setContent(content);
        return article;
    }

    @Override
    public Page<Article> getAdminPages(Integer page, Integer limit) {
        Page<Article> result = articleRepository.findAll((Specification<Article>) (root, query, criteriaBuilder) -> {
            Predicate p1 = criteriaBuilder.equal(root.get("type"), Types.PAGE);
            Predicate p2 = criteriaBuilder.notEqual(root.get("status"), Types.DELETE);

            return criteriaBuilder.and(p1, p2);
        }, PageRequest.of(page, limit));
        result.forEach(article -> article.setContent(""));
        return result;
    }

    @Override
    public Article getAdminPage(Integer id) {
        Article record = new Article();
        record.setId(id);
        record.setType(Types.PAGE);
        Article article = articleRepository.findOne(Example.of(record)).orElseThrow(() -> new TipException("文章不存在"));
        String content = FameUtil.contentTransform(article.getContent(), false, false);
        article.setContent(content);
        return article;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    @CacheEvict(value = ARTICLE_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public Integer savePage(Article page) {
        if (null == page) {
            throw new TipException("自定义页面对象为空");
        }
        if (StringUtils.isEmpty(page.getTitle())) {
            throw new TipException("自定义页面标题不能为空");
        }
        if (page.getTitle().length() > FameConsts.MAX_TITLE_COUNT) {
            throw new TipException("自定义页面标题字数不能超过" + FameConsts.MAX_TITLE_COUNT);
        }

        if (StringUtils.isEmpty(page.getContent())) {
            throw new TipException("自定义页面内容不能为空");
        }
        if (page.getContent().length() > FameConsts.MAX_CONTENT_COUNT) {
            throw new TipException("自定义页面容字数不能超过" + FameConsts.MAX_CONTENT_COUNT);
        }
        if (null == page.getAuthorId()) {
            throw new TipException("请先登陆");
        }


        if (null != page.getId()) {
            Article oldPage = articleRepository.findById(page.getId())
                    .orElseThrow(() -> new TipException("修改文章id不存在"));

            FameUtil.copyPropertiesIgnoreNull(page, oldPage);
            articleRepository.saveAndFlush(oldPage);
        } else {
            page.setType(Types.PAGE);
            articleRepository.saveAndFlush(page);
        }

        return page.getId();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    @CacheEvict(value = ARTICLE_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public boolean deletePage(Integer id) {
        Article record = new Article();
        record.setId(id);
        record.setType(Types.PAGE);
        Article page = articleRepository.findOne(Example.of(record)).orElseThrow(() -> new TipException("没有id为" + id + "的自定义页面"));
        page.setStatus(Types.DELETE);
        return articleRepository.save(page) != null;
    }
}
