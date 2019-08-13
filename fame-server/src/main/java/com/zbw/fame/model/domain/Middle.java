package com.zbw.fame.model.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 关联标签和文章的中间 Model
 *
 * @author zbw
 * @since 2017/9/17 23:37
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class Middle extends BaseEntity {

    @Column(name = "article_id", columnDefinition = "INT NOT NULL")
    private Integer articleId;

    @Column(name = "meta_id", columnDefinition = "INT NOT NULL")
    private Integer metaId;
}
