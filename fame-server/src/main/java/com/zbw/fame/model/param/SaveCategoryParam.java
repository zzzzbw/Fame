package com.zbw.fame.model.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author by zzzzbw
 * @since 2021/03/19 10:50
 */
@Data
public class SaveCategoryParam {

    private Integer id;

    private Integer parentId;

    @NotBlank(message = "名称不允许为空")
    private String name;
}
