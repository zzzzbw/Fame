package com.zbw.fame.model.param;

import com.zbw.fame.model.enums.ArticleStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

/**
 * @author by zzzzbw
 * @since 2021/01/20 10:57
 */
@Data
public class SaveArticleParam {

    private Integer id;

    @NotBlank(message = "标题不允许为空")
    private String title;

    @NotBlank(message = "内容不允许为空")
    private String content;

    private Set<Integer> tagIds;

    private Integer categoryId;

    private ArticleStatus status = ArticleStatus.DRAFT;

    private Boolean listShow = false;

    private Boolean headerShow = false;

    private Integer priority = 0;

    private Boolean allowComment = false;

    @NotNull(message = "创建时间不能为空")
    private Date created;

    @NotNull(message = "更新时间不能为空")
    private Date modified;

}
