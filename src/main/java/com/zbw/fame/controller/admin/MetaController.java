package com.zbw.fame.controller.admin;

import com.zbw.fame.controller.BaseController;
import com.zbw.fame.model.Metas;
import com.zbw.fame.service.MetasService;
import com.zbw.fame.util.RestResponse;
import com.zbw.fame.util.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 属性(标签和分类)管理 Controller
 *
 * @auther zbw
 * @create 2017/8/28 23:16
 */
@RestController
@RequestMapping("/api/admin/meta")
public class MetaController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(MetaController.class);

    @Autowired
    private MetasService metasService;

    /**
     * 获取所有属性
     *
     * @return
     */
    @RequestMapping(value = "/metas", method = RequestMethod.GET)
    public RestResponse getAll(String type) {
        if (StringUtils.isEmpty(type)) {
            return this.error_nullParam();
        }
        List<Metas> metas;
        switch (type) {
            case Types.CATEGORY:
                metas = metasService.getMetas(Types.CATEGORY);
                break;
            case Types.TAG:
                metas = metasService.getMetas(Types.TAG);
                break;
            default:
                return this.error_nullParam();
        }

        return RestResponse.ok(metas);
    }

    /**
     * 根据name删除分类
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "/category/{name}", method = RequestMethod.DELETE)
    public RestResponse deleteCategory(@PathVariable String name) {
        if (metasService.deleteMetas(name, Types.CATEGORY)) {
            return RestResponse.ok();
        }
        return RestResponse.fail();
    }

    /**
     * 添加一个分类
     *
     * @param name
     * @param aId
     * @return
     */
    @RequestMapping(value = "/category", method = RequestMethod.POST)
    public RestResponse saveCategory(String name, Integer aId) {
        if (metasService.saveMetas(name, Types.CATEGORY, aId)) {
            return RestResponse.ok();
        }
        return RestResponse.fail();
    }
}
