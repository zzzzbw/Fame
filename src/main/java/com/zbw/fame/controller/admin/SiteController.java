package com.zbw.fame.controller.admin;

import com.github.pagehelper.Page;
import com.zbw.fame.controller.BaseController;
import com.zbw.fame.dto.Pagination;
import com.zbw.fame.model.Logs;
import com.zbw.fame.service.LogsService;
import com.zbw.fame.service.SiteService;
import com.zbw.fame.util.FameConsts;
import com.zbw.fame.util.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 网站信息 Controller
 *
 * @author zbw
 * @create 2017/10/12 20:27
 */
@RestController
@RequestMapping("/api/admin/site")
public class SiteController extends BaseController {

    @Autowired
    private LogsService logsService;

    @Autowired
    private SiteService siteService;

    @GetMapping("logs")
    public RestResponse getLogs(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = FameConsts.PAGE_SIZE) Integer limit) {
        Page<Logs> logs = logsService.getLogs(page, limit);
        return RestResponse.ok(new Pagination<Logs>(logs));
    }

    /**
     * 获取网站设置缓存
     *
     * @return
     */
    @GetMapping("static")
    public RestResponse getSiteStatic() {
        return RestResponse.ok(siteService.getSiteStatic());
    }

    /**
     * 保存网站设置缓存
     *
     * @param title
     * @param description
     * @param keywords
     * @return
     */
    @PostMapping("static")
    public RestResponse getSiteStatic(@RequestParam String title, @RequestParam String description, @RequestParam String keywords) {
        siteService.saveSiteStatic(title, description, keywords);
        return RestResponse.ok();
    }


}
