package com.zbw.fame.controller.admin;

import com.zbw.fame.controller.BaseController;
import com.zbw.fame.dto.Page;
import com.zbw.fame.service.ArticlesService;
import com.zbw.fame.util.RestResponse;
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

    /**
     * 自定义信息列表
     * @param page
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public RestResponse index(@RequestParam(required = false, defaultValue = "1") Integer page) {
        List<Page> pages = articlesService.getPages(page);
        return RestResponse.ok(pages);
    }

    @RequestMapping(value = "/title",method = RequestMethod.GET)
    public RestResponse showPage(@PathVariable String title){
        Page page = articlesService.getPage(title);
    }
}
