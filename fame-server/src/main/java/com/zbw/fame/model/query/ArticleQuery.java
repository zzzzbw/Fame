package com.zbw.fame.model.query;

import com.zbw.fame.model.enums.ArticleStatus;
import lombok.Data;
import lombok.ToString;

/**
 * 文章查询条件
 *
 * @author zbw
 * @since 2019/5/1 12:33
 */
@Data
public class ArticleQuery {

    private String title;

    private ArticleStatus status;

    private Integer priority;

}
