package com.zbw.fame.mapper;

import com.zbw.fame.Application;
import com.zbw.fame.model.Articles;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by zbw on 2017/10/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ArticlesMapperTest {

    private final Logger logger= LoggerFactory.getLogger(ArticlesMapperTest.class);

    @Autowired
    private ArticlesMapper articlesMapper;

    @Test
    public void findOneTest(){
        Articles article = articlesMapper.selectByPrimaryKey(1);
        System.out.println(article.toString());
    }

    @Test
    @Transactional
    public void updateOneTest(){
        Articles article = articlesMapper.selectByPrimaryKey(1);
        logger.info(article.getModified().toString());
        article.setModified(new Date());
        articlesMapper.updateByPrimaryKey(article);
        logger.info(article.getModified().toString());
    }


}