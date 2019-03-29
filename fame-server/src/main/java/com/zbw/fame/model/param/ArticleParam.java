package com.zbw.fame.model.param;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * Article 查询条件 param
 *
 * @author zbw
 * @since 2019/3/28 20:19
 */
@Data
@Builder
@ToString
public class ArticleParam {

    private Integer id;

    private String title;

    private String type;

    private String status;

    @Builder.Default
    private boolean html = true;

    @Builder.Default
    private boolean summary = false;
}
