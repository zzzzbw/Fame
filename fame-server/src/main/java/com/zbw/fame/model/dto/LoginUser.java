package com.zbw.fame.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author by zzzzbw
 * @since 2021/03/08 18:24
 */
@Data
public class LoginUser {

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 账号
     */
    private String username;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户显示名称
     */
    private String screenName;

    /**
     * 最后登陆时间
     */
    private Date logged;
}
