package com.zbw.fame.model.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 博文 Model
 *
 * @author zbw
 * @since 2019/7/21 19:24
 */
@Entity
@DiscriminatorValue("post")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class Post extends Article {

    /**
     * 标签列表
     */
    @Column(name = "tags", columnDefinition = "VARCHAR(500)")
    private String tags;

    /**
     * 文章分类
     */
    @Column(name = "category", columnDefinition = "VARCHAR(500)")
    private String category;

}
