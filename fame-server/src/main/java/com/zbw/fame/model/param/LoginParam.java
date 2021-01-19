package com.zbw.fame.model.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author by zzzzbw
 * @since 2021/1/19 20:36
 */
@Data
public class LoginParam {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
}
