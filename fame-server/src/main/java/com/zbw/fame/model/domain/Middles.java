package com.zbw.fame.model.domain;

import com.zbw.fame.model.domain.BaseEntity;
import lombok.*;

/**
 * 关联标签和文章的中间 Model
 *
 * @author zbw
 * @since 2017/9/17 23:37
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class Middles extends BaseEntity {

    private Integer aId;

    private Integer mId;
}
