package com.zbw.fame.model.param;

import com.zbw.fame.model.enums.ArticleStatus;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;

/**
 * @author by zzzzbw
 * @since 2021/01/20 10:57
 */
@Data
public class SavePostParam {

    private Integer id;

    @NotBlank(message = "标题不允许为空")
    private String title;

    @NotBlank(message = "内容不允许为空")
    private String content;

    private String tags;

    private String category;

    private ArticleStatus status = ArticleStatus.DRAFT;

    private Integer priority = 0;

    private Boolean allowComment = false;

    private Long created = System.currentTimeMillis();

    private Long modified = System.currentTimeMillis();

}
