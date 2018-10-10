package com.zbw.fame.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.OrderBy;
import java.util.Date;

/**
 * 文章 Model
 *
 * @author zbw
 * @since 2017/7/8 9:29
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class Articles extends BaseEntity {

    /**
     * 内容标题
     */
    private String title;

    /**
     * 内容生成时间
     */
    @OrderBy("desc")
    private Date created;

    /**
     * 内容修改时间
     */
    private Date modified;

    /**
     * 内容
     */
    private String content;

    /**
     * 内容所属用户id
     */
    private Integer authorId;

    /**
     * 点击量
     */
    private Integer hits;

    /**
     * 标签列表
     */
    private String tags;

    /**
     * 文章分类
     */
    private String category;

    /**
     * 内容状态
     */
    private String status;

    /**
     * 内容类别
     */
    private String type;

    /**
     * 是否允许评论
     */
    private Boolean allowComment;
}
