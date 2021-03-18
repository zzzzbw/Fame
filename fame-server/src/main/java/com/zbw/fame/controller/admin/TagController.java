package com.zbw.fame.controller.admin;

import com.zbw.fame.model.dto.TagInfoDto;
import com.zbw.fame.model.entity.Tag;
import com.zbw.fame.model.param.SaveTagParam;
import com.zbw.fame.service.TagServiceNew;
import com.zbw.fame.util.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    private final TagServiceNew tagServiceNew;

    /**
     * 获取所有属性
     *
     * @return {@see List<TagInfoDto>}
     */
    @GetMapping
    public RestResponse<List<TagInfoDto>> getAll() {
        List<TagInfoDto> tagInfos = tagServiceNew.listTagInfo(false);
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
        tagServiceNew.delete(id);
        return RestResponse.ok();
    }

    /**
     * 添加一个标签
     *
     * @param param 新增参数
     * @return {@link Tag}
     */
    @PostMapping
    public RestResponse<Tag> save(SaveTagParam param) {
        Tag tag = tagServiceNew.createOrUpdate(param);
        return RestResponse.ok(tag);
    }
}
