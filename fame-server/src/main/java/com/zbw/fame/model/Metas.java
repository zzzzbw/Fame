package com.zbw.fame.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 属性(分类和标签) Model
 *
 * @author zbw
 * @since 2017/8/28 23:04
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
public class Metas extends BaseEntity {

    /**
     * 属性名
     */
    private String name;

    /**
     * 属性类型
     */
    private String type;
}
