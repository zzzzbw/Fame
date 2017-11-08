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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/logs")
    public RestResponse getLogs(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = FameConsts.PAGE_SIZE) Integer limit) {
        Page<Logs> logs = logsService.getLogs(page, limit);
        return RestResponse.ok(new Pagination<Logs>(logs));
    }

    /**
     * 获取网站访问量
     *
     * @return
     */
    @RequestMapping(value = "/visit", method = RequestMethod.GET)
    public RestResponse visit() {
        Integer visit = siteService.getVisit();
        return RestResponse.ok(visit);
    }

    /**
     * 获取网站设置缓存
     *
     * @return
     */
    @RequestMapping(value = "/static", method = RequestMethod.GET)
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
    @RequestMapping(value = "/static", method = RequestMethod.POST)
    public RestResponse getSiteStatic(@RequestParam String title, @RequestParam String description, @RequestParam String keywords) {
        siteService.saveSiteStatic(title, description, keywords);
        return RestResponse.ok();
    }


}
