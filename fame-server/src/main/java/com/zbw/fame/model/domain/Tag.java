package com.zbw.fame.model.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author zhangbowen
 * @since 2019/7/19 14:55
 */
@Entity
@DiscriminatorValue("tag")
public class Tag extends Meta {
}
