package com.zbw.fame.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zbw.fame.model.dto.CommentDto;
import com.zbw.fame.model.dto.Pagination;
import com.zbw.fame.model.entity.Comment;
import com.zbw.fame.service.CommentService;
import com.zbw.fame.util.FameConst;
import com.zbw.fame.util.FameUtils;
import com.zbw.fame.util.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 后台评论管理 Controller
 *
 * @author zzzzbw
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
    public RestResponse<Pagination<Comment>> index(@RequestParam(required = false, defaultValue = FameConst.DEFAULT_PAGE) Integer page,
                                                   @RequestParam(required = false, defaultValue = FameConst.PAGE_SIZE) Integer limit) {
        Page<Comment> comments = commentService.pageCommentAdmin(page, limit);
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
        CommentDto comment = commentService.getCommentDto(id);
        if (null != comment.getParentComment()) {
            comment.getParentComment().setContent(FameUtils.mdToHtml(comment.getParentComment().getContent()));
        }
        comment.setContent(FameUtils.mdToHtml(comment.getContent()));
        return RestResponse.ok(comment);
    }

    /**
     * 删除评论
     *
     * @param id 评论id
     * @return {@link RestResponse#ok()}
     */
    @DeleteMapping("{id}")
    public RestResponse<RestResponse.Empty> delete(@PathVariable Integer id) {
        commentService.deleteComment(id);
        return RestResponse.ok();
    }

    /**
     * 获取评论数量
     *
     * @return {@see Integer}
     */
    @GetMapping("count")
    public RestResponse<Integer> count() {
        return RestResponse.ok(commentService.count());
    }

}
