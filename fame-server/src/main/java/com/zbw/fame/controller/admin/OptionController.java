package com.zbw.fame.controller.admin;

import com.zbw.fame.controller.BaseController;
import com.zbw.fame.service.OptionService;
import com.zbw.fame.util.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author zbw
 * @since 2019-05-21 21:14
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/option")
public class OptionController extends BaseController {

    @Autowired
    private OptionService optionService;

    /**
     * 获取所有设置
     *
     * @return Map
     */
    @GetMapping("all")
    public RestResponse getAllOptions() {
        return RestResponse.ok(optionService.getAllOptionMap());
    }

    /**
     * 保存所有设置
     *
     * @param options 设置key-value
     * @return {@see RestResponse.ok()}
     */
    @PostMapping("save")
    public RestResponse saveAllOptions(@RequestParam Map<String, String> options) {
        optionService.save(options);
        return RestResponse.ok();
    }
}
