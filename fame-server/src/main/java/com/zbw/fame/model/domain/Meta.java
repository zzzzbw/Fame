package com.zbw.fame.model.domain;

import com.zbw.fame.model.domain.BaseEntity;
import lombok.*;

/**
 * 属性(分类和标签) Model
 *
 * @author zbw
 * @since 2017/8/28 23:04
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class Meta extends BaseEntity {

    /**
     * 属性名
     */
    private String name;

    /**
     * 属性类型
     */
    private String type;
}
