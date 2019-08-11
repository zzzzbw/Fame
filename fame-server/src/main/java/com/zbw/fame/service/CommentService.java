package com.zbw.fame.service;

import com.zbw.fame.model.domain.Comment;
import com.zbw.fame.model.dto.CommentDto;
import com.zbw.fame.model.enums.CommentAssessType;
import org.springframework.data.domain.Page;

/**
 * 评论 Service 接口
 *
 * @author zbw
 * @since 2018/1/19 16:56
 */
public interface CommentService {
    /**
     * 保存评论
     *
     * @param comments 评论entity
     */
    void save(Comment comments);

    /**
     * 获取文章下的评论
     *
     * @param page      第几页
     * @param limit     每页数量
     * @param articleId 文章id
     * @return Page<Comment>
     */
    Page<Comment> getCommentsByArticleId(Integer page, Integer limit, Integer articleId);

    /**
     * 获取文章下的评论
     *
     * @param page  第几页
     * @param limit 每页数量
     * @return Page<Comment>
     */
    Page<Comment> pageAdminComments(Integer page, Integer limit);

    /**
     * 获取评论详情
     *
     * @param id 评论id
     * @return CommentDto
     */
    CommentDto getCommentDetail(Integer id);

    /**
     * 删除评论
     *
     * @param id 评论id
     */
    void deleteComment(Integer id);

    /**
     * 根据文章id删除评论
     *
     * @param articleId 文章id
     * @return 删除评论数量
     */
    int deleteCommentByArticleId(Integer articleId);

    /**
     * 顶或踩评论
     *
     * @param commentId 评论给id
     * @param assess    点评类型 {@link CommentAssessType}
     */
    void assessComment(Integer commentId, CommentAssessType assess);

    /**
     * 评论数量
     *
     * @return 数量
     */
    Long count();
}
