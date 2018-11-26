package com.zbw.fame.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * 用户 Model
 *
 * @author zbw
 * @since 2017/7/9 22:09
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class Users extends BaseEntity {

    /**
     * 账号
     */
    private String username;

    /**
     * 用户密码 md5存储
     */
    private String passwordMd5;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户显示名称
     */
    private String screenName;

    /**
     * 用户创建时间
     */
    private Date created;

    /**
     * 最后登陆时间
     */
    private Date logged;
}
