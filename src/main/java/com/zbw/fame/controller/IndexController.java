package com.zbw.fame.controller;

import com.zbw.fame.dto.MetaDto;
import com.zbw.fame.model.Articles;
import com.zbw.fame.service.ArticlesService;
import com.zbw.fame.service.MetasService;
import com.zbw.fame.util.FameConsts;
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
@RequestMapping("")
public class IndexController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private ArticlesService articlesService;

    @Autowired
    private MetasService metasService;

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
            this.transformPreView(a);
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
     * 标签页
     *
     * @return
     */
    @RequestMapping(value = "/tag")
    public RestResponse tags() {
        List<MetaDto> metaDtos = metasService.getMetaDtos(Types.TAG);
        return RestResponse.ok(metaDtos);
    }

    /**
     * 分类页
     *
     * @return
     */
    @RequestMapping(value = "/category")
    public RestResponse category() {
        List<MetaDto> metaDtos = metasService.getMetaDtos(Types.CATEGORY);
        return RestResponse.ok(metaDtos);
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


    private void transformPreView(Articles article) {
        String content = article.getContent();
        int index = FameUtil.ignoreCaseIndexOf(content, FameConsts.PREVIEW_FLAG);
        String html;
        if (-1 == index) {
            index = content.length() > FameConsts.MAX_PREVIEW_COUNT ? FameConsts.MAX_PREVIEW_COUNT : content.length();
            html = FameUtil.mdToHtml(content.substring(0, index));
        } else {
            html = FameUtil.mdToHtml(content.substring(0, index));
        }
        article.setContent(html);
    }


}
