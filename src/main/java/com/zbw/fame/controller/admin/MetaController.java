package com.zbw.fame.controller.admin;

import com.zbw.fame.controller.BaseController;
import com.zbw.fame.service.MetasService;
import com.zbw.fame.util.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 属性(标签和分类)管理 Controller
 *
 * @author zbw
 * @create 2017/8/28 23:16
 */
@RestController
@RequestMapping("/api/admin/meta")
public class MetaController extends BaseController {

    @Autowired
    private MetasService metasService;

    /**
     * 获取所有属性
     *
     * @return
     */
    @GetMapping
    public RestResponse getAll(@RequestParam String type) {
        return RestResponse.ok(metasService.getMetaDto(type));
    }

    /**
     * 根据name删除分类
     *
     * @param name
     * @return
     */
    @DeleteMapping
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
    @PostMapping
    public RestResponse saveMeta(@RequestParam String name, @RequestParam String type) {
        if (metasService.saveMeta(name, type)) {
            return RestResponse.ok();
        }
        return RestResponse.fail();
    }

    /**
     * 根据id修改分类
     *
     * @param id
     * @param name
     * @param type
     * @return
     */
    @PostMapping("{id}")
    public RestResponse updateMeta(@PathVariable Integer id, @RequestParam String name, @RequestParam String type) {
        if (metasService.updateMeta(id, name, type)) {
            return RestResponse.ok();
        }
        return RestResponse.fail();
    }
}
