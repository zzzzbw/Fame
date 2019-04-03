package com.zbw.fame.service;

import com.github.pagehelper.Page;
import com.zbw.fame.model.domain.Comment;
import com.zbw.fame.model.param.CommentParam;
import com.zbw.fame.model.dto.CommentDto;
import com.zbw.fame.util.Types;

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
     * @param param
     * @return Page<Comment>
     */
    Page<Comment> getComments(Integer page, Integer limit, CommentParam param);

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
     * @return 删除是否成功
     */
    boolean deleteComment(Integer id);

    /**
     * 顶或踩评论
     *
     * @param commentId 评论给id
     * @param assess    {@link Types#AGREE},{@link Types#DISAGREE}
     */
    void assessComment(Integer commentId, String assess);

    /**
     * 评论数量
     *
     * @return 数量
     */
    Integer count();
}
