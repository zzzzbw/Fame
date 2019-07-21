package com.zbw.fame.model.dto;

import com.zbw.fame.model.domain.Post;
import lombok.Data;

import java.util.Date;

/**
 * 文章信息Dto,用于一些列表页
 *
 * @author zbw
 * @since 2018/8/28 14:34
 */
@Data
public class PostInfo {

    public PostInfo() {
    }

    public PostInfo(Post post) {
        super();
        this.id = post.getId();
        this.title = post.getTitle();
        this.created = post.getCreated();
    }


    /**
     * id
     */
    private Integer id;

    /**
     * 内容标题
     */
    private String title;

    /**
     * 创建时间
     */
    private Date created;
}
