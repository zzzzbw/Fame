package com.zbw.fame.dto;

import com.zbw.fame.model.Articles;
import com.zbw.fame.model.Comments;
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
public class CommentDto extends Comments {

    /**
     * 评论文章
     */
    private Articles article;

    /**
     * 父评论
     */
    private Comments pComment;
}
