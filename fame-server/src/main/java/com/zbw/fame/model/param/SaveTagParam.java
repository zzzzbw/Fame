package com.zbw.fame.model.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author by zzzzbw
 * @since 2021/03/15 13:44
 */
@Data
public class SaveTagParam {

    private Integer id;

    @NotBlank(message = "名称不允许为空")
    private String name;
}
