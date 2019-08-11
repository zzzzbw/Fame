package com.zbw.fame.controller;


import com.zbw.fame.model.domain.Comment;
import com.zbw.fame.model.domain.Note;
import com.zbw.fame.model.domain.Post;
import com.zbw.fame.model.dto.*;
import com.zbw.fame.model.enums.CommentAssessType;
import com.zbw.fame.service.*;
import com.zbw.fame.util.FameConsts;
import com.zbw.fame.util.FameUtil;
import com.zbw.fame.util.RestResponse;
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
public class FrontController {

    private final PostService postService;

    private final NoteService noteService;

    private final CategoryService categoryService;

    private final TagService tagService;

    private final CommentService commentService;

    private final EmailService emailService;

    private final OptionService optionService;

    /**
     * 文章列表
     *
     * @param page  第几页
     * @param limit 每页数量
     * @return {@see Pagination<Post>}
     */
    @GetMapping("post")
    public RestResponse<Pagination<Post>> home(@RequestParam(required = false, defaultValue = "0") Integer page,
                                               @RequestParam(required = false, defaultValue = FameConsts.PAGE_SIZE) Integer limit,
                                               @RequestParam(required = false, defaultValue = "id") List<String> sort) {
        Page<Post> posts = postService.pageFrontArticle(page, limit, sort);
        return RestResponse.ok(Pagination.of(posts));
    }

    /**
     * 文章内容页
     *
     * @param id 文章id
     * @return {@see Article}
     */
    @GetMapping("post/{id}")
    public RestResponse<Post> post(@PathVariable Integer id) {
        Post post = postService.getFrontArticle(id);
        this.updateHits(post);
        return RestResponse.ok(post);
    }

    /**
     * 点击量添加
     *
     * @param post 文章实体
     */
    private void updateHits(Post post) {
        int hits = post.getHits() + 1;
        post.setHits(hits);
        if (hits % FameConsts.CACHE_ARTICLE_HITS_SAVE == 0) {
            postService.updateHits(post.getId(), hits);
        }
    }


    /**
     * 标签页
     *
     * @return {@see List<MetaInfo>}
     */
    @GetMapping("tag")
    public RestResponse<List<MetaInfo>> tag() {
        List<MetaInfo> metaInfos = tagService.getFrontMetaInfos();
        return RestResponse.ok(metaInfos);
    }

    /**
     * 分类页
     *
     * @return {@see List<MetaInfo>}
     */
    @GetMapping("category")
    public RestResponse<List<MetaInfo>> category() {
        List<MetaInfo> metaInfos = categoryService.getFrontMetaInfos();
        return RestResponse.ok(metaInfos);
    }

    /**
     * 归档页
     *
     * @return {@see List<Archive>}
     */
    @GetMapping("archive")
    public RestResponse<List<Archive>> archive() {
        List<Archive> archives = postService.getArchives();
        return RestResponse.ok(archives);
    }

    /**
     * 获取自定义页面的列表,根据权重排序
     *
     * @return {@see List<NoteInfo>}
     */
    @GetMapping("note")
    public RestResponse<List<NoteInfo>> noteList() {
        List<NoteInfo> notes = noteService.getFrontNoteList();
        return RestResponse.ok(notes);
    }

    /**
     * 自定义页面
     *
     * @param id 页面id
     * @return {@see Article}
     */
    @GetMapping("note/{id}")
    public RestResponse<Note> note(@PathVariable Integer id) {
        Note note = noteService.getFrontArticle(id);
        return RestResponse.ok(note);
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
                                                               @RequestParam(required = false, defaultValue = FameConsts.PAGE_SIZE) Integer limit) {
        Page<Comment> comments = commentService.getCommentsByArticleId(page, limit, articleId);
        return RestResponse.ok(Pagination.of(comments));
    }


    /**
     * 发表评论
     *
     * @param articleId 文章id
     * @param parentId  父评论id
     * @param content   评论内容
     * @param name      评论用户名
     * @param email     评论用户email
     * @param website   评论用户网址
     * @return {@see RestResponse.ok()}
     */
    @PostMapping("comment")
    public RestResponse addComment(@RequestParam Integer articleId,
                                   @RequestParam(required = false) Integer parentId,
                                   @RequestParam String content,
                                   @RequestParam String name,
                                   @RequestParam(required = false) String email,
                                   @RequestParam(required = false) String website) {
        Comment comments = new Comment();
        comments.setArticleId(articleId);
        comments.setParentId(parentId);
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
        if (null != commentDetail.getParentComment() && !StringUtils.isEmpty(commentDetail.getParentComment().getEmail())) {
            emailService.sendEmailToUser(commentDetail, commentDetail.getParentComment().getEmail());
        }
        return RestResponse.ok();
    }

    /**
     * 顶或踩评论
     *
     * @param commentId 评论id
     * @param assess    点评类型 {@link CommentAssessType}
     * @return {@see RestResponse.ok()}
     */
    @PostMapping("comment/{commentId}/assess")
    public RestResponse assessComment(@PathVariable Integer commentId, @RequestParam CommentAssessType assess) {
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
        Map<String, String> map = optionService.getFrontOptionMap();
        return RestResponse.ok(map);
    }
}
