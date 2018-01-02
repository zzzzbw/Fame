package com.zbw.fame.controller.admin;

import com.github.pagehelper.Page;
import com.zbw.fame.controller.BaseController;
import com.zbw.fame.dto.Pagination;
import com.zbw.fame.model.Articles;
import com.zbw.fame.model.Users;
import com.zbw.fame.service.ArticlesService;
import com.zbw.fame.service.LogsService;
import com.zbw.fame.util.FameConsts;
import com.zbw.fame.util.FameUtil;
import com.zbw.fame.util.RestResponse;
import com.zbw.fame.util.Types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 后台文章管理 Controller
 *
 * @author zbw
 * @create 2017/7/11 19:52
 */
@RestController
@RequestMapping("/api/admin/article")
public class ArticleController extends BaseController {

    @Autowired
    private ArticlesService articlesService;

    @Autowired
    private LogsService logsService;


    /**
     * 文章信息列表
     *
     * @param page
     * @return
     */
    @GetMapping
    public RestResponse index(@RequestParam(required = false, defaultValue = "1") Integer page,
                              @RequestParam(required = false, defaultValue = FameConsts.PAGE_SIZE) Integer limit) {
        Page<Articles> articles = articlesService.getArticles(page, limit);
        return RestResponse.ok(new Pagination<Articles>(articles));
    }

    /**
     * 单个文章信息
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public RestResponse showArticle(@PathVariable Integer id) {
        Articles article = articlesService.get(id);
        if (null == article) {
            return this.error404();
        }
        return RestResponse.ok(article);
    }

    /**
     * 保存文章
     *
     * @param id
     * @param title
     * @param content
     * @param tags
     * @param category
     * @param status
     * @param allowComment
     * @return
     */
    @PostMapping
    public RestResponse saveArticle(@RequestParam(value = "id", required = false) Integer id,
                                    @RequestParam(value = "title") String title,
                                    @RequestParam(value = "content") String content,
                                    @RequestParam(value = "tags") String tags,
                                    @RequestParam(value = "category") String category,
                                    @RequestParam(value = "status", defaultValue = Types.DRAFT) String status,
                                    @RequestParam(value = "allowComment", defaultValue = "false") Boolean allowComment) {
        Users user = this.user();
        if (null == user) {
            return RestResponse.fail("未登陆，请先登陆");
        }
        Articles article = new Articles();
        if (!StringUtils.isEmpty(id)) {
            article.setId(id);
        }
        article.setTitle(title);
        article.setContent(content);
        article.setTags(tags);
        article.setCategory(category);
        article.setStatus(status);
        article.setAllowComment(allowComment);
        article.setAuthorId(user.getId());
        articlesService.saveArticle(article);
        return RestResponse.ok("保存文章成功");
    }

    /**
     * 删除文章
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public RestResponse deleteArticle(@PathVariable Integer id) {
        if (articlesService.deleteArticle(id)) {
            logsService.save(Types.LOG_ACTION_DELETE, "id:" + id, Types.LOG_MESSAGE_DELETE_ARTICLE, Types.LOG_TYPE_OPERATE, FameUtil.getIp());
            return RestResponse.ok("删除文章成功");
        } else {
            return RestResponse.fail();
        }
    }

    @GetMapping("count")
    public RestResponse count() {
        return RestResponse.ok(articlesService.count());
    }

}
