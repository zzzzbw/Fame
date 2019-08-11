package com.zbw.fame.model.domain;

import com.zbw.fame.model.enums.CommentStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

/**
 * 评论 Model
 *
 * @author zbw
 * @since 2018/1/19 16:27
 */
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class Comment extends BaseEntity {

    /**
     * 所属文章id
     */
    @Column(name = "article_id", columnDefinition = "INT NOT NULL")
    private Integer articleId;

    /**
     * 父评论id
     */
    @Column(name = "parent_id", columnDefinition = "INT")
    private Integer parentId;

    /**
     * 评论内容
     */
    @Column(name = "content", columnDefinition = "TEXT NOT NULL")
    private String content;

    /**
     * 昵称
     */
    @Column(name = "name", columnDefinition = "VARCHAR(255)")
    private String name;

    /**
     * 邮箱
     */
    @Column(name = "email", columnDefinition = "VARCHAR(255)")
    private String email;

    /**
     * 网址
     */
    @Column(name = "website", columnDefinition = "VARCHAR(255)")
    private String website;

    /**
     * 赞
     */
    @Column(name = "agree", columnDefinition = "INT NOT NULL DEFAULT 0")
    private Integer agree;

    /**
     * 踩
     */
    @Column(name = "disagree", columnDefinition = "INT NOT NULL DEFAULT 0")
    private Integer disagree;

    /**
     * 评论ip
     */
    @Column(name = "ip", columnDefinition = "VARCHAR(255)")
    private String ip;

    /**
     * 评论agent
     */
    @Column(name = "agent", columnDefinition = "VARCHAR(255)")
    private String agent;

    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "VARCHAR(32)")
    private CommentStatus status;

    @PrePersist
    @Override
    protected void prePersist() {
        super.prePersist();

        if (null == agree) {
            agree = 0;
        }
        if (null == disagree) {
            disagree = 0;
        }
        if (null == status) {
            status = CommentStatus.NORMAL;
        }
    }
}
