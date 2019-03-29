package com.zbw.fame.model.param;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * Comment 查询条件 param
 *
 * @author zbw
 * @since 2019/3/29 12:57
 */
@Data
@Builder
@ToString
public class CommentParam {

    private Integer id;

    private Integer articleId;

    @Builder.Default
    private boolean summary = false;

    @Builder.Default
    private boolean html = true;

}
