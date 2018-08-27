package com.zbw.fame.controller.admin;

import com.github.pagehelper.Page;
import com.zbw.fame.controller.BaseController;
import com.zbw.fame.dto.Pagination;
import com.zbw.fame.dto.SiteStatic;
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
 * @since 2017/10/12 20:27
 */
@RestController
@RequestMapping("/api/admin/site")
public class SiteController extends BaseController {

    @Autowired
    private LogsService logsService;

    @Autowired
    private SiteService siteService;

    /**
     * 获取日志列表
     *
     * @param page  第几页
     * @param limit 每页数量
     * @return {@see Pagination<Logs>}
     */
    @GetMapping("logs")
    public RestResponse getLogs(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = FameConsts.PAGE_SIZE) Integer limit) {
        Page<Logs> logs = logsService.getLogs(page, limit);
        return RestResponse.ok(new Pagination<Logs>(logs));
    }

    /**
     * 获取网站设置缓存
     *
     * @return {@see SiteStatic}
     */
    @GetMapping("static")
    public RestResponse getSiteStatic() {
        return RestResponse.ok(siteService.getSiteStatic());
    }

    /**
     * 保存网站设置缓存
     *
     * @param title         网页title
     * @param description   网页description
     * @param keywords      网页keywords
     * @param emailSend     是否发送邮件提示
     * @param emailHost     邮箱Host
     * @param emailPort     邮箱Port
     * @param emailUsername 邮箱用户名
     * @param emailPassword 邮箱密码
     * @return {@see RestResponse.ok()}
     */
    @PostMapping("static")
    public RestResponse getSiteStatic(@RequestParam String title, @RequestParam String description,
                                      @RequestParam String keywords, @RequestParam Boolean emailSend,
                                      @RequestParam(required = false) String emailHost, @RequestParam(required = false) Integer emailPort,
                                      @RequestParam(required = false) String emailUsername, @RequestParam(required = false) String emailPassword) {
        SiteStatic siteStatic = new SiteStatic();
        siteStatic.setTitle(title);
        siteStatic.setDescription(description);
        siteStatic.setKeywords(keywords);
        siteStatic.setEmailSend(emailSend);
        if (emailSend) {
            siteStatic.setEmailHost(emailHost);
            siteStatic.setEmailPort(emailPort);
            siteStatic.setEmailUsername(emailUsername);
            siteStatic.setEmailPassword(emailPassword);
        }
        siteService.saveSiteStatic(siteStatic);
        return RestResponse.ok();
    }


}
