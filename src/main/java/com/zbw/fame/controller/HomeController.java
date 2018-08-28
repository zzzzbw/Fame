package com.zbw.fame.controller;

import com.github.pagehelper.Page;
import com.zbw.fame.dto.Archives;
import com.zbw.fame.dto.CommentDto;
import com.zbw.fame.dto.MetaDto;
import com.zbw.fame.dto.Pagination;
import com.zbw.fame.model.Articles;
import com.zbw.fame.model.Comments;
import com.zbw.fame.service.ArticlesService;
import com.zbw.fame.service.CommentsService;
import com.zbw.fame.service.EmailService;
import com.zbw.fame.service.MetasService;
import com.zbw.fame.util.FameConsts;
import com.zbw.fame.util.FameUtil;
import com.zbw.fame.util.RestResponse;
import com.zbw.fame.util.Types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 博客前台 Controller
 *
 * @author zbw
 * @since 2017/7/15 18:29
 */
@RestController
@RequestMapping("/api")
public class HomeController extends BaseController {

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
        Page<Articles> articles = articlesService.getContents(page, limit);
        for (Articles a : articles) {
            this.transformPreView(a);
        }
        return RestResponse.ok(new Pagination<Articles>(articles));
    }

    /**
     * 文章内容页
     *
     * @param id 文章id
     * @return {@see Articles}
     */
    @GetMapping("article/{id}")
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
     * @param articleId 文章id
     * @param hits      当前点击量
     */
    private void updateHits(Integer articleId, Integer hits) {
        Integer cHits = cache.get(FameConsts.CACHE_ARTICLE_HITS, articleId.toString());
        cHits = null == cHits ? 1 : cHits + 1;
        if (cHits >= FameConsts.CACHE_ARTICLE_HITS_SAVE) {
            Articles temp = new Articles();
            temp.setId(articleId);
            temp.setHits(hits + cHits);
            articlesService.updateArticle(temp);
            cache.put(FameConsts.CACHE_ARTICLE_HITS, articleId.toString(), 0);
        } else {
            cache.put(FameConsts.CACHE_ARTICLE_HITS, articleId.toString(), cHits);
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
     * @return {@see List<Archives>}
     */
    @GetMapping("archive")
    public RestResponse archive() {
        Integer maxLimit = 9999;
        List<Articles> articles = articlesService.getContents(1, maxLimit);
        List<Archives> archives = new ArrayList<>();
        String current = "";
        for (Articles article : articles) {
            // 清空文章内容
            article.setContent("");
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
     * @param title 页面标题
     * @return {@see Articles}
     */
    @GetMapping("page/{title}")
    public RestResponse page(@PathVariable String title) {
        Articles page = articlesService.getPage(title);
        if (null == page) {
            return error404();
        }
        transformContent(page);
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
        Page<Comments> comments = commentsService.getCommentsByArticleId(articleId, page, limit);
        for (Comments comment : comments) {
            comment.setContent(FameUtil.mdToHtml(comment.getContent()));
        }
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
        comments.setpId(pId);
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
        if (null != commentDetail.getpComment() && !StringUtils.isEmpty(commentDetail.getpComment().getEmail())) {
            emailService.sendEmailToUser(commentDetail, commentDetail.getpComment().getEmail());
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


    /**
     * 文章内容转为html
     *
     * @param article 文章实体类
     */
    private void transformContent(Articles article) {
        String html = FameUtil.mdToHtml(article.getContent());
        article.setContent(html);
    }

    /**
     * 文章内容转为预览html
     *
     * @param article 文章实体类
     */
    private void transformPreView(Articles article) {
        String html = FameUtil.mdToHtml(FameUtil.getPreView(article.getContent()));
        article.setContent(html);
    }


}
