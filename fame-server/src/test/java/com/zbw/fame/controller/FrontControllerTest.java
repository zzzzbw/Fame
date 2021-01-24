package com.zbw.fame.controller;

import com.zbw.fame.BaseMvcTest;
import com.zbw.fame.model.param.AddCommentParam;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * @author by zzzzbw
 * @since 2021/01/20 11:29
 */
@Slf4j
class FrontControllerTest extends BaseMvcTest {

    @Test
    void home() throws UnsupportedEncodingException {
        String url = "/api/post";

        MvcResult mvcResult = getJson(url);
        log.info("{}", mvcResult.getResponse().getContentAsString(Charset.defaultCharset()));
    }

    @Test
    void addComment() throws Exception {
        String url = "/api/comment";

        AddCommentParam param = new AddCommentParam();
        param.setArticleId(1);
        param.setContent("测试评论");
        param.setEmail("zzzzbw@gmail.com");
        param.setName("zzzzbw");
        MvcResult mvcResult = postJson(url, param);
        log.info("{}", mvcResult.getResponse().getContentAsString(Charset.defaultCharset()));

        Thread.sleep(10000);
    }
}