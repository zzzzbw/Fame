package com.zbw.fame.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbw.fame.exception.NotFoundException;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.listener.event.ArticleHitsEvent;
import com.zbw.fame.listener.event.LogEvent;
import com.zbw.fame.mapper.ArticleMapper;
import com.zbw.fame.model.dto.ArchiveDto;
import com.zbw.fame.model.dto.ArticleDetailDto;
import com.zbw.fame.model.dto.ArticleInfoDto;
import com.zbw.fame.model.dto.LoginUser;
import com.zbw.fame.model.entity.Article;
import com.zbw.fame.model.entity.BaseEntity;
import com.zbw.fame.model.entity.Category;
import com.zbw.fame.model.entity.Tag;
import com.zbw.fame.model.enums.ArticleStatus;
import com.zbw.fame.model.enums.LogAction;
import com.zbw.fame.model.enums.LogType;
import com.zbw.fame.model.param.ArticleQuery;
import com.zbw.fame.model.param.SaveArticleParam;
import com.zbw.fame.service.*;
import com.zbw.fame.util.FameUtils;
import com.zbw.fame.util.OptionKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author by zzzzbw
 * @since 2021/03/09 15:09
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private final CommentService commentService;

    private final SysOptionService sysOptionService;

    private final ArticleCategoryService articleCategoryService;

    private final ArticleTagService articleTagService;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public IPage<ArticleDetailDto> pageArticleFront(Integer current, Integer size, List<String> sort) {
        Page<Article> page = new Page<>(current, size);

        List<OrderItem> orderItems = sort.stream().map(OrderItem::desc).collect(Collectors.toList());
        page.addOrder(orderItems);

        Page<Article> articlePage = lambdaQuery()
                .eq(Article::getStatus, ArticleStatus.PUBLISH)
                .eq(Article::isListShow, true)
                .page(page);

        return batchConvertToDetailDto(articlePage);
    }

    @Override
    public ArticleDetailDto getArticleFront(Integer id) {
        Article article = lambdaQuery()
                .eq(BaseEntity::getId, id)
                .eq(Article::getStatus, ArticleStatus.PUBLISH)
                .one();

        if (null == article) {
            throw new NotFoundException(Article.class);
        }

        return convertToDetailDto(article);
    }

    @Override
    public IPage<ArticleDetailDto> pageArticleAdmin(Integer current, Integer size, ArticleQuery query) {
        Page<Article> page = new Page<>(current, size);
        page.addOrder(OrderItem.desc("id"));

        Page<Article> articlePage = lambdaQuery()
                .eq(!ObjectUtils.isEmpty(query.getStatus()), Article::getStatus, query.getStatus())
                .eq(!ObjectUtils.isEmpty(query.getPriority()), Article::getPriority, query.getPriority())
                .like(!ObjectUtils.isEmpty(query.getTitle()), Article::getTitle, query.getTitle())
                .eq(!ObjectUtils.isEmpty(query.getListShow()), Article::isListShow, query.getListShow())
                .eq(!ObjectUtils.isEmpty(query.getHeaderShow()), Article::isHeaderShow, query.getHeaderShow())
                .page(page);

        return batchConvertToDetailDto(articlePage);
    }

    @Override
    public ArticleDetailDto getArticleAdmin(Integer id) {
        Article article = getById(id);
        if (null == article) {
            throw new NotFoundException(Article.class);
        }

        return convertToDetailDto(article);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ArticleDetailDto createOrUpdate(SaveArticleParam param) {
        Article article;
        if (null != param.getId()) {
            article = Optional
                    .ofNullable(getById(param.getId()))
                    .orElseThrow(() -> new NotFoundException(Article.class));

            FameUtils.copyPropertiesIgnoreNull(param, article);
            updateById(article);
        } else {
            article = FameUtils.convertTo(param, Article.class);
            LoginUser user = FameUtils.getLoginUser();
            article.setAuthorId(user.getId());
            save(article);

            LogEvent logEvent = new LogEvent(this, param, LogAction.ADD, LogType.ARTICLE, FameUtils.getIp(), FameUtils.getLoginUser().getId());
            eventPublisher.publishEvent(logEvent);
        }

        articleCategoryService.createOrUpdate(article.getId(), param.getCategoryId());
        articleTagService.createOrUpdate(article.getId(), param.getTagIds());
        return convertToDetailDto(article);
    }

    @Override
    public void delete(Integer id) {
        Article article = Optional
                .ofNullable(getById(id))
                .orElseThrow(() -> new NotFoundException(Article.class));

        log.info("删除文章: {}", article);
        removeById(id);


        int commentsResult = commentService.deleteByArticleId(id);
        log.info("删除对应的评论,数量: {}", commentsResult);

        articleCategoryService.deleteByArticleId(article.getId());
        articleTagService.deleteByArticleId(article.getId());

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
        return lambdaQuery()
                .eq(Article::getStatus, ArticleStatus.PUBLISH)
                .eq(Article::isHeaderShow, true)
                .orderByDesc(Article::getPriority)
                .list()
                .stream()
                .map(ArticleInfoDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<Article> listByIds(Collection<Integer> ids, boolean isFront) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }

        return lambdaQuery()
                .select()
                .eq(isFront, Article::getStatus, ArticleStatus.PUBLISH)
                .in(BaseEntity::getId, ids)
                .list();
    }

    /**
     * 转换为 ArticleDetailDto
     *
     * @param article
     * @return
     */
    private ArticleDetailDto convertToDetailDto(Article article) {
        ArticleDetailDto articleDetailDto = FameUtils.convertTo(article, ArticleDetailDto.class);
        String contentHtml = FameUtils.contentTransform(articleDetailDto.getContent(), false, true, null);
        articleDetailDto.setContentHtml(contentHtml);

        Category category = articleCategoryService.getCategoryByArticleId(articleDetailDto.getId());
        articleDetailDto.setCategory(category);
        List<Tag> tags = articleTagService.listTagByArticleId(articleDetailDto.getId());
        articleDetailDto.setTags(tags);
        int commentCount = commentService.countByArticleId(articleDetailDto.getId());
        articleDetailDto.setCommentCount(commentCount);

        return articleDetailDto;
    }

    /**
     * 批量转换为 ArticleDetailDto
     *
     * @param articles
     * @return
     */
    private IPage<ArticleDetailDto> batchConvertToDetailDto(IPage<Article> articles) {
        Set<Integer> articleIds = articles.getRecords().stream().map(BaseEntity::getId).collect(Collectors.toSet());

        Map<Integer, Category> categoryMap = articleCategoryService.listCategoryByArticleIds(articleIds);
        Map<Integer, List<Tag>> tagMap = articleTagService.listTagByArticleIds(articleIds);
        Map<Integer, Long> countMap = commentService.countByArticleIds(articleIds);

        String summaryFlag = sysOptionService.get(OptionKeys.SUMMARY_FLAG);
        return articles.convert(article -> {
            ArticleDetailDto articleDetailDto = FameUtils.convertTo(article, ArticleDetailDto.class);
            String contentHtml = FameUtils.contentTransform(articleDetailDto.getContent(), true, true, summaryFlag);
            articleDetailDto.setContentHtml(contentHtml);

            Category category = categoryMap.get(articleDetailDto.getId());
            articleDetailDto.setCategory(category);
            List<Tag> tags = tagMap.getOrDefault(articleDetailDto.getId(), Collections.emptyList());
            articleDetailDto.setTags(tags);
            int commentCount = Math.toIntExact(countMap.getOrDefault(articleDetailDto.getId(), 0L));
            articleDetailDto.setCommentCount(commentCount);
            return articleDetailDto;
        });
    }
}
