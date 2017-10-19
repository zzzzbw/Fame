package com.zbw.fame.controller.admin;

import com.zbw.fame.controller.BaseController;
import com.zbw.fame.model.Articles;
import com.zbw.fame.model.Users;
import com.zbw.fame.service.ArticlesService;
import com.zbw.fame.service.LogsService;
import com.zbw.fame.util.FameUtil;
import com.zbw.fame.util.RestResponse;
import com.zbw.fame.util.Types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 自定义页面管理 Controller
 *
 * @auther zbw
 * @create 2017/10/17 12:28
 */
@RestController
@RequestMapping("/admin/page")
public class PageController extends BaseController {

    @Autowired
    private ArticlesService articlesService;

    @Autowired
    private LogsService logsService;

    /**
     * 自定义页面列表
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public RestResponse index(@RequestParam(required = false, defaultValue = "1") Integer page) {
        List<Articles> pages = articlesService.getPages(page);
        return RestResponse.ok(pages);
    }

    /**
     * 获取自定义页面信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}")
    public RestResponse showPage(@PathVariable Integer id) {
        Articles page = articlesService.getPage(id);
        if (null == page) {
            return this.error_404();
        }
        return RestResponse.ok(page);
    }

    /**
     * 保存自定义页面
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public RestResponse saveArticle(Articles page) {
        Users user = this.user();
        if (null == user) {
            return RestResponse.fail("未登陆，请先登陆");
        }
        page.setAuthorId(user.getId());
        Integer id = articlesService.savePage(page);
        return RestResponse.ok("保存文章成功");
    }

    /**
     * 删除文章
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public RestResponse deletePage(@PathVariable Integer id) {
        if (articlesService.deletePage(id)) {
            logsService.save(Types.LOG_ACTION_DELETE, "id:" + id, Types.LOG_MESSAGE_DELETE_PAGE, Types.LOG_TYPE_OPERATE, FameUtil.getIp());
            return RestResponse.ok();
        } else {
            return RestResponse.fail();
        }
    }
}
