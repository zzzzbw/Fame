package com.zbw.fame.model.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * BaseModel类
 *
 * @author zbw
 * @since 2017/7/5 23:59
 */
@MappedSuperclass
@Data
public class BaseEntity {

    /**
     * Id
     */
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 创建时间
     */
    @Column(name = "created", columnDefinition = "TIMESTAMP NOT NULL DEFAULT current_timestamp")
    private Date created;

    /**
     * 修改时间
     */
    @Column(name = "modified", columnDefinition = "TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp")
    private Date modified;

    @PrePersist
    protected void prePersist() {
        if (created == null) {
            created = new Date();
        }

        if (modified == null) {
            modified = new Date();
        }
    }


    @PreUpdate
    protected void preUpdate() {
        modified = new Date();
    }
}
