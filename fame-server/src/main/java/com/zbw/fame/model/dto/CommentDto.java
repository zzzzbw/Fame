package com.zbw.fame.model.dto;

import com.zbw.fame.model.entity.Article;
import com.zbw.fame.model.entity.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 评论 Dto
 *
 * @author zzzzbw
 * @since 2018/1/21 16:08
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class CommentDto extends Comment {

    /**
     * 评论文章
     */
    private Article article;

    /**
     * 父评论
     */
    private Comment parentComment;
}
