package com.zbw.fame.model.param;

import com.zbw.fame.util.FameConst;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author by zzzzbw469
 * @since 2021/01/19 10:39
 */
@Data
public class AddCommentParam {
    @NotNull(message = "关联文章不允许为空")
    private Integer articleId;

    private Integer parentId;

    @NotBlank(message = "评论内容不允许为空")
    @Size(max = FameConst.MAX_COMMENT_CONTENT_COUNT, message = "评论内容长度不能超过 {max}")
    private String content;

    @NotBlank(message = "评论昵称不允许为空")
    @Size(max = FameConst.MAX_COMMENT_NAME_COUNT, message = "评论昵称长度不能超过 {max}")
    private String name;

    @Email(message = "邮箱格式错误")
    @Size(max = FameConst.MAX_COMMENT_EMAIL_COUNT, message = "邮箱长度不能超过 {max}")
    private String email;

    @URL(message = "URL网址格式错误")
    @Size(max = FameConst.MAX_COMMENT_WEBSITE_COUNT, message = "URL网址长度不能超过 {max}")
    private String website;
}
