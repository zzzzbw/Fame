package com.zbw.fame.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author by zzzzbw
 * @since 2021/03/08 19:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Media extends BaseEntity {

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件url
     */
    private String url;


    /**
     * 缩略图url
     */
    private String thumbUrl;

    /**
     * 后缀
     */
    private String suffix;
}
