package com.zbw.fame.controller.admin;

import com.zbw.fame.controller.BaseController;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.model.Articles;
import com.zbw.fame.model.Users;
import com.zbw.fame.service.ArticlesService;
import com.zbw.fame.util.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 后台文章管理 Controller
 *
 * @auther zbw
 * @create 2017/7/11 19:52
 */
@RestController
@RequestMapping("/api/admin/article")
public class ArticleController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private ArticlesService articlesService;


    /**
     * 文章信息列表
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "/index/{page}")
    public RestResponse index(@PathVariable Integer page) {
        List<Articles> articles = articlesService.getArticles(page);
        return RestResponse.ok(articles);
    }

    /**
     * 单个文章信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/show/{id}")
    public RestResponse showArticle(@PathVariable Integer id) {
        Articles article = articlesService.get(id);
        if (null == article) {
            return this.error_404();
        }
        return RestResponse.ok(article);
    }

    /**
     * 保存文章
     *
     * @param article
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RestResponse saveArticle(Articles article) {
        Users user = this.user();
        if (null == user) {
            return RestResponse.fail("未登陆，请先登陆");
        }
        article.setAuthor_id(user.getId());
        try {
            Integer id = articlesService.saveArticle(article);
        } catch (Exception e) {
            String msg = "文章发布失败";
            if (e instanceof TipException) {
                msg = e.getMessage();
            } else {
                logger.error(msg, e);
            }
            return RestResponse.fail(msg);
        }

        return RestResponse.ok("保存文章成功");
    }

    /**
     * 删除文章
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
    public RestResponse deleteArticle(@PathVariable Integer id) {
        if (articlesService.deleteArticle(id)) {
            return RestResponse.ok();
        } else {
            return RestResponse.fail();
        }
    }

}
