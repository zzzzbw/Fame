package com.zbw.fame.controller.admin;

import com.zbw.fame.controller.BaseController;
import com.zbw.fame.service.MetasService;
import com.zbw.fame.util.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 属性(标签和分类)管理 Controller
 *
 * @auther zbw
 * @create 2017/8/28 23:16
 */
@RestController
@RequestMapping("/admin/meta")
public class MetaController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(MetaController.class);

    @Autowired
    private MetasService metasService;

    /**
     * 获取所有属性
     *
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public RestResponse getAll(@RequestParam String type) {
        return RestResponse.ok(metasService.getMetas(type));
    }

    /**
     * 根据name删除分类
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public RestResponse deleteMeta(@RequestParam String name, @RequestParam String type) {
        if (metasService.deleteMeta(name, type)) {
            return RestResponse.ok();
        }
        return RestResponse.fail();
    }

    /**
     * 添加一个分类
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public RestResponse saveMeta(@RequestParam String name, @RequestParam String type) {
        if (metasService.saveMeta(name, type)) {
            return RestResponse.ok();
        }
        return RestResponse.fail();
    }
}
