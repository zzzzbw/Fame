package com.zbw.fame.model.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * BaseModel类
 *
 * @author zbw
 * @since 2017/7/5 23:59
 */
@MappedSuperclass
@Data
public class BaseEntity {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}
