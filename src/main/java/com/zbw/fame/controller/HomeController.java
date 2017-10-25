package com.zbw.fame.controller;

import com.github.pagehelper.Page;
import com.zbw.fame.dto.Archives;
import com.zbw.fame.dto.MetaDto;
import com.zbw.fame.dto.Pagination;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 博客前台 Controller
 *
 * @author zbw
 * @create 2017/7/15 18:29
 */
@RestController
@RequestMapping("")
public class HomeController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private ArticlesService articlesService;

    @Autowired
    private MetasService metasService;

    /**
     * 文章列表
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "/article", method = RequestMethod.GET)
    public RestResponse home(@RequestParam(required = false, defaultValue = "1") Integer page,
                              @RequestParam(required = false, defaultValue = FameConsts.PAGE_SIZE) Integer limit) {
        Page<Articles> articles = articlesService.getContents(page, limit);
        for (Articles a : articles) {
            this.transformPreView(a);
        }
        return RestResponse.ok(new Pagination<Articles>(articles));
    }

    /**
     * 文章内容页
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/article/{id}", method = RequestMethod.GET)
    public RestResponse content(@PathVariable Integer id) {
        Articles article = articlesService.get(id);
        if (null == article || Types.DRAFT.equals(article.getStatus())) {
            return this.error404();
        }
        this.transformContent(article);
        this.updateHits(article.getId(), article.getHits());
        return RestResponse.ok(article);
    }

    /**
     * 点击量添加
     *
     * @param articleId
     * @param hits
     */
    private void updateHits(Integer articleId, Integer hits) {
        Integer cHits = cache.get(FameConsts.CACHE_ARTICLE_HITS, articleId.toString());
        cHits = null == cHits ? 1 : cHits + 1;
        if (cHits >= FameConsts.CACHE_ARTICLE_HITS_SAVE) {
            Articles temp = new Articles();
            temp.setId(articleId);
            temp.setHits(hits + cHits);
            articlesService.updateArticle(temp);
            cache.put(FameConsts.CACHE_ARTICLE_HITS, articleId.toString(), 1);
        } else {
            cache.put(FameConsts.CACHE_ARTICLE_HITS, articleId.toString(), cHits);
        }
    }


    /**
     * 标签页
     *
     * @return
     */
    @RequestMapping(value = "/tag", method = RequestMethod.GET)
    public RestResponse tag() {
        List<MetaDto> metaDtos = metasService.getMetaDtos(Types.TAG);
        return RestResponse.ok(metaDtos);
    }

    /**
     * 分类页
     *
     * @return
     */
    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public RestResponse category() {
        List<MetaDto> metaDtos = metasService.getMetaDtos(Types.CATEGORY);
        return RestResponse.ok(metaDtos);
    }

    /**
     * 归档页
     *
     * @return
     */
    @RequestMapping(value = "/archive", method = RequestMethod.GET)
    public RestResponse archive() {
        Integer maxLimit = 9999;
        List<Articles> articles = articlesService.getContents(1, maxLimit);
        List<Archives> archives = new ArrayList<>();
        String current = "";
        for (Articles article : articles) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(article.getCreated());
            String dateStr = cal.get(Calendar.YEAR) + "";
            if (dateStr.equals(current)) {
                Archives arc = archives.get(archives.size() - 1);
                arc.getArticles().add(article);
                arc.setCount(arc.getArticles().size());
            } else {
                current = dateStr;
                Archives arc = new Archives();
                arc.setDateStr(dateStr);
                arc.setCount(1);
                List<Articles> arts = new ArrayList<>();
                arts.add(article);
                arc.setArticles(arts);
                archives.add(arc);
            }
        }
        return RestResponse.ok(archives);
    }

    /**
     * 自定义页面
     *
     * @param title
     * @return
     */
    @RequestMapping(value = "/page/{title}", method = RequestMethod.GET)
    public RestResponse page(@PathVariable String title) {
        Articles page = articlesService.getPage(title);
        if (null == page) {
            return error404();
        }
        transformContent(page);
        return RestResponse.ok(page);
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

    /**
     * 文章内容转为预览html
     *
     * @param article
     */
    private void transformPreView(Articles article) {
        String html = FameUtil.mdToHtml(FameUtil.getPreView(article.getContent()));
        article.setContent(html);
    }


}
