package com.zbw.fame.controller.admin;

import com.zbw.fame.model.dto.Pagination;
import com.zbw.fame.model.domain.Article;
import com.zbw.fame.model.query.ArticleQuery;
import com.zbw.fame.service.ArticleService;
import com.zbw.fame.util.FameConsts;
import com.zbw.fame.util.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 后台文章管理 Controller
 *
 * @author zbw
 * @since 2017/7/11 19:52
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public abstract class AbstractArticleController<ARTICLE extends Article> {

    private final ArticleService<ARTICLE> articleService;


    /**
     * 文章信息列表
     *
     * @param page  第几页
     * @param limit 每页数量
     * @return {@see Pagination<Article>}
     */
    @GetMapping
    public RestResponse<Pagination<ARTICLE>> page(@RequestParam(required = false, defaultValue = "0") Integer page,
                                                  @RequestParam(required = false, defaultValue = FameConsts.PAGE_SIZE) Integer limit, ArticleQuery query) {
        Page<ARTICLE> articles = articleService.pageAdminArticle(page, limit, query);
        return RestResponse.ok(Pagination.of(articles));
    }

    /**
     * 单个文章信息
     *
     * @param id 文章id
     * @return {@see Article}
     */
    @GetMapping("{id}")
    public RestResponse<ARTICLE> get(@PathVariable Integer id) {
        ARTICLE article = articleService.getAdminArticle(id);
        return RestResponse.ok(article);
    }

    /**
     * 删除文章
     *
     * @param id 文章id
     * @return {@see RestResponse.ok()}
     */
    @DeleteMapping("{id}")
    public RestResponse delete(@PathVariable Integer id) {
        articleService.delete(id);
        return RestResponse.ok();
    }

    /**
     * 已发布文章数量
     *
     * @return {@see Integer}
     */
    @GetMapping("count")
    public RestResponse<Long> count() {
        return RestResponse.ok(articleService.count());
    }

}
