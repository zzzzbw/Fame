package com.zbw.fame.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author by zzzzbw
 * @since 2021/03/19 10:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Category extends BaseEntity {

    private Integer parentId;

    private String name;
}
