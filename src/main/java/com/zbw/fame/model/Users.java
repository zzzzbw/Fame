package com.zbw.fame.model;

import java.util.Date;

/**
 * 用户 Model
 *
 * @author zbw
 * @create 2017/7/9 22:09
 */
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordMd5() {
        return passwordMd5;
    }

    public void setPasswordMd5(String passwordMd5) {
        this.passwordMd5 = passwordMd5;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLogged() {
        return logged;
    }

    public void setLogged(Date logged) {
        this.logged = logged;
    }

    @Override
    public String toString() {
        return "Users{" +
                "username='" + username + '\'' +
                ", passwordMd5='" + passwordMd5 + '\'' +
                ", email='" + email + '\'' +
                ", screenName='" + screenName + '\'' +
                ", created=" + created +
                ", logged=" + logged +
                '}';
    }
}
