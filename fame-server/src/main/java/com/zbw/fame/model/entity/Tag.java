package com.zbw.fame.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author by zzzzbw
 * @since 2021/03/15 11:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Tag extends BaseEntity {

    private String name;
}
