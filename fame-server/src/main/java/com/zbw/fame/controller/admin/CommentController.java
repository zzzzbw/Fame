package com.zbw.fame.controller.admin;

import com.zbw.fame.exception.NotFoundException;
import com.zbw.fame.model.domain.Comment;
import com.zbw.fame.model.dto.CommentDto;
import com.zbw.fame.model.dto.Pagination;
import com.zbw.fame.service.CommentService;
import com.zbw.fame.util.FameConsts;
import com.zbw.fame.util.FameUtil;
import com.zbw.fame.util.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 后台评论管理 Controller
 *
 * @author zbw
 * @since 2018/1/21 10:47
 */
@RestController
@RequestMapping("/api/admin/comment")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentController {

    private final CommentService commentService;

    /**
     * 获取所有评论
     *
     * @param page  第几页
     * @param limit 每页数量
     * @return {@see Pagination<Comment>}
     */
    @GetMapping
    public RestResponse<Pagination<Comment>> index(@RequestParam(required = false, defaultValue = "0") Integer page,
                                                   @RequestParam(required = false, defaultValue = FameConsts.PAGE_SIZE) Integer limit) {
        Page<Comment> comments = commentService.pageAdminComments(page, limit);
        return RestResponse.ok(Pagination.of(comments));
    }

    /**
     * 获取评论详情
     *
     * @param id 评论id
     * @return {@see CommentDto}
     */
    @GetMapping("{id}")
    public RestResponse<Comment> detail(@PathVariable Integer id) {
        CommentDto comment = commentService.getCommentDetail(id);
        if (null != comment.getParentComment()) {
            comment.getParentComment().setContent(FameUtil.mdToHtml(comment.getParentComment().getContent()));
        }
        comment.setContent(FameUtil.mdToHtml(comment.getContent()));
        return RestResponse.ok(comment);
    }

    /**
     * 删除评论
     *
     * @param id 评论id
     * @return {@see RestResponse.ok()}
     */
    @DeleteMapping("{id}")
    public RestResponse delete(@PathVariable Integer id) {
        commentService.deleteComment(id);
        return RestResponse.ok();
    }

    /**
     * 获取评论数量
     *
     * @return {@see Integer}
     */
    @GetMapping("count")
    public RestResponse<Long> count() {
        return RestResponse.ok(commentService.count());
    }

}
