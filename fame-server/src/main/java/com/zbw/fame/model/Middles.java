package com.zbw.fame.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 关联标签和文章的中间 Model
 *
 * @author zbw
 * @since 2017/9/17 23:37
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
public class Middles extends BaseEntity {

    private Integer aId;

    private Integer mId;
}
