package com.zbw.fame.model.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 页面 Model
 *
 * @author zbw
 * @since 2019/7/21 19:31
 */
@Entity
@DiscriminatorValue("note")
public class Note extends Article {
}
