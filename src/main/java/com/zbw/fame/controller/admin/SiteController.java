package com.zbw.fame.controller.admin;

import com.zbw.fame.controller.BaseController;
import com.zbw.fame.model.Logs;
import com.zbw.fame.service.LogsService;
import com.zbw.fame.util.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 网站信息 Controller
 *
 * @auther zbw
 * @create 2017/10/12 20:27
 */
@RestController
@RequestMapping("/admin/site")
public class SiteController extends BaseController {

    @Autowired
    private LogsService logsService;

    @RequestMapping(value = "/logs")
    public RestResponse getLogs(@RequestParam(defaultValue = "1") Integer page) {
        List<Logs> logs = logsService.getLogs(page);
        return RestResponse.ok(logs);
    }

    /**
     * 获取网站访问量
     *
     * @return
     */
    @RequestMapping(value = "/visit", method = RequestMethod.GET)
    public RestResponse visit() {
        Integer visit = logsService.getVisit();
        return RestResponse.ok(visit);
    }


}
