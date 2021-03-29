package com.zbw.fame.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbw.fame.model.dto.CategoryInfoDto;
import com.zbw.fame.model.entity.Category;
import com.zbw.fame.model.param.SaveCategoryParam;

import java.util.List;

/**
 * @author by zzzzbw
 * @since 2021/03/19 10:26
 */
public interface CategoryService extends IService<Category> {

    /**
     * 删除标签
     *
     * @param id 属性名
     */
    void delete(Integer id);

    /**
     * 保存或更新标签
     *
     * @param param
     * @return
     */
    Category createOrUpdate(SaveCategoryParam param);

    /**
     * 获取标签信息
     *
     * @param isFront 是否为前端标签
     * @return List<CategoryInfoDto>
     */
    List<CategoryInfoDto> listCategoryInfo(boolean isFront);


}
