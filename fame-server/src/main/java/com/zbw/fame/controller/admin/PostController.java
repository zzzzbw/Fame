package com.zbw.fame.controller.admin;

import com.zbw.fame.model.domain.Post;
import com.zbw.fame.model.domain.User;
import com.zbw.fame.service.PostService;
import com.zbw.fame.util.RestResponse;
import com.zbw.fame.util.Types;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author zhangbowen
 * @since 2019/7/22 17:50
 */
@RestController
@RequestMapping("/api/admin/post")
public class PostController extends AbstractArticleController<Post> {

    private final PostService postService;

    public PostController(PostService postService) {
        super(postService);
        this.postService = postService;
    }

    /**
     * 新增或保存文章
     *
     * @param id           文章id
     * @param title        文章标题
     * @param content      文章内容
     * @param tags         文章标签
     * @param category     文章分类
     * @param status       {@link Types#DRAFT},{@link Types#PUBLISH}
     * @param allowComment 是否允许评论
     * @return {@see RestResponse.ok()}
     */
    @PostMapping
    public RestResponse save(@RequestParam(value = "id", required = false) Integer id,
                             @RequestParam(value = "title") String title,
                             @RequestParam(value = "content") String content,
                             @RequestParam(value = "tags") String tags,
                             @RequestParam(value = "category") String category,
                             @RequestParam(value = "status", defaultValue = Types.DRAFT) String status,
                             @RequestParam(value = "allowComment", defaultValue = "false") Boolean allowComment,
                             @RequestParam(value = "created") Long created,
                             @RequestParam(value = "modified") Long modified) {
        User user = this.user();
        Post post = new Post();
        if (!StringUtils.isEmpty(id)) {
            post.setId(id);
        }
        post.setTitle(title);
        post.setContent(content);
        post.setTags(tags);
        post.setCategory(category);
        post.setStatus(status);
        post.setAllowComment(allowComment);
        post.setAuthorId(user.getId());
        post.setCreated(new Date(created));
        post.setModified(new Date(modified));
        Integer postId = postService.save(post);
        return RestResponse.ok(postId);
    }
}
