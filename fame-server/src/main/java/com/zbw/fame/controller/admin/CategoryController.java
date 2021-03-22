package com.zbw.fame.controller.admin;

import com.zbw.fame.model.dto.CategoryInfoDto;
import com.zbw.fame.model.entity.Category;
import com.zbw.fame.model.param.SaveCategoryParam;
import com.zbw.fame.service.CategoryServiceNew;
import com.zbw.fame.util.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理 Controller
 *
 * @author zzzzbw
 * @since 2019/7/21 10:14
 */
@RestController
@RequestMapping("/api/admin/category")

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CategoryController {

    private final CategoryServiceNew categoryServiceNew;

    /**
     * 获取所有分类
     *
     * @return {@see List<CategoryInfoDto>}
     */
    @GetMapping
    public RestResponse<List<CategoryInfoDto>> getAll() {
        List<CategoryInfoDto> categoryInfos = categoryServiceNew.listCategoryInfo(false);
        return RestResponse.ok(categoryInfos);
    }

    /**
     * 根据id删除标分类
     *
     * @param id 分类id
     * @return {@link RestResponse#ok()}
     */
    @DeleteMapping("{id}")
    public RestResponse<RestResponse.Empty> delete(@PathVariable Integer id) {
        categoryServiceNew.delete(id);
        return RestResponse.ok();
    }

    /**
     * 添加一个分类
     *
     * @param param 新增参数
     * @return {@link Category}
     */
    @PostMapping
    public RestResponse<Category> save(SaveCategoryParam param) {
        Category category = categoryServiceNew.createOrUpdate(param);
        return RestResponse.ok(category);
    }

}
