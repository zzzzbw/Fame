package com.zbw.fame.controller;

import com.github.pagehelper.Page;
import com.zbw.fame.model.param.ArticleParam;
import com.zbw.fame.model.param.CommentParam;
import com.zbw.fame.model.dto.Archive;
import com.zbw.fame.model.dto.CommentDto;
import com.zbw.fame.model.dto.MetaDto;
import com.zbw.fame.model.dto.Pagination;
import com.zbw.fame.model.domain.Articles;
import com.zbw.fame.model.domain.Comments;
import com.zbw.fame.service.ArticlesService;
import com.zbw.fame.service.CommentsService;
import com.zbw.fame.service.EmailService;
import com.zbw.fame.service.MetasService;
import com.zbw.fame.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 博客前台 Controller
 *
 * @author zbw
 * @since 2017/7/15 18:29
 */
@RestController
@RequestMapping("/api")
public class FrontController extends BaseController {

    @Autowired
    private ArticlesService articlesService;

    @Autowired
    private MetasService metasService;

    @Autowired
    private CommentsService commentsService;

    @Autowired
    private EmailService emailService;

    /**
     * 文章列表
     *
     * @param page  第几页
     * @param limit 每页数量
     * @return {@see Pagination<Articles>}
     */
    @GetMapping("article")
    public RestResponse home(@RequestParam(required = false, defaultValue = "1") Integer page,
                             @RequestParam(required = false, defaultValue = FameConsts.PAGE_SIZE) Integer limit) {
        ArticleParam param = ArticleParam.builder()
                .type(Types.POST)
                .status(Types.PUBLISH)
                .html(true)
                .summary(true)
                .build();
        Page<Articles> articles = articlesService.getArticles(page, limit, param);
        return RestResponse.ok(new Pagination<Articles>(articles));
    }

    /**
     * 文章内容页
     *
     * @param id 文章id
     * @return {@see Articles}
     */
    @GetMapping("article/{id}")
    public RestResponse article(@PathVariable Integer id) {
        ArticleParam param = ArticleParam.builder()
                .id(id)
                .type(Types.POST)
                .status(Types.PUBLISH)
                .html(true)
                .summary(false)
                .build();

        Articles article = articlesService.getArticle(param);
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
            Articles temp = new Articles();
            temp.setId(articleId);
            temp.setHits(hits + cHits);
            articlesService.updateArticle(temp);
            cacheUtil.putCacheValue(FameConsts.CACHE_ARTICLE_HITS, articleId, 0);
        } else {
            cacheUtil.putCacheValue(FameConsts.CACHE_ARTICLE_HITS, articleId, cHits);
        }
    }


    /**
     * 标签页
     *
     * @return {@see List<MetaDto>}
     */
    @GetMapping("tag")
    public RestResponse tag() {
        List<MetaDto> metaDtos = metasService.getMetaDtos(Types.TAG);
        return RestResponse.ok(metaDtos);
    }

    /**
     * 分类页
     *
     * @return {@see List<MetaDto>}
     */
    @GetMapping("/category")
    public RestResponse category() {
        List<MetaDto> metaDtos = metasService.getMetaDtos(Types.CATEGORY);
        return RestResponse.ok(metaDtos);
    }

    /**
     * 归档页
     *
     * @return {@see List<Archive>}
     */
    @GetMapping("archive")
    public RestResponse archive() {
        List<Archive> archives = articlesService.getArchives();
        return RestResponse.ok(archives);
    }

    /**
     * 自定义页面
     *
     * @param title 页面标题
     * @return {@see Articles}
     */
    @GetMapping("page/{title}")
    public RestResponse page(@PathVariable String title) {
        ArticleParam param = ArticleParam.builder()
                .title(title)
                .type(Types.PAGE)
                .status(Types.PUBLISH)
                .html(true)
                .summary(false)
                .build();

        Articles page = articlesService.getArticle(param);
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
     * @return {@see Pagination<Comments>}
     */
    @GetMapping("comment")
    public RestResponse getArticleComment(@RequestParam Integer articleId, @RequestParam(required = false, defaultValue = "1") Integer page,
                                          @RequestParam(required = false, defaultValue = FameConsts.PAGE_SIZE) Integer limit) {
        CommentParam param = CommentParam.builder()
                .articleId(articleId)
                .build();
        Page<Comments> comments = commentsService.getComments(page, limit, param);
        return RestResponse.ok(new Pagination<Comments>(comments));
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
        Comments comments = new Comments();
        comments.setArticleId(articleId);
        comments.setPId(pId);
        comments.setContent(content);
        comments.setName(name);
        comments.setEmail(email);
        comments.setWebsite(website);
        comments.setIp(FameUtil.getIp());
        comments.setAgent(FameUtil.getAgent());
        commentsService.save(comments);

        //发送邮件提醒
        CommentDto commentDetail = commentsService.getCommentDetail(comments.getId());
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
        commentsService.assessComment(commentId, assess);
        return RestResponse.ok();
    }
}
