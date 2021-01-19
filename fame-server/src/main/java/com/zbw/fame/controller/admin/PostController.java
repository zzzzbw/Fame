package com.zbw.fame.controller.admin;

import com.zbw.fame.model.domain.Post;
import com.zbw.fame.model.domain.User;
import com.zbw.fame.model.enums.ArticleStatus;
import com.zbw.fame.model.param.SavePostParam;
import com.zbw.fame.service.PostService;
import com.zbw.fame.util.FameUtil;
import com.zbw.fame.util.RestResponse;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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
    public RestResponse<Integer> save(SavePostParam param) {
        Post post = FameUtil.convertTo(param, Post.class);
        // TODO created、modified时间转换
        User user = FameUtil.getLoginUser();
        post.setAuthorId(user.getId());
        Integer postId = postService.save(post);
        return RestResponse.ok(postId);
    }
}
