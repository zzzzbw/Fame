package com.zbw.fame.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zbw.fame.model.dto.CommentDto;
import com.zbw.fame.model.entity.Comment;
import com.zbw.fame.model.enums.CommentAssessType;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.Map;

/**
 * 评论 Service 接口
 *
 * @author zzzzbw
 * @since 2018/1/19 16:56
 */
public interface CommentService extends IService<com.zbw.fame.model.entity.Comment> {
    /**
     * 保存评论
     *
     * @param comment 评论entity
     */
    void createComment(@NonNull Comment comment);

    /**
     * 获取文章下的评论
     *
     * @param current   第几页
     * @param size      每页数量
     * @param articleId 文章id
     * @return Page<Comment>
     */
    Page<Comment> pageByArticleId(Integer current, Integer size, Integer articleId);

    /**
     * 获取文章下的评论
     *
     * @param current 第几页
     * @param size    每页数量
     * @return Page<Comment>
     */
    Page<Comment> pageCommentAdmin(Integer current, Integer size);

    /**
     * 获取评论详情
     *
     * @param id 评论id
     * @return CommentDto
     */
    CommentDto getCommentDto(Integer id);

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
    int deleteByArticleId(Integer articleId);

    /**
     * 顶或踩评论
     *
     * @param commentId 评论给id
     * @param assess    点评类型 {@link CommentAssessType}
     */
    void assessComment(Integer commentId, CommentAssessType assess);

    /**
     * 新增评论
     *
     * @param comment 评论id
     */
    void createCommentEvent(Comment comment);

    /**
     * 文章评论数量
     *
     * @param articleId
     * @return
     */
    int countByArticleId(Integer articleId);

    /**
     * 查询文章下的评论数量
     *
     * @param articleIds
     * @return
     */
    Map<Integer, Long> countByArticleIds(Collection<Integer> articleIds);
}
