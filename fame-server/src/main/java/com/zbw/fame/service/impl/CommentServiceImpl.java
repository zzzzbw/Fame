package com.zbw.fame.service.impl;

import com.zbw.fame.exception.NotFoundException;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.listener.event.CommentNewEvent;
import com.zbw.fame.listener.event.LogEvent;
import com.zbw.fame.model.domain.Article;
import com.zbw.fame.model.domain.Comment;
import com.zbw.fame.model.dto.CommentDto;
import com.zbw.fame.model.enums.CommentAssessType;
import com.zbw.fame.model.enums.CommentStatus;
import com.zbw.fame.model.enums.LogAction;
import com.zbw.fame.model.enums.LogType;
import com.zbw.fame.repository.ArticleRepository;
import com.zbw.fame.repository.CommentRepository;
import com.zbw.fame.service.CommentService;
import com.zbw.fame.util.FameUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 评论 Service 实现类
 *
 * @author zzzzbw
 * @since 2018/1/19 16:57
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final ArticleRepository<Article> articleRepository;

    private final ApplicationEventPublisher eventPublisher;


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void save(@NonNull Comment comment) {
        Article article = articleRepository.findById(comment.getArticleId())
                .orElseThrow(() -> new NotFoundException(Article.class));

        commentRepository.save(comment);

        // 增加文章的评论数
        article.setCommentCount(article.getCommentCount() + 1);
        articleRepository.save(article);
    }

    @Override
    public Page<Comment> getCommentsByArticleId(Integer page, Integer limit, Integer articleId) {
        Comment record = new Comment();
        record.setArticleId(articleId);
        record.setStatus(CommentStatus.NORMAL);
        Page<Comment> result = commentRepository.findAll(Example.of(record), PageRequest.of(page, limit, FameUtils.sortDescById()));

        result.forEach(comments -> {
            String content = FameUtils.contentTransform(comments.getContent(), false, true, null);
            comments.setContent(content);
        });

        return result;
    }

    @Override
    public Page<Comment> pageAdminComments(Integer page, Integer limit) {
        Comment record = new Comment();
        record.setStatus(CommentStatus.NORMAL);
        Page<Comment> result = commentRepository.findAll(Example.of(record), PageRequest.of(page, limit, FameUtils.sortDescById()));
        result.forEach(comments -> {
            String content = FameUtils.contentTransform(comments.getContent(), false, false, null);
            comments.setContent(content);
        });

        return result;
    }

    @Override
    public CommentDto getCommentDetail(Integer id) {
        Comment entity = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Comment.class));
        CommentDto comment = new CommentDto();
        BeanUtils.copyProperties(entity, comment);
        if (null != comment.getParentId() && -1 != comment.getParentId()) {
            Comment parentComment = commentRepository.findById(comment.getParentId()).orElse(null);
            comment.setParentComment(parentComment);
        }

        Article article = articleRepository.findById(comment.getArticleId())
                .orElseThrow(() -> new NotFoundException(Article.class));
        comment.setArticle(article);
        return comment;
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteComment(Integer id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Comment.class));

        // 减去文章中评论数
        Article article = articleRepository.findById(comment.getArticleId())
                .orElseThrow(() -> new NotFoundException(Article.class));
        article.setCommentCount(article.getCommentCount() - 1);
        articleRepository.save(article);

        // 去除子评论中关联
        Comment record = new Comment();
        record.setParentId(id);
        commentRepository.findOne(Example.of(record)).ifPresent(childComment -> {
            childComment.setParentId(null);
            commentRepository.save(childComment);
        });

        comment.setStatus(CommentStatus.DELETE);

        log.info("删除评论: {}", comment);
        commentRepository.save(comment);

        LogEvent logEvent = new LogEvent(this, comment, LogAction.DELETE, LogType.COMMENT, FameUtils.getIp(), FameUtils.getLoginUser().getId());
        eventPublisher.publishEvent(logEvent);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public int deleteCommentByArticleId(Integer articleId) {
        Comment record = new Comment();
        record.setArticleId(articleId);
        List<Comment> list = commentRepository.findAll(Example.of(record));
        list.forEach(comment -> comment.setStatus(CommentStatus.DELETE));

        LogEvent logEvent = new LogEvent(this, list, LogAction.DELETE, LogType.COMMENT, FameUtils.getIp(), FameUtils.getLoginUser().getId());
        eventPublisher.publishEvent(logEvent);

        return commentRepository.saveAll(list).size();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void assessComment(Integer commentId, CommentAssessType assess) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(Comment.class));

        if (CommentAssessType.AGREE.equals(assess)) {
            comment.setAgree(comment.getAgree() + 1);
        } else if (CommentAssessType.DISAGREE.equals(assess)) {
            comment.setDisagree(comment.getDisagree() + 1);
        } else {
            throw new TipException("assess参数错误");
        }
        commentRepository.save(comment);
    }

    @Override
    public Long count() {
        Comment record = new Comment();
        record.setStatus(CommentStatus.NORMAL);
        return commentRepository.count(Example.of(record));
    }

    @Override
    public void newCommentEvent(Comment comment) {
        eventPublisher.publishEvent(new CommentNewEvent(this, comment.getId()));

        LogEvent logEvent = new LogEvent(this, comment, LogAction.ADD, LogType.COMMENT, FameUtils.getIp(), null);
        eventPublisher.publishEvent(logEvent);
    }

}
