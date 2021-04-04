package com.zbw.fame.service.impl;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbw.fame.exception.NotFoundException;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.listener.event.CommentNewEvent;
import com.zbw.fame.listener.event.LogEvent;
import com.zbw.fame.mapper.CommentMapper;
import com.zbw.fame.model.dto.CommentDto;
import com.zbw.fame.model.entity.Article;
import com.zbw.fame.model.entity.BaseEntity;
import com.zbw.fame.model.entity.Comment;
import com.zbw.fame.model.enums.CommentAssessType;
import com.zbw.fame.model.enums.LogAction;
import com.zbw.fame.model.enums.LogType;
import com.zbw.fame.service.ArticleService;
import com.zbw.fame.service.CommentService;
import com.zbw.fame.util.FameUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 评论 Service 实现类
 *
 * @author zzzzbw
 * @since 2018/1/19 16:57
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final ArticleService articleService;

    private final ApplicationEventPublisher eventPublisher;


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void createComment(@NonNull Comment comment) {
        Article article = Optional.of(articleService.getById(comment.getArticleId()))
                .orElseThrow(() -> new NotFoundException(Article.class));

        save(comment);

        this.createCommentEvent(comment);
    }

    @Override
    public Page<Comment> pageByArticleId(Integer current, Integer size, Integer articleId) {
        Comment record = new Comment();
        record.setArticleId(articleId);

        Page<Comment> page = new Page<>(current, size);
        page.addOrder(OrderItem.desc("id"));

        Page<Comment> commentPage = lambdaQuery()
                .eq(Comment::getArticleId, articleId)
                .page(page);

        commentPage.getRecords().forEach(comments -> {
            String content = FameUtils.contentTransform(comments.getContent(), false, true, null);
            comments.setContent(content);
        });

        return commentPage;
    }

    @Override
    public Page<Comment> pageCommentAdmin(Integer current, Integer size) {
        Page<Comment> page = new Page<>(current, size);
        page.addOrder(OrderItem.desc("id"));
        Page<Comment> commentPage = page(page);


        commentPage.getRecords().forEach(comments -> {
            String content = FameUtils.contentTransform(comments.getContent(), false, false, null);
            comments.setContent(content);
        });

        return commentPage;
    }

    @Override
    public CommentDto getCommentDto(Integer id) {

        Comment entity = getById(id);
        if (null == entity) {
            throw new NotFoundException(Comment.class);
        }

        CommentDto commentDto = new CommentDto();
        BeanUtils.copyProperties(entity, commentDto);
        if (null != commentDto.getParentId() && -1 != commentDto.getParentId()) {

            Comment parentComment = getById(commentDto.getParentId());
            commentDto.setParentComment(parentComment);
        }


        Article article = Optional.ofNullable(articleService.getById(commentDto.getArticleId()))
                .orElseThrow(() -> new NotFoundException(Article.class));
        commentDto.setArticle(article);
        return commentDto;
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteComment(Integer id) {
        Comment comment = getById(id);
        if (null == comment) {
            throw new NotFoundException(Comment.class);
        }

        // 去除子评论中关联
        List<Comment> childComments = lambdaQuery()
                .eq(Comment::getParentId, id)
                .list();
        childComments.forEach(child -> child.setParentId(null));
        updateBatchById(childComments);

        log.info("删除评论: {}", comment);
        removeById(id);

        LogEvent logEvent = new LogEvent(this, comment, LogAction.DELETE, LogType.COMMENT, FameUtils.getIp(), FameUtils.getLoginUser().getId());
        eventPublisher.publishEvent(logEvent);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public int deleteByArticleId(Integer articleId) {
        List<Comment> list = lambdaQuery()
                .eq(Comment::getArticleId, articleId)
                .list();
        Set<Integer> ids = list.stream().map(BaseEntity::getId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        removeByIds(ids);

        LogEvent logEvent = new LogEvent(this, list, LogAction.DELETE, LogType.COMMENT, FameUtils.getIp(), FameUtils.getLoginUser().getId());
        eventPublisher.publishEvent(logEvent);

        return ids.size();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void assessComment(Integer commentId, CommentAssessType assess) {
        Comment comment = getById(commentId);
        if (null == comment) {
            throw new NotFoundException(Comment.class);
        }

        if (CommentAssessType.AGREE.equals(assess)) {
            comment.setAgree(comment.getAgree() + 1);
        } else if (CommentAssessType.DISAGREE.equals(assess)) {
            comment.setDisagree(comment.getDisagree() + 1);
        } else {
            throw new TipException("assess参数错误");
        }
        updateById(comment);
    }

    @Override
    public void createCommentEvent(Comment comment) {
        eventPublisher.publishEvent(new CommentNewEvent(this, comment.getId()));

        LogEvent logEvent = new LogEvent(this, comment, LogAction.ADD, LogType.COMMENT, FameUtils.getIp(), null);
        eventPublisher.publishEvent(logEvent);
    }

    @Override
    public int countByArticleId(Integer articleId) {
        return lambdaQuery()
                .eq(Comment::getArticleId, articleId)
                .count();
    }

    @Override
    public Map<Integer, Long> countByArticleIds(Collection<Integer> articleIds) {
        if (CollectionUtils.isEmpty(articleIds)) {
            return Collections.emptyMap();
        }

        List<Comment> comments = lambdaQuery()
                .select(BaseEntity::getId)
                .select(Comment::getArticleId)
                .in(Comment::getArticleId, articleIds)
                .list();

        return comments.stream()
                .collect(Collectors.groupingBy(Comment::getArticleId, Collectors.counting()));
    }

}
