package com.zbw.fame.model.param;

import com.zbw.fame.model.enums.ArticleStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author by zzzzbw
 * @since 2021/01/20 10:57
 */
@Data
public class SaveNoteParam {

    private Integer id;

    @NotBlank(message = "标题不允许为空")
    private String title;

    @NotBlank(message = "内容不允许为空")
    private String content;

    private ArticleStatus status = ArticleStatus.DRAFT;

    private Integer priority = 0;

    private Boolean allowComment = false;
}
