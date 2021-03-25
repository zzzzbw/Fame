package com.zbw.fame.service;

import com.zbw.fame.BaseTest;
import com.zbw.fame.model.dto.ArchiveDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author zzzzbw
 * @since 2019/6/24 18:23
 */
@Slf4j
public class ArticleServiceTest extends BaseTest {


    @Autowired
    private ArticleServiceNew articleServiceNew;

    @Test
    public void getArticleFront() {
        articleServiceNew.getArticleFront(1);
    }

    @Test
    public void visitPost() throws InterruptedException {
        articleServiceNew.visitArticle(1);
        Thread.sleep(1000); // 等待异步线程完成
    }

    @Test
    public void increaseHits() {
        articleServiceNew.increaseHits(1, 10);
    }

    @Test
    public void getArchives() {
        List<ArchiveDto> archives = articleServiceNew.getArchives();
        log.info("{}", archives);
    }
}
