package com.zbw.fame.model.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;

/**
 * 属性(分类和标签) Model
 *
 * @author zbw
 * @since 2017/8/28 23:04
 */
@Entity
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING, columnDefinition = "VARCHAR(45)")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class Meta extends BaseEntity {

    /**
     * 属性名
     */
    @Column(name = "name", columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;
}
