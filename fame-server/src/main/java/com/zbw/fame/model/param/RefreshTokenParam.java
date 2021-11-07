package com.zbw.fame.model.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * @author by zzzzbw
 * @since 2021/1/19 20:36
 */
@Data
public class RefreshTokenParam {
    @NotBlank
    private String refreshToken;
}
