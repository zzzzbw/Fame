package com.zbw.fame.service;

import com.github.pagehelper.Page;
import com.zbw.fame.dto.CommentDto;
import com.zbw.fame.model.Comments;

/**
 * 评论 Service 接口
 *
 * @author zbw
 * @create 2018/1/19 16:56
 */
public interface CommentsService {
    /**
     * 保存评论
     *
     * @param comments
     */
    void save(Comments comments);

    /**
     * 获取文章下的评论
     *
     * @param articleId
     * @param page
     * @param limit
     * @return
     */
    Page<Comments> getCommentsByArticleId(Integer articleId, Integer page, Integer limit);

    /**
     * 分页获取评论
     *
     * @param page
     * @param limit
     * @return
     */
    Page<Comments> getComments(Integer page, Integer limit);

    /**
     * 获取评论详情
     *
     * @param id
     * @return
     */
    CommentDto getCommentDetail(Integer id);

    /**
     * 删除评论
     *
     * @param id
     * @return
     */
    boolean deleteComment(Integer id);

    /**
     * 顶或踩评论
     *
     * @param commentId
     * @param assess
     */
    void assessComment(Integer commentId, String assess);
}
