package com.zbw.fame.model.dto;

import com.zbw.fame.model.domain.Article;
import com.zbw.fame.model.domain.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 评论 Dto
 *
 * @author zbw
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
