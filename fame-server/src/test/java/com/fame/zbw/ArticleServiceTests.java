package com.fame.zbw;

import com.zbw.fame.model.domain.Article;
import com.zbw.fame.model.dto.Pagination;
import com.zbw.fame.model.query.ArticleQuery;
import com.zbw.fame.repository.ArticleRepository;
import com.zbw.fame.service.ArticleService;
import com.zbw.fame.util.Types;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * @author zhangbowen
 * @since 2019/6/24 18:23
 */
@Slf4j
public class ArticleServiceTests extends BaseTests {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void test1() {
        log.info("{}", articleService.count());
    }

    @Test
    public void test2() {
        Article article = new Article();
        article.setTitle("Hello Word 2");
        article.setContent("### Hello Word 2");
        article.setTags("First");
        article.setCategory("New");
        article.setStatus(Types.PUBLISH);
        article.setAllowComment(true);
        article.setAuthorId(1);
        article.setCreated(new java.util.Date());
        article.setModified(new java.util.Date());
        articleService.saveArticle(article);
    }

    @Test
    public void test3() {
        ArticleQuery query = new ArticleQuery();
        query.setTag("test");

        Page<Article> page = articleService.getAdminArticles(0, 10, query);
        log.info("{}", Pagination.of(page));
    }

    @Test
    public void test4() {
        Page<Article> page = articleService.getAdminPages(0, 10);
        log.info("{}", Pagination.of(page));
    }

    @Test
    public void test5() {
        articleService.deletePage(2);
    }

}
