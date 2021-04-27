package com.zbw.fame.model.param;

import com.zbw.fame.model.enums.ArticleStatus;
import lombok.Data;

/**
 * 文章查询条件
 *
 * @author zzzzbw
 * @since 2019/5/1 12:33
 */
@Data
public class ArticleQuery {

    private String title;

    private ArticleStatus status;

    private Integer priority;

    private Boolean listShow;

    private Boolean headerShow;
}
