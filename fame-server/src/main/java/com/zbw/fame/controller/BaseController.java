package com.zbw.fame.controller;

import com.zbw.fame.model.domain.User;
import com.zbw.fame.util.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * 公共类 Controller
 *
 * @author zbw
 * @since 2017/7/8 10:25
 */
public abstract class BaseController {

    @Autowired
    protected HttpServletRequest request;

    protected User user() {
        return FameUtil.getLoginUser();
    }

    protected RestResponse error404() {
        return RestResponse.fail(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMsg());
    }
}
