package com.zbw.fame.controller.admin;

import com.zbw.fame.model.dto.TagInfoDto;
import com.zbw.fame.model.entity.Tag;
import com.zbw.fame.model.param.SaveTagParam;
import com.zbw.fame.service.TagService;
import com.zbw.fame.util.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 标签管理 Controller
 *
 * @author zzzzbw
 * @since 2019/7/21 10:14
 */
@RestController
@RequestMapping("/api/admin/tag")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TagController {

    private final TagService tagService;

    /**
     * 获取所有标签
     *
     * @return {@see List<TagInfoDto>}
     */
    @GetMapping
    public RestResponse<List<TagInfoDto>> getAll() {
        List<TagInfoDto> tagInfos = tagService.listTagInfo(false);
        return RestResponse.ok(tagInfos);
    }

    /**
     * 根据id删除标签
     *
     * @param id 标签id
     * @return {@link RestResponse#ok()}
     */
    @DeleteMapping("{id}")
    public RestResponse<RestResponse.Empty> delete(@PathVariable Integer id) {
        tagService.delete(id);
        return RestResponse.ok();
    }

    /**
     * 添加一个标签
     *
     * @param param 新增参数
     * @return {@link Tag}
     */
    @PostMapping
    public RestResponse<Tag> save(@RequestBody @Valid SaveTagParam param) {
        Tag tag = tagService.createOrUpdate(param);
        return RestResponse.ok(tag);
    }
}
