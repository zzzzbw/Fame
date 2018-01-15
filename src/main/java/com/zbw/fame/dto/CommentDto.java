package com.zbw.fame.dto;

import com.zbw.fame.model.Articles;
import com.zbw.fame.model.Comments;

/**
 * 评论 Dto
 *
 * @author zbw
 * @create 2018/1/21 16:08
 */
public class CommentDto extends Comments {

    /**
     * 评论文章
     */
    private Articles article;

    /**
     * 父评论
     */
    private Comments pComment;

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public Comments getpComment() {
        return pComment;
    }

    public void setpComment(Comments pComment) {
        this.pComment = pComment;
    }
}
