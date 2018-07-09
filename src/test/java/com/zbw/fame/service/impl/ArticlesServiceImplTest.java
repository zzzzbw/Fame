package com.zbw.fame.service.impl;

import com.github.pagehelper.Page;
import com.zbw.fame.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zbw on 2017/12/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ArticlesServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(ArticlesServiceImpl.class);

    @Autowired
    private ArticlesServiceImpl articlesService;

    @Test
    public void getArticles() throws Exception {
        Page page = articlesService.getArticles(1,10);
        logger.info(page.toString());
    }

    @Test
    public void getContents() throws Exception {
    }

    @Test
    public void get() throws Exception {
        logger.info(articlesService.get(1).toString());

    }

    @Test
    public void saveArticle() throws Exception {
    }

    @Test
    public void updateArticle() throws Exception {
    }

    @Test
    public void deleteArticle() throws Exception {
    }

    @Test
    public void count() throws Exception {
    }

    @Test
    public void getPages() throws Exception {
    }

    @Test
    public void getPage() throws Exception {
    }

    @Test
    public void getPage1() throws Exception {
    }

    @Test
    public void savePage() throws Exception {
    }

    @Test
    public void deletePage() throws Exception {
    }

}