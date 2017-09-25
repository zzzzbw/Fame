package com.zbw.fame.controller;

import com.zbw.fame.model.Users;
import com.zbw.fame.util.FameUtil;
import com.zbw.fame.util.RestResponse;
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

    protected RestResponse error_404() {
        return RestResponse.fail("访问的页面不存在");
    }
}
