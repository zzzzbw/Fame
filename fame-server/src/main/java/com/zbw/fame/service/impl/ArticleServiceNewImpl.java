package com.zbw.fame.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbw.fame.exception.NotFoundException;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.listener.event.ArticleHitsEvent;
import com.zbw.fame.listener.event.LogEvent;
import com.zbw.fame.mapper.ArticleMapper;
import com.zbw.fame.model.domain.Post;
import com.zbw.fame.model.dto.ArchiveDto;
import com.zbw.fame.model.dto.ArticleInfoDto;
import com.zbw.fame.model.entity.Article;
import com.zbw.fame.model.entity.BaseEntity;
import com.zbw.fame.model.enums.ArticleStatus;
import com.zbw.fame.model.enums.LogAction;
import com.zbw.fame.model.enums.LogType;
import com.zbw.fame.model.param.ArticleQuery;
import com.zbw.fame.service.ArticleServiceNew;
import com.zbw.fame.service.CommentService;
import com.zbw.fame.service.SysOptionService;
import com.zbw.fame.util.FameUtils;
import com.zbw.fame.util.OptionKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author by zzzzbw
 * @since 2021/03/09 15:09
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class ArticleServiceNewImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleServiceNew {

    private final CommentService commentService;

    private final SysOptionService sysOptionService;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Page<Article> pageArticleFront(Integer current, Integer size, List<String> sort) {
        Page<Article> page = new Page<>(current, size);

        List<OrderItem> orderItems = sort.stream().map(OrderItem::desc).collect(Collectors.toList());
        page.addOrder(orderItems);

        Page<Article> articlePage = lambdaQuery()
                .eq(Article::getStatus, ArticleStatus.PUBLISH)
                .eq(Article::isListShow, true)
                .page(page);

        String summaryFlag = sysOptionService.get(OptionKeys.SUMMARY_FLAG);
        articlePage.getRecords().forEach(article -> {
            String content = FameUtils.contentTransform(article.getContent(), true, true, summaryFlag);
            article.setContent(content);
        });
        return articlePage;
    }

    @Override
    public Article getArticleFront(Integer id) {
        Article article = getById(id);
        if (null == article) {
            throw new NotFoundException(Article.class);
        }
        if (!ArticleStatus.PUBLISH.equals(article.getStatus())) {
            throw new NotFoundException(Article.class);
        }

        String content = FameUtils.contentTransform(article.getContent(), false, true, null);
        article.setContent(content);
        return article;
    }

    @Override
    public Page<Article> pageArticleAdmin(Integer current, Integer size, ArticleQuery query) {
        Page<Article> page = new Page<>();
        page.addOrder(OrderItem.desc("id"));

        Page<Article> articlePage = lambdaQuery()
                .eq(!ObjectUtils.isEmpty(query.getStatus()), Article::getStatus, query.getStatus())
                .eq(ObjectUtils.isEmpty(query.getPriority()), Article::getPriority, query.getPriority())
                .like(ObjectUtils.isEmpty(query.getTitle()), Article::getTitle, query.getTitle())
                .page(page);

        //只需要文章列表，不需要内容
        articlePage.getRecords().forEach(article -> article.setContent(""));
        return articlePage;
    }

    @Override
    public Article getArticleAdmin(Integer id) {
        Article article = getById(id);
        if (null == article) {
            throw new NotFoundException(Article.class);
        }

        String content = FameUtils.contentTransform(article.getContent(), false, false, null);
        article.setContent(content);
        return article;
    }

    @Override
    public Integer createOrUpdate(Article article) {
        if (null != article.getId()) {
            Article old = Optional
                    .ofNullable(getById(article.getId()))
                    .orElseThrow(() -> new NotFoundException(Article.class));

            FameUtils.copyProperties(article, old, true);
            updateById(article);
        } else {
            save(article);

            LogEvent logEvent = new LogEvent(this, article, LogAction.ADD, LogType.ARTICLE, FameUtils.getIp(), FameUtils.getLoginUser().getId());
            eventPublisher.publishEvent(logEvent);
        }

        Integer id = article.getId();
        // TODO 存储分类和标签
        // categoryService.saveOrRemoveMetas(post.getCategory(), id);
        // tagService.saveOrRemoveMetas(post.getTags(), id);
        return id;
    }

    @Override
    public void delete(Integer id) {
        Article article = Optional
                .ofNullable(getById(id))
                .orElseThrow(() -> new NotFoundException(Post.class));

        log.info("删除文章: {}", article);
        removeById(id);


        int commentsResult = commentService.deleteCommentByArticleId(id);
        log.info("删除对应的评论,数量: {}", commentsResult);

        // TODO 传空的属性，则移除该文章关联的属性
        // categoryService.saveOrRemoveMetas("", post.getId());
        // tagService.saveOrRemoveMetas("", post.getId());

        LogEvent logEvent = new LogEvent(this, article, LogAction.DELETE, LogType.POST, FameUtils.getIp(), FameUtils.getLoginUser().getId());
        eventPublisher.publishEvent(logEvent);
    }

    @Override
    public void visitArticle(Integer id) {
        eventPublisher.publishEvent(new ArticleHitsEvent(this, id));
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void increaseHits(Integer id, Integer increaseHits) {
        int update = getBaseMapper().increaseHits(id, increaseHits);
        if (update < 1) {
            log.info("article increaseHits fail! postId: {}", id);
            throw new TipException("文章更新点击量失败");
        }
    }

    @Override
    public List<ArchiveDto> getArchives() {
        List<Article> articles = lambdaQuery()
                .eq(Article::getStatus, ArticleStatus.PUBLISH)
                .orderByDesc(BaseEntity::getCreated)
                .list();

        Map<Integer, List<ArticleInfoDto>> groupByYear = articles.stream()
                .map(ArticleInfoDto::new)
                .collect(Collectors.groupingBy(articleInfo -> DateUtil.year(articleInfo.getCreated())));

        List<ArchiveDto> archives = new ArrayList<>();
        groupByYear.forEach((year, articleInfos) -> {
            ArchiveDto archive = new ArchiveDto();
            archive.setYear(String.valueOf(year));
            archive.setArticleInfos(articleInfos);
            archives.add(archive);
        });

        return archives;
    }

    @Override
    public List<ArticleInfoDto> listArticleHeader() {
        List<Article> list = lambdaQuery()
                .eq(Article::getStatus, ArticleStatus.PUBLISH)
                .eq(Article::isHeaderShow, true)
                .orderByDesc(Article::getPriority)
                .list();

        return list.stream()
                .map(ArticleInfoDto::new)
                .collect(Collectors.toList());
    }
}
