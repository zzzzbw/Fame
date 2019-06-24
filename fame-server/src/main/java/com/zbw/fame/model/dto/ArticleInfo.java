package com.zbw.fame.model.dto;

import lombok.Data;

/**
 * 文章信息Dto,用于一些列表页
 *
 * @author zbw
 * @since 2018/8/28 14:34
 */
@Data
public class ArticleInfo {
    /**
     * id
     */
    private Integer id;

    /**
     * 内容标题
     */
    private String title;
}
