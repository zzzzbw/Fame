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
 * 自定义页面管理 Controller
 *
 * @author zbw
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
    public RestResponse index(@RequestParam(required = false, defaultValue = "1") Integer page,
                              @RequestParam(required = false, defaultValue = FameConsts.PAGE_SIZE) Integer limit) {
        Page<Articles> pages = articlesService.getPages(page,limit);
        return RestResponse.ok(new Pagination<Articles>(pages));
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
            return this.error404();
        }
        return RestResponse.ok(page);
    }

    /**
     * 保存自定义页面
     *
     * @param id
     * @param title
     * @param content
     * @param status
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public RestResponse savePage(@RequestParam(value = "id", required = false) Integer id,
                                 @RequestParam(value = "title") String title,
                                 @RequestParam(value = "content") String content,
                                 @RequestParam(value = "status", defaultValue = Types.DRAFT) String status) {
        Users user = this.user();
        if (null == user) {
            return RestResponse.fail("未登陆，请先登陆");
        }
        Articles page = new Articles();
        if (!StringUtils.isEmpty(id)) {
            page.setId(id);
        }
        page.setTitle(title);
        page.setContent(content);
        page.setStatus(status);
        page.setAuthorId(user.getId());
        articlesService.savePage(page);
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
            return RestResponse.ok("删除自定义页面成功");
        } else {
            return RestResponse.fail();
        }
    }
}
