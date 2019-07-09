package com.zbw.fame.model.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author zhangbowen
 * @since 2019/7/9 16:09
 */
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class Media extends BaseEntity {

    /**
     * 文件名
     */
    @Column(name = "name", columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    /**
     * 文件url
     */
    @Column(name = "url", columnDefinition = "VARCHAR(1023) NOT NULL")
    private String url;


    /**
     * 缩略图url
     */
    @Column(name = "thumb_url", columnDefinition = "VARCHAR(1023)")
    private String thumbUrl;

    /**
     * 后缀
     */
    @Column(name = "suffix", columnDefinition = "VARCHAR(255) NOT NULL")
    private String suffix;
}
