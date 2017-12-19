package com.zbw.fame.controller;

import com.zbw.fame.model.Users;
import com.zbw.fame.util.ErrorCode;
import com.zbw.fame.util.FameUtil;
import com.zbw.fame.util.RestResponse;
import com.zbw.fame.util.SystemCache;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * 公共类 Controller
 *
 * @author zbw
 * @create 2017/7/8 10:25
 */
public abstract class BaseController {

    @Autowired
    protected HttpServletRequest request;

    protected SystemCache cache = SystemCache.instance();


    public Users user() {
        return FameUtil.getLoginUser();
    }

    protected RestResponse error404() {
        return RestResponse.fail(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMsg());
    }
}
