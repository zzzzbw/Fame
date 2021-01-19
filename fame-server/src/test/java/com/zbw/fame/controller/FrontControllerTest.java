package com.zbw.fame.controller;

import com.zbw.fame.BaseMvcTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

/**
 * @author by ZHANGBOWEN469
 * @since 2021/01/20 11:29
 */
@Slf4j
class FrontControllerTest extends BaseMvcTest {

    @Test
    void addComment() throws UnsupportedEncodingException {
        String url = "/api/comment";

        MvcResult mvcResult = postJson(url, null);

        log.info("{}", mvcResult.getResponse().getContentAsString());
    }
}