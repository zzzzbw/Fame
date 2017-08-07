package com.zbw.fame.model;

import java.util.Date;

/**
 * 用户 Model
 *
 * @auther zbw
 * @create 2017/7/9 22:09
 */
public class Users extends BaseEntity{

    //账号
    private String username;

    //用户密码 md5存储
    private String password_md5;

    //用户邮箱
    private String email;

    //用户显示名称
    private String screen_name;

    //用户创建时间
    private Date created;

    //最后登陆时间
    private Date logged;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword_md5() {
        return password_md5;
    }

    public void setPassword_md5(String password_md5) {
        this.password_md5 = password_md5;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
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
}
