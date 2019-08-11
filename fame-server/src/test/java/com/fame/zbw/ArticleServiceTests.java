package com.fame.zbw;

import com.zbw.fame.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhangbowen
 * @since 2019/6/24 18:23
 */
@Slf4j
public class ArticleServiceTests extends BaseTests {

    @Autowired
    private PostService postService;

    @Test
    public void test1() {
        postService.getFrontArticle(100);
    }
}
