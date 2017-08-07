package com.zbw.fame.controller;

import com.zbw.fame.model.Users;
import com.zbw.fame.util.FameUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * 公共类 Controller
 *
 * @auther zbw
 * @create 2017/7/8 10:25
 */
public abstract class BaseController {
    @Autowired
    protected HttpServletRequest request;


    public Users user() {
        return FameUtil.getLoginUser();
    }

}
