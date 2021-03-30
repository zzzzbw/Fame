package com.zbw.fame.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zbw.fame.BaseTest;
import com.zbw.fame.model.dto.ArchiveDto;
import com.zbw.fame.model.dto.ArticleDetailDto;
import com.zbw.fame.model.param.ArticleQuery;
import com.zbw.fame.util.FameUtils;
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
    private ArticleService articleService;

    @Test
    public void getArticleFront() {
        articleService.getArticleFront(1);
    }

    @Test
    public void visitPost() throws InterruptedException {
        articleService.visitArticle(1);
        Thread.sleep(1000); // 等待异步线程完成
    }

    @Test
    public void increaseHits() {
        articleService.increaseHits(1, 10);
    }

    @Test
    public void getArchives() {
        List<ArchiveDto> archives = articleService.getArchives();
        log.info("{}", archives);
    }

    @Test
    public void pageArticleAdmin() {
        IPage<ArticleDetailDto> articleDetailDtoIPage = articleService.pageArticleAdmin(1, 10, new ArticleQuery());
        log.info("{}", FameUtils.objectToJson(articleDetailDtoIPage));
    }
}
