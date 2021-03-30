package com.zbw.fame.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zbw.fame.model.dto.*;
import com.zbw.fame.model.entity.Article;
import com.zbw.fame.model.entity.Comment;
import com.zbw.fame.model.enums.CommentAssessType;
import com.zbw.fame.model.param.AddCommentParam;
import com.zbw.fame.service.*;
import com.zbw.fame.util.FameConst;
import com.zbw.fame.util.FameUtils;
import com.zbw.fame.util.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 博客前台 Controller
 *
 * @author zzzzbw
 * @since 2017/7/15 18:29
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FrontController {
    private final ArticleService articleService;

    private final CategoryService categoryService;

    private final TagService tagService;

    private final CommentService commentService;

    private final SysOptionService sysOptionService;

    /**
     * 文章列表
     *
     * @param page  第几页
     * @param limit 每页数量
     * @return {@see Pagination<ArticleDetailDto>}
     */
    @GetMapping("post")
    public RestResponse<Pagination<ArticleDetailDto>> home(@RequestParam(required = false, defaultValue = "0") Integer page,
                                                  @RequestParam(required = false, defaultValue = FameConst.PAGE_SIZE) Integer limit,
                                                  @RequestParam(required = false, defaultValue = "id") List<String> sort) {
        IPage<ArticleDetailDto> articles = articleService.pageArticleFront(page, limit, sort);
        return RestResponse.ok(Pagination.of(articles));
    }

    /**
     * 文章内容页
     *
     * @param id 文章id
     * @return {@see ArticleDetailDto}
     */
    @GetMapping("post/{id}")
    public RestResponse<ArticleDetailDto> post(@PathVariable Integer id) {
        ArticleDetailDto articleDetailDto = articleService.getArticleFront(id);
        articleService.visitArticle(articleDetailDto.getId());
        return RestResponse.ok(articleDetailDto);
    }


    /**
     * 标签页
     *
     * @return {@see List<TagInfoDto>}
     */
    @GetMapping("tag")
    public RestResponse<List<TagInfoDto>> tag() {
        List<TagInfoDto> tagInfos = tagService.listTagInfo(true);
        return RestResponse.ok(tagInfos);
    }

    /**
     * 分类页
     *
     * @return {@see List<CategoryInfoDto>}
     */
    @GetMapping("category")
    public RestResponse<List<CategoryInfoDto>> category() {
        List<CategoryInfoDto> categoryInfos = categoryService.listCategoryInfo(true);
        return RestResponse.ok(categoryInfos);
    }

    /**
     * 归档页
     *
     * @return {@see List<ArchiveDto>}
     */
    @GetMapping("archive")
    public RestResponse<List<ArchiveDto>> archive() {
        List<ArchiveDto> archives = articleService.getArchives();
        return RestResponse.ok(archives);
    }

    /**
     * 导航栏文章列表
     *
     * @return {@see List<ArticleInfoDto>}
     */
    @GetMapping("header")
    public RestResponse<List<ArticleInfoDto>> headerList() {
        List<ArticleInfoDto> articleHeader = articleService.listArticleHeader();
        return RestResponse.ok(articleHeader);
    }

    /**
     * 获取文章的评论
     *
     * @param articleId 文章id
     * @param page      第几页
     * @param limit     每页数量
     * @return {@see Pagination<Comment>}
     */
    @GetMapping("comment")
    public RestResponse<Pagination<Comment>> getArticleComment(@RequestParam Integer articleId, @RequestParam(required = false, defaultValue = "0") Integer page,
                                                               @RequestParam(required = false, defaultValue = FameConst.PAGE_SIZE) Integer limit) {
        Page<Comment> comments = commentService.pageByArticleId(page, limit, articleId);
        return RestResponse.ok(Pagination.of(comments));
    }


    /**
     * 发表评论
     *
     * @return {@link RestResponse#ok()}
     */
    @PostMapping("comment")
    public RestResponse<RestResponse.Empty> addComment(@RequestBody @Valid AddCommentParam param) {
        Comment comment = FameUtils.convertTo(param, Comment.class);
        commentService.createComment(comment);
        return RestResponse.ok();
    }

    /**
     * 顶或踩评论
     *
     * @param commentId 评论id
     * @param assess    点评类型 {@link CommentAssessType}
     * @return {@link RestResponse#ok()}
     */
    @PostMapping("comment/{commentId}/assess")
    public RestResponse<RestResponse.Empty> assessComment(@PathVariable Integer commentId, @RequestParam CommentAssessType assess) {
        commentService.assessComment(commentId, assess);
        return RestResponse.ok();
    }

    /**
     * 获取前端的设置
     *
     * @return Map
     */
    @GetMapping("option")
    public RestResponse<Map<String, String>> getOption() {
        Map<String, String> map = sysOptionService.getFrontOptionMap();
        return RestResponse.ok(map);
    }
}
