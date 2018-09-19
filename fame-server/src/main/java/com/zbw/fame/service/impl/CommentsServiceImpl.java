package com.zbw.fame.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zbw.fame.dto.CommentDto;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.mapper.ArticlesMapper;
import com.zbw.fame.mapper.CommentsMapper;
import com.zbw.fame.model.Articles;
import com.zbw.fame.model.Comments;
import com.zbw.fame.service.CommentsService;
import com.zbw.fame.util.FameConsts;
import com.zbw.fame.util.Types;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 评论 Service 实现类
 *
 * @author zbw
 * @since 2018/1/19 16:57
 */
@Service("commentsService")
@Transactional(rollbackFor = Throwable.class)
public class CommentsServiceImpl implements CommentsService {

    @Autowired
    private CommentsMapper commentsMapper;

    @Autowired
    private ArticlesMapper articlesMapper;

    @Override
    public Page<Comments> getCommentsByArticleId(Integer articleId, Integer page, Integer limit) {
        Comments record = new Comments();
        record.setArticleId(articleId);
        return PageHelper.startPage(page, limit).doSelectPage(() -> commentsMapper.select(record));
    }

    @Override
    public void save(Comments comments) {
        if (null == comments) {
            throw new TipException("评论对象为空");
        }
        if (StringUtils.isEmpty(comments.getContent())) {
            throw new TipException("评论不能为空");
        }
        if (comments.getContent().length() > FameConsts.MAX_COMMENT_CONTENT_COUNT) {
            throw new TipException("评论字数不能超过" + FameConsts.MAX_COMMENT_CONTENT_COUNT);
        }
        if (StringUtils.isEmpty(comments.getName())) {
            throw new TipException("名称不能为空");
        }
        if (comments.getName().length() > FameConsts.MAX_COMMENT_NAME_COUNT) {
            throw new TipException("名称字数不能超过" + FameConsts.MAX_COMMENT_NAME_COUNT);
        }
        if (!StringUtils.isEmpty(comments.getEmail()) && comments.getEmail().length() > FameConsts.MAX_COMMENT_EMAIL_COUNT) {
            throw new TipException("邮箱字数不能超过" + FameConsts.MAX_COMMENT_EMAIL_COUNT);
        }
        if (!StringUtils.isEmpty(comments.getWebsite()) && comments.getWebsite().length() > FameConsts.MAX_COMMENT_WEBSITE_COUNT) {
            throw new TipException("网址长度不能超过" + FameConsts.MAX_COMMENT_WEBSITE_COUNT);
        }
        comments.setAgree(0);
        comments.setDisagree(0);
        commentsMapper.insert(comments);
    }

    @Override
    public Page<Comments> getComments(Integer page, Integer limit) {
        return PageHelper.startPage(page, limit).doSelectPage(() -> commentsMapper.selectAll());
    }

    @Override
    public CommentDto getCommentDetail(Integer id) {
        Comments entity = commentsMapper.selectByPrimaryKey(id);
        if (null == entity) {
            return null;
        }
        CommentDto comment = new CommentDto();
        BeanUtils.copyProperties(entity, comment);
        if (null != comment.getpId() && -1 != comment.getpId()) {
            Comments pComment = commentsMapper.selectByPrimaryKey(comment.getpId());
            comment.setpComment(pComment);
        }

        Articles articles = articlesMapper.selectByPrimaryKey(comment.getArticleId());
        comment.setArticle(articles);
        return comment;
    }


    @Override
    public boolean deleteComment(Integer id) {
        Comments record = new Comments();
        record.setpId(id);
        Comments childComment = commentsMapper.selectOne(record);
        if (null != childComment) {
            childComment.setpId(null);
            commentsMapper.updateByPrimaryKey(childComment);
        }
        return commentsMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public void assessComment(Integer commentId, String assess) {
        Comments comment = commentsMapper.selectByPrimaryKey(commentId);
        if (null == comment) {
            throw new TipException("没有该评论");
        }

        if (Types.AGREE.equals(assess)) {
            comment.setAgree(comment.getAgree() + 1);
        } else if (Types.DISAGREE.equals(assess)) {
            comment.setDisagree(comment.getDisagree() + 1);
        } else {
            throw new TipException("assess参数错误");
        }
        commentsMapper.updateByPrimaryKey(comment);
    }

    @Override
    public Integer count() {
        return commentsMapper.selectCount(new Comments());
    }

}
