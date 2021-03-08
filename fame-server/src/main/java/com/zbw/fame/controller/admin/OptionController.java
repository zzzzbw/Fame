package com.zbw.fame.controller.admin;

import com.zbw.fame.service.SysOptionService;
import com.zbw.fame.util.RestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author zzzzbw
 * @since 2019-05-21 21:14
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/option")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OptionController {

    private final SysOptionService sysOptionService;

    /**
     * 获取所有设置
     *
     * @return Map
     */
    @GetMapping("all")
    public RestResponse<Map<String, String>> getAllOptions() {
        return RestResponse.ok(sysOptionService.getAllOptionMap());
    }

    /**
     * 保存所有设置
     *
     * @param options 设置key-value
     * @return {@link RestResponse#ok()}
     */
    @PostMapping("save")
    public RestResponse<RestResponse.Empty> saveAllOptions(@RequestBody Map<String, String> options) {
        sysOptionService.save(options);
        return RestResponse.ok();
    }
}
