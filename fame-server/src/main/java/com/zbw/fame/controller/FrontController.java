package com.zbw.fame.controller;


import com.zbw.fame.model.domain.Article;
import com.zbw.fame.model.domain.Comment;
import com.zbw.fame.model.dto.*;
import com.zbw.fame.service.*;
import com.zbw.fame.util.FameConsts;
import com.zbw.fame.util.FameUtil;
import com.zbw.fame.util.RestResponse;
import com.zbw.fame.util.Types;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 博客前台 Controller
 *
 * @author zbw
 * @since 2017/7/15 18:29
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FrontController extends BaseController {

    private final ArticleService articleService;

    private final MetaService metaService;

    private final CommentService commentService;

    private final EmailService emailService;

    private final OptionService optionService;

    /**
     * 文章列表
     *
     * @param page  第几页
     * @param limit 每页数量
     * @return {@see Pagination<Article>}
     */
    @GetMapping("article")
    public RestResponse home(@RequestParam(required = false, defaultValue = "0") Integer page,
                             @RequestParam(required = false, defaultValue = FameConsts.PAGE_SIZE) Integer limit) {
        Page<Article> articles = articleService.getFrontArticles(page, limit);
        return RestResponse.ok(Pagination.of(articles));
    }

    /**
     * 文章内容页
     *
     * @param id 文章id
     * @return {@see Article}
     */
    @GetMapping("article/{id}")
    public RestResponse article(@PathVariable Integer id) {
        Article article = articleService.getFrontArticle(id);
        if (null == article) {
            return this.error404();
        }
        this.updateHits(article.getId(), article.getHits());
        return RestResponse.ok(article);
    }

    /**
     * 点击量添加
     *
     * @param articleId 文章id
     * @param hits      当前点击量
     */
    private void updateHits(Integer articleId, Integer hits) {
        Integer cHits = cacheUtil.getCacheValue(FameConsts.CACHE_ARTICLE_HITS, articleId, Integer.class);
        cHits = null == cHits ? 1 : cHits + 1;
        if (cHits >= FameConsts.CACHE_ARTICLE_HITS_SAVE) {
            articleService.updateHits(articleId, hits + cHits);
            cacheUtil.putCacheValue(FameConsts.CACHE_ARTICLE_HITS, articleId, 0);
        } else {
            cacheUtil.putCacheValue(FameConsts.CACHE_ARTICLE_HITS, articleId, cHits);
        }
    }


    /**
     * 标签页
     *
     * @return {@see List<MetaInfo>}
     */
    @GetMapping("tag")
    public RestResponse tag() {
        List<MetaInfo> metaInfos = metaService.getPublishMetaInfos(Types.TAG);
        return RestResponse.ok(metaInfos);
    }

    /**
     * 分类页
     *
     * @return {@see List<MetaInfo>}
     */
    @GetMapping("/category")
    public RestResponse category() {
        List<MetaInfo> metaInfos = metaService.getPublishMetaInfos(Types.CATEGORY);
        return RestResponse.ok(metaInfos);
    }

    /**
     * 归档页
     *
     * @return {@see List<Archive>}
     */
    @GetMapping("archive")
    public RestResponse archive() {
        List<Archive> archives = articleService.getArchives();
        return RestResponse.ok(archives);
    }

    /**
     * 自定义页面
     *
     * @param title 页面标题
     * @return {@see Article}
     */
    @GetMapping("page/{title}")
    public RestResponse page(@PathVariable String title) {
        Article page = articleService.getFrontPage(title);
        if (null == page) {
            return error404();
        }
        return RestResponse.ok(page);
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
    public RestResponse getArticleComment(@RequestParam Integer articleId, @RequestParam(required = false, defaultValue = "0") Integer page,
                                          @RequestParam(required = false, defaultValue = FameConsts.PAGE_SIZE) Integer limit) {
        org.springframework.data.domain.Page<Comment> comments = commentService.getCommentsByArticleId(page, limit, articleId);
        return RestResponse.ok(Pagination.of(comments));
    }


    /**
     * 发表评论
     *
     * @param articleId 文章id
     * @param pId       父评论id
     * @param content   评论内容
     * @param name      评论用户名
     * @param email     评论用户email
     * @param website   评论用户网址
     * @return {@see RestResponse.ok()}
     */
    @PostMapping("comment")
    public RestResponse postComment(@RequestParam Integer articleId, @RequestParam(required = false) Integer pId,
                                    @RequestParam String content, @RequestParam String name,
                                    @RequestParam(required = false) String email, @RequestParam(required = false) String website) {
        Comment comments = new Comment();
        comments.setArticleId(articleId);
        comments.setPId(pId);
        comments.setContent(content);
        comments.setName(name);
        comments.setEmail(email);
        comments.setWebsite(website);
        comments.setIp(FameUtil.getIp());
        comments.setAgent(FameUtil.getAgent());
        commentService.save(comments);

        //发送邮件提醒
        CommentDto commentDetail = commentService.getCommentDetail(comments.getId());
        emailService.sendEmailToAdmin(commentDetail);
        if (null != commentDetail.getPComment() && !StringUtils.isEmpty(commentDetail.getPComment().getEmail())) {
            emailService.sendEmailToUser(commentDetail, commentDetail.getPComment().getEmail());
        }
        return RestResponse.ok();
    }

    /**
     * 顶或踩评论
     *
     * @param commentId 评论id
     * @param assess    {@link Types#AGREE},{@link Types#DISAGREE}
     * @return {@see RestResponse.ok()}
     */
    @PostMapping("comment/{commentId}/assess")
    public RestResponse assessComment(@PathVariable Integer commentId, @RequestParam String assess) {
        commentService.assessComment(commentId, assess);
        return RestResponse.ok();
    }

    /**
     * 获取前端的设置
     *
     * @return Map
     */
    @GetMapping("option")
    public RestResponse getOption() {
        Map<String, String> map = optionService.getFrontOptionMap();
        return RestResponse.ok(map);
    }
}
