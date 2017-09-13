package com.zbw.fame.controller;

import com.zbw.fame.model.Articles;
import com.zbw.fame.service.ArticlesService;
import com.zbw.fame.util.FameUtil;
import com.zbw.fame.util.RestResponse;
import com.zbw.fame.util.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 博客前台 Controller
 *
 * @auther zbw
 * @create 2017/7/15 18:29
 */
@RestController
@RequestMapping("api")
public class IndexController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private ArticlesService articlesService;

    /**
     * 首页
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public RestResponse index() {
        return this.index(1);
    }

    /**
     * 文章列表
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "/article", method = RequestMethod.GET)
    public RestResponse index(@RequestParam Integer page) {
        List<Articles> articles = articlesService.getContents(page);
        for (Articles a : articles) {
            this.transformContent(a);
        }
        return RestResponse.ok(articles);
    }

    /**
     * 文章内容页
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/article/{id}")
    public RestResponse content(@PathVariable Integer id) {
        Articles article = articlesService.get(id);
        if (null == article || Types.DRAFT.equals(article.getStatus())) {
            return this.error_404();
        }
        this.transformContent(article);
        return RestResponse.ok(article);
    }

    /**
     * 文章内容转为html
     *
     * @param article
     */
    private void transformContent(Articles article) {
        String html = FameUtil.mdToHtml(article.getContent());
        article.setContent(html);
    }


}
