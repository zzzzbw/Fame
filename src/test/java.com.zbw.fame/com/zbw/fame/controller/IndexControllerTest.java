package com.zbw.fame.controller;

import com.zbw.fame.Application;
import com.zbw.fame.model.Articles;
import com.zbw.fame.service.ArticlesService;
import com.zbw.fame.service.MetasService;
import com.zbw.fame.util.FameUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by zbw on 2017/10/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class IndexControllerTest {

    private final Logger logger = LoggerFactory.getLogger(IndexControllerTest.class);

    @Autowired
    private ArticlesService articlesService;

    @Autowired
    private MetasService metasService;

    @Test
    public void index() throws Exception {

    }

    @Test
    public void content() throws Exception {
        Integer id = 1;
        Articles article = articlesService.get(id);
        String html = FameUtil.mdToHtml(article.getContent());
        article.setContent(html);
        logger.info(article.toString());
    }

    @Test
    public void tag() throws Exception {

    }

    @Test
    public void category() throws Exception {

    }

    @Test
    public void archive() throws Exception {

    }

}