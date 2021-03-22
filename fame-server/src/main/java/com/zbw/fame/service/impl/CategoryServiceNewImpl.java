package com.zbw.fame.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbw.fame.exception.NotFoundException;
import com.zbw.fame.mapper.CategoryMapper;
import com.zbw.fame.model.dto.ArticleInfoDto;
import com.zbw.fame.model.dto.CategoryInfoDto;
import com.zbw.fame.model.entity.Article;
import com.zbw.fame.model.entity.BaseEntity;
import com.zbw.fame.model.entity.Category;
import com.zbw.fame.model.param.SaveCategoryParam;
import com.zbw.fame.service.ArticleCategoryService;
import com.zbw.fame.service.CategoryServiceNew;
import com.zbw.fame.util.FameUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author by zzzzbw
 * @since 2021/03/19 10:26
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class CategoryServiceNewImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryServiceNew {

    private final ArticleCategoryService articleCategoryService;

    @Override
    public void delete(Integer id) {
        recursionDelete(id);
    }

    private void recursionDelete(Integer id) {
        Category category = getById(id);
        if (null == category) {
            return;
        }
        removeById(id);
        articleCategoryService.deleteByCategoryId(id);

        if (null != category.getParentId()) {
            recursionDelete(category.getParentId());
        }
    }

    @Override
    public Category createOrUpdate(SaveCategoryParam param) {
        Integer parentId = param.getParentId();
        Optional.ofNullable(getById(parentId))
                .orElseThrow(() -> new NotFoundException(Category.class));

        Category category = FameUtils.convertTo(param, Category.class);
        saveOrUpdate(category);
        return category;
    }

    @Override
    public List<CategoryInfoDto> listCategoryInfo(boolean isFront) {
        List<Category> categories = list();
        if (CollectionUtils.isEmpty(categories)) {
            return Collections.emptyList();
        }

        Set<Integer> categoryIds = categories
                .stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());

        Map<Integer, List<Article>> articleMap = articleCategoryService.listArticleByCategoryIds(categoryIds, isFront);
        Map<Integer, List<Category>> childCategoryMap = categories.stream()
                .collect(Collectors.groupingBy(Category::getParentId));

        return categories.stream()
                .filter(category -> category.getParentId() == null)
                .map(category -> categoryToCategoryInfoDto(category, null, childCategoryMap, articleMap))
                .collect(Collectors.toList());
    }

    /**
     * Category转CategoryInfoDto
     *
     * @param category
     * @param parentDto
     * @param childCategoryMap
     * @param articleMap
     * @return
     */
    private CategoryInfoDto categoryToCategoryInfoDto(Category category,
                                                      CategoryInfoDto parentDto,
                                                      Map<Integer, List<Category>> childCategoryMap,
                                                      Map<Integer, List<Article>> articleMap) {
        CategoryInfoDto dto = new CategoryInfoDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setParentCategory(parentDto);

        // 填入分类下的文章信息
        List<ArticleInfoDto> articleInfos = articleMap.getOrDefault(category.getId(), Collections.emptyList())
                .stream()
                .map(ArticleInfoDto::new)
                .collect(Collectors.toList());
        dto.setArticleInfos(articleInfos);

        // 递归填入分类的子分类列表
        List<Category> childCategory = childCategoryMap.getOrDefault(category.getId(), Collections.emptyList());
        List<CategoryInfoDto> childCategories = childCategory.stream()
                .map(child -> categoryToCategoryInfoDto(child, dto, childCategoryMap, articleMap))
                .collect(Collectors.toList());
        dto.setChildCategories(childCategories);
        return dto;
    }

}
