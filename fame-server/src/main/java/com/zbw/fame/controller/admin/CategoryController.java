package com.zbw.fame.controller.admin;

import com.zbw.fame.model.domain.Category;
import com.zbw.fame.service.CategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分类管理 Controller
 *
 * @author zbw
 * @since 2019/7/21 10:14
 */
@RestController
@RequestMapping("/api/admin/category")
public class CategoryController extends AbstractMetaController<Category> {

    public CategoryController(CategoryService categoryService) {
        super(categoryService);
    }

}
