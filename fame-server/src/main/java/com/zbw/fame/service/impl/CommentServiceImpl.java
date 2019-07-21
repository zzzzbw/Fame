package com.zbw.fame.service.impl;

import com.zbw.fame.exception.TipException;
import com.zbw.fame.model.domain.Article;
import com.zbw.fame.model.domain.Comment;
import com.zbw.fame.model.dto.CommentDto;
import com.zbw.fame.repository.ArticleRepository;
import com.zbw.fame.repository.CommentRepository;
import com.zbw.fame.service.CommentService;
import com.zbw.fame.util.FameConsts;
import com.zbw.fame.util.FameUtil;
import com.zbw.fame.util.Types;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;


/**
 * 评论 Service 实现类
 *
 * @author zbw
 * @since 2018/1/19 16:57
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentServiceImpl implements CommentService {

    public static final String COMMENT_CACHE_NAME = "comments";

    private final CommentRepository commentRepository;

    private final ArticleRepository<Article> articleRepository;


    @Override
    @Transactional(rollbackFor = Throwable.class)
    @CacheEvict(value = {COMMENT_CACHE_NAME, AbstractArticleServiceImpl.ARTICLE_CACHE_NAME}, allEntries = true, beforeInvocation = true)
    public void save(Comment comment) {
        if (null == comment) {
            throw new TipException("评论对象为空");
        }
        if (StringUtils.isEmpty(comment.getContent())) {
            throw new TipException("评论不能为空");
        }
        if (comment.getContent().length() > FameConsts.MAX_COMMENT_CONTENT_COUNT) {
            throw new TipException("评论字数不能超过" + FameConsts.MAX_COMMENT_CONTENT_COUNT);
        }
        if (StringUtils.isEmpty(comment.getName())) {
            throw new TipException("名称不能为空");
        }
        if (comment.getName().length() > FameConsts.MAX_COMMENT_NAME_COUNT) {
            throw new TipException("名称字数不能超过" + FameConsts.MAX_COMMENT_NAME_COUNT);
        }
        if (!StringUtils.isEmpty(comment.getEmail()) && comment.getEmail().length() > FameConsts.MAX_COMMENT_EMAIL_COUNT) {
            throw new TipException("邮箱字数不能超过" + FameConsts.MAX_COMMENT_EMAIL_COUNT);
        }
        if (!StringUtils.isEmpty(comment.getWebsite()) && comment.getWebsite().length() > FameConsts.MAX_COMMENT_WEBSITE_COUNT) {
            throw new TipException("网址长度不能超过" + FameConsts.MAX_COMMENT_WEBSITE_COUNT);
        }


        Article article = articleRepository.findById(comment.getArticleId())
                .orElseThrow(() -> new TipException("无法查询到对应评论文章"));

        commentRepository.save(comment);

        // 增加文章的评论数
        article.setCommentCount(article.getCommentCount() + 1);
        articleRepository.save(article);
    }

    @Override
    @Cacheable(value = COMMENT_CACHE_NAME, key = "'article_comments['+#page+':'+#limit+':'+#articleId+']'")
    public Page<Comment> getCommentsByArticleId(Integer page, Integer limit, Integer articleId) {
        Comment record = new Comment();
        record.setArticleId(articleId);
        record.setStatus(Types.COMMENT_STATUS_NORMAL);
        Page<Comment> result = commentRepository.findAll(Example.of(record), PageRequest.of(page, limit));

        result.forEach(comments -> {
            String content = FameUtil.contentTransform(comments.getContent(), false, true);
            comments.setContent(content);
        });

        return result;
    }

    @Override
    public Page<Comment> getAdminComments(Integer page, Integer limit) {
        Comment record = new Comment();
        record.setStatus(Types.COMMENT_STATUS_NORMAL);
        Page<Comment> result = commentRepository.findAll(Example.of(record), PageRequest.of(page, limit));
        result.forEach(comments -> {
            String content = FameUtil.contentTransform(comments.getContent(), false, false);
            comments.setContent(content);
        });

        return result;
    }

    @Override
    @Cacheable(value = COMMENT_CACHE_NAME, key = "'comment_detail['+#id+']'")
    public CommentDto getCommentDetail(Integer id) {
        Comment entity = commentRepository.findById(id)
                .orElseThrow(() -> new TipException("不存在该评论"));
        CommentDto comment = new CommentDto();
        BeanUtils.copyProperties(entity, comment);
        if (null != comment.getPId() && -1 != comment.getPId()) {
            Comment pComment = commentRepository.findById(comment.getPId()).orElse(null);
            comment.setPComment(pComment);
        }

        Article article = articleRepository.findById(comment.getArticleId())
                .orElseThrow(() -> new RuntimeException("评论关联文章不存在"));
        comment.setArticle(article);
        return comment;
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    @CacheEvict(value = COMMENT_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public boolean deleteComment(Integer id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new TipException("不存在该评论"));

        // 减去文章中评论数
        Article article = articleRepository.findById(comment.getArticleId())
                .orElseThrow(() -> new TipException("评论关联文章不存在"));
        article.setCommentCount(article.getCommentCount() - 1);
        articleRepository.save(article);

        // 去除子评论中关联
        Comment record = new Comment();
        record.setPId(id);
        commentRepository.findOne(Example.of(record)).ifPresent(childComment -> {
            childComment.setPId(null);
            commentRepository.save(childComment);
        });

        comment.setStatus(Types.COMMENT_STATUS_DELETE);
        if (commentRepository.save(comment) != null) {
            log.info("删除评论: {}", comment);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    @CacheEvict(value = COMMENT_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public int deleteCommentByArticleId(Integer articleId) {
        Comment record = new Comment();
        record.setArticleId(articleId);
        List<Comment> list = commentRepository.findAll(Example.of(record));
        list.forEach(comment -> comment.setStatus(Types.COMMENT_STATUS_DELETE));
        return commentRepository.saveAll(list).size();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    @CacheEvict(value = COMMENT_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public void assessComment(Integer commentId, String assess) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new TipException("没有该评论"));

        if (Types.AGREE.equals(assess)) {
            comment.setAgree(comment.getAgree() + 1);
        } else if (Types.DISAGREE.equals(assess)) {
            comment.setDisagree(comment.getDisagree() + 1);
        } else {
            throw new TipException("assess参数错误");
        }
        commentRepository.save(comment);
    }

    @Override
    @Cacheable(value = COMMENT_CACHE_NAME, key = "'comment_count'")
    public Long count() {
        Comment record = new Comment();
        record.setStatus(Types.COMMENT_STATUS_NORMAL);
        return commentRepository.count(Example.of(record));
    }

}
