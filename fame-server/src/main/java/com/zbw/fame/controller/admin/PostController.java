package com.zbw.fame.controller.admin;

import com.zbw.fame.model.domain.Post;
import com.zbw.fame.model.domain.User;
import com.zbw.fame.model.param.SavePostParam;
import com.zbw.fame.service.PostService;
import com.zbw.fame.util.FameUtils;
import com.zbw.fame.util.RestResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author zzzzbw
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
     * @return {@link RestResponse#ok()}
     */
    @PostMapping
    public RestResponse<Integer> save(@RequestBody @Valid SavePostParam param) {
        Post post = FameUtils.convertTo(param, Post.class);
        User user = FameUtils.getLoginUser();
        post.setAuthorId(user.getId());
        Integer postId = postService.save(post);
        return RestResponse.ok(postId);
    }
}
