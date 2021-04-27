package com.zbw.fame.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zbw.fame.model.dto.ArticleDetailDto;
import com.zbw.fame.model.dto.Pagination;
import com.zbw.fame.model.entity.Article;
import com.zbw.fame.model.param.ArticleQuery;
import com.zbw.fame.model.param.SaveArticleParam;
import com.zbw.fame.service.ArticleService;
import com.zbw.fame.util.FameConst;
import com.zbw.fame.util.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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

    private final ArticleService articleService;

    /**
     * 文章信息列表
     *
     * @param page  第几页
     * @param limit 每页数量
     * @return {@see Pagination<ArticleDetailDto>}
     */
    @GetMapping
    public RestResponse<Pagination<ArticleDetailDto>> page(@RequestParam(required = false, defaultValue = FameConst.DEFAULT_PAGE) Integer page,
                                                           @RequestParam(required = false, defaultValue = FameConst.PAGE_SIZE) Integer limit,
                                                           ArticleQuery query) {
        IPage<ArticleDetailDto> articles = articleService.pageArticleAdmin(page, limit, query);
        return RestResponse.ok(Pagination.of(articles));
    }

    /**
     * 单个文章信息
     *
     * @param id 文章id
     * @return {@see ArticleDetailDto}
     */
    @GetMapping("{id}")
    public RestResponse<ArticleDetailDto> get(@PathVariable Integer id) {
        ArticleDetailDto articleDetailDto = articleService.getArticleAdmin(id);
        return RestResponse.ok(articleDetailDto);
    }

    /**
     * 新增或保存文章
     *
     * @return {@see ArticleDetailDto}
     */
    @PostMapping
    public RestResponse<ArticleDetailDto> createOrUpdate(@RequestBody @Valid SaveArticleParam param) {
        ArticleDetailDto articleDetailDto = articleService.createOrUpdate(param);
        return RestResponse.ok(articleDetailDto);
    }

    /**
     * 删除文章
     *
     * @param id 文章id
     * @return {@see RestResponse.ok()}
     */
    @DeleteMapping("{id}")
    public RestResponse<RestResponse.Empty> delete(@PathVariable Integer id) {
        articleService.delete(id);
        return RestResponse.ok();
    }

    /**
     * 已发布文章数量
     *
     * @return {@see Integer}
     */
    @GetMapping("count")
    public RestResponse<Integer> count() {
        return RestResponse.ok(articleService.count());
    }
}
