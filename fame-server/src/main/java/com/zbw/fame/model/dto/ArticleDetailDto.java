package com.zbw.fame.model.dto;

import com.zbw.fame.model.entity.Category;
import com.zbw.fame.model.entity.Tag;
import com.zbw.fame.model.enums.ArticleStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author by zzzzbw
 * @since 2021/03/29 16:02
 */
@Data
public class ArticleDetailDto {

    private Integer id;

    private String title;

    private String content;

    private String contentHtml;

    private Integer authorId;

    private Integer hits;

    private ArticleStatus status;

    private boolean listShow;

    private boolean headerShow;

    private Integer priority;

    private Boolean allowComment;

    private Integer commentCount;

    private Category category;

    private List<Tag> tags;

    private Date created;

    private Date modified;
}
