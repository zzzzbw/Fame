package com.zbw.fame.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zbw.fame.model.dto.LoginUser;
import com.zbw.fame.model.dto.Pagination;
import com.zbw.fame.model.entity.Article;
import com.zbw.fame.model.param.ArticleQuery;
import com.zbw.fame.model.param.SaveArticleParam;
import com.zbw.fame.service.ArticleServiceNew;
import com.zbw.fame.util.FameConst;
import com.zbw.fame.util.FameUtils;
import com.zbw.fame.util.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author by zzzzbw
 * @since 2021/03/11 10:50
 */
@RestController
@RequestMapping("/api/admin/article")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ArticleController {

    private final ArticleServiceNew articleServiceNew;

    /**
     * 文章信息列表
     *
     * @param page  第几页
     * @param limit 每页数量
     * @return {@see Pagination<Article>}
     */
    @GetMapping
    public RestResponse<Pagination<Article>> page(@RequestParam(required = false, defaultValue = "0") Integer page,
                                                  @RequestParam(required = false, defaultValue = FameConst.PAGE_SIZE) Integer limit, ArticleQuery query) {
        Page<Article> articles = articleServiceNew.pageArticleAdmin(page, limit, query);
        return RestResponse.ok(Pagination.of(articles));
    }

    /**
     * 单个文章信息
     *
     * @param id 文章id
     * @return {@see Article}
     */
    @GetMapping("{id}")
    public RestResponse<Article> get(@PathVariable Integer id) {
        Article article = articleServiceNew.getArticleAdmin(id);
        return RestResponse.ok(article);
    }

    /**
     * 新增或保存文章
     *
     * @return {@link RestResponse#ok()}
     */
    @PostMapping
    public RestResponse<Integer> createOrUpdate(@RequestBody @Valid SaveArticleParam param) {
        Article article = FameUtils.convertTo(param, Article.class);
        LoginUser user = FameUtils.getLoginUser();
        article.setAuthorId(user.getId());
        Integer postId = articleServiceNew.createOrUpdate(article);
        return RestResponse.ok(postId);
    }

    /**
     * 删除文章
     *
     * @param id 文章id
     * @return {@see RestResponse.ok()}
     */
    @DeleteMapping("{id}")
    public RestResponse<RestResponse.Empty> delete(@PathVariable Integer id) {
        articleServiceNew.delete(id);
        return RestResponse.ok();
    }

    /**
     * 已发布文章数量
     *
     * @return {@see Integer}
     */
    @GetMapping("count")
    public RestResponse<Integer> count() {
        return RestResponse.ok(articleServiceNew.count());
    }
}
